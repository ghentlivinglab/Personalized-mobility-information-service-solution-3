resourceServices.factory('User', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
  return $resource(HOSTNAME.URL+'user/:userId',{ userId: '@id'}, {
    'edit': { method: 'PUT' }
  });
}]);