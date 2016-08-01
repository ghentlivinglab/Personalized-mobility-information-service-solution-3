resourceServices.factory('UserRecover',function($http, HOSTNAME){
    return {
        getUserRecover: function(code, body){
            return $http.post(HOSTNAME.URL+"user/forgot_password?password_token=" + code , body);
        }
    };
});