var resourceServices = angular.module('vopResources',['ngResource']);

resourceServices.config(['$httpProvider', function($httpProvider) {
  $httpProvider.interceptors.push('authInterceptor');
}]);