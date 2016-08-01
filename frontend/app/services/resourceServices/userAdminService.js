resourceServices.factory('UserAdmin', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
    return $resource(HOSTNAME.URL+'/admin/user');
}]);
