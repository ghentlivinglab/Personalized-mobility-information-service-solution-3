resourceServices.factory('UserEvent',function($http, HOSTNAME){
  return {
    getUserEvents: function(userId){
      return $http.get(HOSTNAME.URL+"event/?user_id=" + userId);
    }
  };
});