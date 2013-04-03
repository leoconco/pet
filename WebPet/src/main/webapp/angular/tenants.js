angular.module('tenantResource', ['ngResource']).
    factory('Tenant', function($resource) {
      var Tenant = $resource('/RestPet/base/tenants/:id',
         { format: 'json'}, {
             get: { method: 'GET' },
             update: { method: 'PUT' },
             query: { method: 'GET', isArray: true }
          }
      );

      Tenant.prototype.list = function(cb) {
        return Tenant.list({id: this._id.$oid},
            angular.extend({}, this, {_id:undefined}), cb);
      };

      Tenant.prototype.destroy = function(cb) {
        return Tenant.remove({id: this._id.$oid}, cb);
      };

      return Tenant;
    });
    
angular.module('tenants', ['tenantResource']);


function ListCtrl($scope, Tenant) {
    $scope.tenants = Tenant.query();
    $scope.$on('pushTenant', function (e, value) {
        $scope.tenants.push(value);
    });
}


function CreateCtrl($scope, $location, Tenant) {
    $scope.save = function() {
        Tenant.save($scope.tenant, function(tenant) {
            $scope.$emit('pushTenant', tenant);
        });
    }
}