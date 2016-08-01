/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopRoute
 * @constructor routeAddController
 */
angular.module('vopRoute').controller('routeAddController', function ($scope, $http, $routeParams, $location, EventType, TransportationType, $timeout, $rootScope) {

    /**
     * @member route
     * @description route to be added
     * @memberof angular_module.vopApp.vopRoute.routeAddController
     */
    $scope.route = {};

    /**
     * @member entries
     * @description model to hold all the event types that can possibly selected
     * @memberof angular_module.vopApp.vopRoute.routeAddController
     * @type {*|{method, isArray}}
     */
    $scope.entries = EventType.query();
    
});
