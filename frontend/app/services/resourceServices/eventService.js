resourceServices.factory('Event', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
  return $resource(HOSTNAME.URL+'event/:eventId',{ eventId: '@id'}, {
    'edit': { method: 'PUT' }
  });
}]);