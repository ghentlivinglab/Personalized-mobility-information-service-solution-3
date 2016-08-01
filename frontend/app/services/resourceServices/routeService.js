resourceServices.factory('Route', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
  return $resource(HOSTNAME.URL+'user/:userId/travel/:travelId/route/:routeId', { userId: '@userId', travelId: '@travelId', routeId: '@routeId'}, {
    'edit': { method: 'PUT' }
  });
}]);
