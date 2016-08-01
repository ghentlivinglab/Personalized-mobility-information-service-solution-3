resourceServices.factory('UserPassword', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
    return $resource(HOSTNAME.URL+'user/:userId/modify_password',{ userId: '@id'}, {
        'save': { method: 'POST' }
    });
}]);