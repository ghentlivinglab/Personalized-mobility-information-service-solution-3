resourceServices.factory('TransportationType', ['$resource','HOSTNAME', function($resource, HOSTNAME) {
  return $resource(HOSTNAME.URL+'transportationtype/',{});
}]);