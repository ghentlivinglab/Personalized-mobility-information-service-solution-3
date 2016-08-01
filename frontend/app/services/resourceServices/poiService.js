resourceServices.factory('PointOfInterest', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
  return $resource(HOSTNAME.URL+'user/:userId/point_of_interest/:poiId',{ poiId: '@id'}, {
    'edit': { method: 'PUT' }
  });
}]);