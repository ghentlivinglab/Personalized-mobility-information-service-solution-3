resourceServices.factory('UserRoles', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
    return $resource(HOSTNAME.URL+'role/');
}]);