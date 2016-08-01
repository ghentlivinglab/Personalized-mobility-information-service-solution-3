resourceServices.factory('EventType', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
  return $resource(HOSTNAME.URL+'eventtype/',{});
}]);