resourceServices.factory('UserForgotPassword', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
    return $resource(HOSTNAME.URL+'/user/forgot_password_request/', {
        'save': { method: 'POST' }
    });
}]);