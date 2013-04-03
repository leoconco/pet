<jsp:include page="/header.jsp">
    <jsp:param name="ngapp" value="tenants"/>
    <jsp:param name="ngcontroller" value="ListCtrl"/>
</jsp:include>
<script type="text/javascript" src="angular/tenants.js"></script>
<div class="container-fluid">
    <div class="row-fluid">
        <a href="#addTenant" class="btn pull-right" role="button" data-toggle="modal" title="Add tenant">
            <i class="icon-plus"></i>
        </a>
    </div>
    <div class="row-fluid">
        <ul class="thumbnails">
            <li ng-repeat="tenant in tenants" class="span2">
                <a href="home.jsp?_select_tenant={{tenant.id}}">
                    <div class="thumbnail">
                        <img src="public/img/tenant.png"/>  
                        <h3>{{tenant.name}}</h3>
                        <p>{{tenant.description}}</p>
                    </div>
                </a>
            </li>            
        </ul>
    </div>
</div>
<div id="addTenant" class="modal hide fade">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3>Add new Tenant</h3>
  </div>
  <div class="modal-body">
      <form></form>
  </div>
  <div class="modal-footer">
    <a href="#" class="btn btn-primary">Save changes</a>
  </div>
</div>
<jsp:include page="/footer.jsp"/>