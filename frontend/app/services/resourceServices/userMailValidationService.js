resourceServices.factory('UserMailValidation',function($http, HOSTNAME){
    return {
        VerifyMail: function(body){
            return $http.post(HOSTNAME.URL+"user/verify" , body);
        }
    };
});