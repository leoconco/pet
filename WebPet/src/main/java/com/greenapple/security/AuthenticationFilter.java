/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapple.security;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;

/**
 *
 * @author leonardo.contreras
 */
public class AuthenticationFilter implements Filter {

    private static final String TRUE_VALUE = "true";
    private static final String IS_RETURN_PARAM = "is_return";
    private static final String IS_RETURN = "?is_return=true";
    private FilterConfig filterConfig;
    private String pathToIgnore;
    private String pathToLogout;
    private String loginPage;
    private String redirectPage;
    private String openIdSelector;
    private String openIdParam;
    private String proxyUrl;
    private String proxyPort;
    private ConsumerManager manager;
    private static final String CONSUMER_KEY = "d2656882-7ba0-4fdd-8ae6-a74c9da35c8f";
    private static final String CONSUMER_SECRET = "a26e1f37-6f6e-4346-a521-b2d0ad4adbea";
    private static final String scopes = "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&";

    @Override
    public void init(FilterConfig pFilterConfig) throws ServletException {

        filterConfig = pFilterConfig;
        pathToIgnore = filterConfig.getInitParameter("public_url");
        pathToLogout = filterConfig.getInitParameter("logout_url");
        loginPage = filterConfig.getInitParameter("login_page");
        redirectPage = filterConfig.getInitParameter("redirect_page");
        openIdSelector = filterConfig.getInitParameter("openid_select");
        openIdParam = filterConfig.getInitParameter("openid_url_param");
        proxyUrl = filterConfig.getInitParameter("proxyurl_param");
        proxyPort = filterConfig.getInitParameter("proxyport_param");
        if(proxyUrl != null & proxyPort !=null){
            ProxyProperties proxyProps = new ProxyProperties();
            proxyProps.setProxyHostName(proxyUrl);
            proxyProps.setProxyPort(Integer.parseInt(proxyPort));
            HttpClientFactory.setProxyProperties(proxyProps);
        }        manager = new ConsumerManager();
        manager.setAssociations(new InMemoryConsumerAssociationStore());
        manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
        manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getServletPath();

        if (path.startsWith(pathToLogout)){
            httpResponse.setHeader("Cache-Control", "no-cache, no-store");
            httpResponse.setHeader("Pragma", "no-cache");
            httpRequest.getSession().invalidate();
        }

        if (path.startsWith(pathToIgnore)) {
            chain.doFilter(request, response);}
        else {
            HttpSession httpSession = httpRequest.getSession(false);
            if (TRUE_VALUE.equals(httpRequest.getParameter(IS_RETURN_PARAM))) {
                if (processAuthResponse(httpRequest)) {
                    chain.doFilter(request, response);
                } else {
                    httpSession = null;
                }
            } else if (httpSession == null || httpSession.getAttribute(Constants.AUTH_CONTEXT)== null) {
                if (path.startsWith(openIdSelector)) {
                    String urlOpenid = httpRequest.getParameter(openIdParam);
                    doAuthRequest(httpRequest, httpResponse, urlOpenid);


                } else {
                    RequestDispatcher dispatcher = filterConfig.getServletContext().getRequestDispatcher(loginPage);
                    dispatcher.forward(request, response);
                }

            } else {
                chain.doFilter(request, response);

            }
        }
    }

    @Override
    public void destroy() {
    }

