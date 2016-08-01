resourceServices.factory('Travel', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
  return $resource(HOSTNAME.URL+'user/:userId/travel/:travelId',{ travelId: '@id' }, {
    'edit': { method: 'PUT' }
  });
}]);