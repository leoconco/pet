/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapple.security;

import java.io.IOException;
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
import org.openid4java.consumer.ConsumerManager;

/**
 *
 * @author leonardo.contreras
 */
public class AuthenticationFilter implements Filter {

    private FilterConfig filterConfig;
    private String pathToIgnore;
    private String loginPage;
    private String openIdSelector;
    private String openIdParam;
    private ConsumerManager manager;

    @Override
    public void init(FilterConfig pFilterConfig) throws ServletException {
        filterConfig = pFilterConfig;
        pathToIgnore = filterConfig.getInitParameter("public_url");
        loginPage = filterConfig.getInitParameter("login_page");
        openIdSelector = filterConfig.getInitParameter("openid_select");
        openIdParam = filterConfig.getInitParameter("openid_url_param");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getServletPath();

        if (path.startsWith(pathToIgnore)) {
            chain.doFilter(request, response);
        } else {

            HttpSession httpSession = httpRequest.getSession(false);
            if (httpSession == null) {
                if (path.startsWith(openIdSelector)) {
                    String urlOpenid = httpRequest.getParameter(openIdParam);
                    // TODO: Discover
                }

            } else {
            }
            RequestDispatcher dispatcher = filterConfig.getServletContext().getRequestDispatcher(loginPage);
            dispatcher.forward(request, response);

        }
    }

    @Override
    public void destroy() {
    }
}