    private void doAuthRequest(
            HttpServletRequest httpReq, HttpServletResponse httpResp, String urlOpenId)
            throws IOException, ServletException {
        try {
            StringBuilder returnToUrl = new StringBuilder(httpReq.getRequestURL());
            int selectorIx = returnToUrl.indexOf(openIdSelector);
            if(selectorIx>=0){
                returnToUrl.delete(selectorIx, selectorIx+openIdSelector.length());
            }
            returnToUrl.append(IS_RETURN);
            // perform discovery on the user-supplied identifier
            List discoveries = manager.discover(urlOpenId);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            // store the discovery information in the user's session
            httpReq.getSession().setAttribute("openid-disc", discovered);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl.toString());

            // Try SReg request first if supported else try the Attribute Exchange if supported.
            //
            if (discovered.hasType(Constants.SREG_EXTENTION_XRDS_TYPE)) {
                // open id UI extension request
                //authReq.setImmediate(true);
                authReq.addExtension(createSRegRequest());
            } else if (discovered.hasType(Constants.AX_EXTENTION_XRDS_TYPE)) {
                // attribute exchange request
                authReq.addExtension(createAXFetchRequest());
            }

            // Hybrid OAuth request
            // The site needs to register with the provider first with a valid realm added for this
            // to work
            //
            if (discovered.hasType(Constants.OAUTH_EXTENTION_XRDS_TYPE)) {
                authReq.addExtension(createOAuthRequest());
            }


            // dispatch the OpenId Authentication process
            //
            if (!discovered.isVersion2()) {
                // Option 1: GET HTTP-redirect to the OpenID Provider endpoint
                // The only method supported in OpenID 1.x
                // redirect-URL usually limited ~2048 bytes
                //
                String durl = authReq.getDestinationUrl(true);
                System.err.println(durl);
                httpResp.sendRedirect(durl);

            } else {
                // Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)
                //
                String url = authReq.getDestinationUrl(true);
                RequestDispatcher dispatcher = filterConfig.getServletContext().getRequestDispatcher(redirectPage);
                httpReq.setAttribute("parameterMap", httpReq.getParameterMap());
                httpReq.setAttribute("message", authReq);
                dispatcher.forward(httpReq, httpResp);
            }
        } catch (OpenIDException e) {
            throw new ServletException(e);
        }


    }

    private boolean processAuthResponse(HttpServletRequest httpReq)
            throws ServletException {
        try {


            // extract the parameters from the authentication response
            // (which comes in as a HTTP request from the OpenID provider)
            ParameterList response = new ParameterList(httpReq.getParameterMap());

            // retrieve the previously stored discovery information
            DiscoveryInformation discovered = (DiscoveryInformation) httpReq.getSession().getAttribute("openid-disc");

            // extract the receiving URL from the HTTP request
            StringBuffer receivingURL = httpReq.getRequestURL();
            String queryString = httpReq.getQueryString();
            if (queryString != null && queryString.length() > 0) {
                receivingURL.append("?").append(httpReq.getQueryString());
            }

            // verify the response; ConsumerManager needs to be the same
            // (static) instance used to place the authentication request
            VerificationResult verification = manager.verify(receivingURL.toString(), response, discovered);

            // examine the verification result and extract the verified
            // identifier
            Identifier verified = verification.getVerifiedId();
            if (verified != null) {
                AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

                AuthContext authContext = new AuthContext();


                if (authSuccess.hasExtension(OAuthMessage.OPENID_NS_OAUTH)) {
                    MessageExtension ext = authSuccess.getExtension(OAuthMessage.OPENID_NS_OAUTH);
                    if (ext instanceof OAuthResponse) {

                        OAuthResponse oauthresponse = (OAuthResponse) ext;

                        AccessToken accessToken = OAuth.getAccessToken(CONSUMER_KEY, CONSUMER_SECRET, oauthresponse.getRequestToken());

                        authContext.putProperty(Constants.OAUTH_ACCESS_TOKEN, accessToken);

                    }
                }

                if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
                    MessageExtension ext = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
                    if (ext instanceof SRegResponse) {
                        SRegResponse sregResp = (SRegResponse) ext;
                        for (Iterator<?> iter = sregResp.getAttributeNames().iterator(); iter.hasNext();) {
                            String name = (String) iter.next();
                            String value = sregResp.getParameterValue(name);

                            authContext.putProperty(name, value);
                        }
                    }
                }
                if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
                    FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);

                    // List emails = fetchResp.getAttributeValues("email");
                    // String email = (String) emails.get(0);

                    List<?> aliases = fetchResp.getAttributeAliases();
                    for (Iterator<?> iter = aliases.iterator(); iter.hasNext();) {
                        String alias = (String) iter.next();
                        List<?> values = fetchResp.getAttributeValues(alias);
                        if (values.size() > 0) {
                            authContext.putProperty(getAttributeNameForAXURI(fetchResp.getAttributeTypeUri(alias)), values.get(0));
                        }
                    }
                }


                //  setup the auth context for the session
                //
                httpReq.getSession(true).setAttribute(Constants.AUTH_CONTEXT, authContext);


                return true;

            } else {
                return false;
            }
        } catch (OpenIDException e) {
            // present error to the user
            throw new ServletException(e);
        }

    }

    private MessageExtension createOAuthRequest() {
        OAuthRequest oauthRequest = OAuthRequest.createOAuthRequest();
        oauthRequest.setScopes(scopes);
        oauthRequest.setConsumer(CONSUMER_KEY);
        return oauthRequest;
    }

    private MessageExtension createSRegRequest() {
        SRegRequest sregReq = SRegRequest.createFetchRequest();
        sregReq.addAttribute(Constants.EMAIL_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.FULLNAME_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.NICKNAME_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.DOB_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.GENDER_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.POSTCODE_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.COUNTRY_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.LANGUAGE_ATTRIBUTE_NAME, true);
        sregReq.addAttribute(Constants.TIMEZONE_ATTRIBUTE_NAME, true);
        return sregReq;
    }

    private MessageExtension createAXFetchRequest() throws MessageException {
        // Attribute Exchange example: fetching the 'email' attribute
        FetchRequest fetchRequest = FetchRequest.createFetchRequest();
        fetchRequest.addAttribute(Constants.EMAIL_ATTRIBUTE_NAME, Constants.AX_EMAIL_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.FULLNAME_ATTRIBUTE_NAME, Constants.AX_FULLNAME_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.FIRSTNAME_ATTRIBUTE_NAME, Constants.AX_FIRSTNAME_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.LASTNAME_ATTRIBUTE_NAME, Constants.AX_LASTNAME_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.DOB_ATTRIBUTE_NAME, Constants.AX_DOB_ATTRIBUTE_URI, false);
        fetchRequest.addAttribute(Constants.GENDER_ATTRIBUTE_NAME, Constants.AX_GENDER_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.POSTCODE_ATTRIBUTE_NAME, Constants.AX_POSTCODE_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.COUNTRY_ATTRIBUTE_NAME, Constants.AX_COUNTRY_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.LANGUAGE_ATTRIBUTE_NAME, Constants.AX_LANGUAGE_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.TIMEZONE_ATTRIBUTE_NAME, Constants.AX_TIMEZONE_ATTRIBUTE_URI, true);
        fetchRequest.addAttribute(Constants.NICKNAME_ATTRIBUTE_NAME, Constants.AX_NICKNAME_ATTRIBUTE_URI, true);

        return fetchRequest;

    }

    private String getAttributeNameForAXURI(String attributeTypeUri) {
        if (attributeTypeUri.equals(Constants.AX_EMAIL_ATTRIBUTE_URI)) {
            return Constants.EMAIL_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_DOB_ATTRIBUTE_URI)) {
            return Constants.DOB_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_FIRSTNAME_ATTRIBUTE_URI)) {
            return Constants.FIRSTNAME_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_FULLNAME_ATTRIBUTE_URI)) {
            return Constants.FULLNAME_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_LASTNAME_ATTRIBUTE_URI)) {
            return Constants.LASTNAME_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_COUNTRY_ATTRIBUTE_URI)) {
            return Constants.COUNTRY_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_GENDER_ATTRIBUTE_URI)) {
            return Constants.GENDER_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_LANGUAGE_ATTRIBUTE_URI)) {
            return Constants.LANGUAGE_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_POSTCODE_ATTRIBUTE_URI)) {
            return Constants.POSTCODE_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_TIMEZONE_ATTRIBUTE_URI)) {
            return Constants.TIMEZONE_ATTRIBUTE_NAME;
        }
        if (attributeTypeUri.equals(Constants.AX_NICKNAME_ATTRIBUTE_URI)) {
            return Constants.NICKNAME_ATTRIBUTE_NAME;
        }
        return "";
    }
}
