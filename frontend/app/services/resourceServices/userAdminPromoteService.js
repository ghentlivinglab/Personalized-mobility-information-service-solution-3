resourceServices.factory('UserAdminPromote', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
    return $resource(HOSTNAME.URL+'admin/permission/:userId',{ userId: '@id'}, {
        'save': { method: 'POST' }
    });
}]);