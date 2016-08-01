// Global variable to check if google api is already loaded (needed to avoid unexpected errors)
var googleLoaded = false;

/**
 * @author Frontend team
 * @module travelAddController
 * @description Controllers related to adding travels.
 * @memberof angular_module.vopApp.vopTravel
 */
angular.module('vopTravel').controller('travelAddController', function ($scope, $http, $routeParams, $location, $rootScope, $q, Travel, Route, $timeout, EventType, TransportationType) {
    /**
     * @member eventTypes
     * @description model to hold all the event types that can possibly selected
     * @memberof angular_module.vopApp.vopTravel.travelAddController
     * @type {*|{method, isArray}}
     */
    $scope.eventTypes = EventType.query();

    $scope.routes=[];
    /**
     @member travel
     @description travel to add
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.travel = new Travel();
    /**
     @member startEndPointValidation
     @description model to hold whether the origin and destination were given
     @memberof angular_module.vopApp.vopTravel.travelAddController
     @default false
     */
    $scope.startEndPointValidation = false;
	
	$scope.autocompleteDisabled = false;
    $scope.travel.time_interval = [];
    /**
     @member email
     @description model to hold whether email was selected for the routes
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.email = false;
    /**
     @member cellphone
     @description model to hold whether cellphone was selected for the routes
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.cellphone = false;
    /**
     @member activeRoute
     @description model to hold whether the route must be active or not
     @memberof angular_module.vopApp.vopTravel.travelAddController
     @default true
     */
    $scope.activeRoute = true;
    /**
     @member selectedEventTypesTemp
     @description array to hold temporarily the selected event types
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.selectedEventTypesTemp = [];
    /**
     @member selectedEventTypes
     @description array of the selected event types, ready to be send to the API
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.selectedEventTypes = [];
    /**
     @member routeAddSuccess
     @description model to hold whether the addition of the route was successfull or not
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.routeAddSuccess = false;
    /**
     @member recurring
     @description array of booleans for each day
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.travel.recurring = [false, false, false, false, false, false, false];

    /**
     @function cancel
     @description cancel the travelAdd and redirect to the list page
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.cancel = function () {
        $location.path('/user/' + $routeParams.userId + "/travel/");
    };

    /**
     * @member transportationTypes
     * @description model to hold all the transportation types that can possibly selected
     * @memberof angular_module.vopApp.vopTravel.travelAddController
     * @type {*|{method, isArray}}
     */
    $scope.transportationTypes = TransportationType.query(function (response) {
        $timeout(function () {
            $scope.transportationModel = response[0];
            // document.getElementById("mode").value = response[0];
        }, 1000)
    });
	
	/**
     @function saveRoute
     @description save the route
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.saveRoute = function () {
        var waypoints = [];
        $scope.markers.forEach(function (entry) {
            if (entry.getMap() != null && !(entry == $scope.originMarker || entry == $scope.destinationMarker)) {
                waypoints.push({
                    lat: entry.position.lat(),
                    lon: entry.position.lng()
                });
            }
        });
        angular.forEach($scope.selectedEventTypesTemp, function (entry) {
            if (entry != null && entry != false) {
                $scope.selectedEventTypes.push({"type": entry});
            }
        });
        var route = new Route();
        route.waypoints = waypoints;
        route.transportation_type = $scope.transportationModel;
        route.notify_for_event_types = $scope.selectedEventTypes;
        route.notify = {
            email: $scope.email,
            cell_number: $scope.cellphone
        };
        route.interpolated_waypoints = $scope.overview_path;
        route.active = true;
        $scope.routes.push(route);
        $(document).ready(function () {
            $("#overlappingForm").fadeOut();
            $("#travels").css('transition', '1s');
            $("#travels").css('-webkit-filter', 'blur(0px)');
        });
        $scope.routeAddSuccess = true;
        $scope.selectedEventTypesTemp = [];
        $scope.selectedEventTypes = [];
        $scope.email = false;
        $scope.cellphone = false;
        $scope.originMarker.draggable = false;
        $scope.destinationMarker.draggable = false;
		$scope.autocompleteDisabled = true;
        $scope.selectedAll = false;
    };
	
    /**
     @description The function will create a new Travel.
     @function createTravel
     @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.createTravel = function () {
        //promise
        // var defer = $q.defer();
        // $scope.travel.date=formatDate($scope.travel.date);

        if ($scope.originMarker == null || $scope.destinationMarker == null) {
            $scope.startEndPointValidation = true;
            return;
        }
		
		var deferred = $q.defer();
        $scope.geocoder.geocode({'location': $scope.originMarker.position}, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    $scope.$apply(function () {
                        // You have to do all the work in the apply (you can't call external function, because otherwise, google will not always have loaded everything.) NEED TO FIX THIS, BUT IT WORKS FOR NOW!!!!!!
                        $scope.travel.startpoint = {};
                        $scope.travel.startpoint = formatAddress(results[0], $scope.originMarker.getPosition());
                    });
					deferred.resolve();
                } else {
					deferred.reject('No results found');
                }
            } else {
				deferred.reject('Geocoder failed due to: ' + status);
            }
        });
        $scope.geocoder.geocode({'location': $scope.destinationMarker.position}, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                $scope.travel.endpoint = {};
                if (results[0]) {
                    $scope.$apply(function () {
                        // $scope.travel.endpoint = formatAddress(results[0], destinationMarker.getPosition());
                        // You have to do all the work in the apply (you can't call external function, because otherwise, google will not always have loaded everything.) NEED TO FIX THIS, BUT IT WORKS FOR NOW!!!!!!
                        $scope.travel.endpoint = {};
                        $scope.travel.endpoint.coordinates = {};
                        $scope.travel.endpoint = formatAddress(results[0], $scope.destinationMarker.getPosition());
                        $scope.travel.time_interval[0] = formatTime($scope.time[0]);
                        $scope.travel.time_interval[1] = formatTime($scope.time[1]);
						deferred.promise.then(function() {
							$scope.travel.$save({userId: $routeParams.userId}, function (trav) {
								if ($scope.routes.length == 0) {
									$scope.saveRoute();
								}
								angular.forEach($scope.routes, function (route) {
									route.$save({userId: $routeParams.userId, travelId: trav.id});
								});
								$location.path('/user/' + $routeParams.userId + "/travel/");
							});
						});
                    });

                } else {
                    window.alert('No results found');
                }
            } else {
                window.alert('Geocoder failed due to: NOT OK');
            }
        });
    };

    /**
     * @function selectAll
     * @description functions that unchecks or checks all the boxes when necessary
     * @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.selectAll = function(){
        if($scope.selectedAll){
            var k = 0;
            angular.forEach($scope.eventTypes, function(type){
                $scope.selectedEventTypesTemp[k] = type.type;
                k++;
            });
        } else {
            for(var i = 0; i < $scope.eventTypes.length; i++){
                $scope.selectedEventTypesTemp[i] = false;
            }
        }
    };

    /**
     * @function checking
     * @description functions that unchecks or checks the selected all option when necessary
     * @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.checking = function(){
        var tmp = true;
        if($scope.selectedEventTypesTemp.length == $scope.eventTypes.length){
            for(var i = 0; i < $scope.selectedEventTypesTemp.length; i++){
                if($scope.selectedEventTypesTemp[i] == false){
                    tmp = false;
                }
            }
        } else {
            tmp = false;
        }
        $scope.selectedAll = tmp;
    };

    /**
     * @function selectAllDays
     * @description functions that unchecks or checks all the boxes when necessary
     * @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.selectAllDays = function(){
        if($scope.selectedAllDays){
            for(var k = 0; k < $scope.travel.recurring.length; k++){
                $scope.travel.recurring[k] = true;
            }
        } else {
            for(var i = 0; i < $scope.travel.recurring.length; i++){
                $scope.travel.recurring[i] = false;
            }
        }
    };

    /**
     * @function checking
     * @description functions that unchecks or checks the selected all option when necessary
     * @memberof angular_module.vopApp.vopTravel.travelAddController
     */
    $scope.checkingDays = function(){
        var tmp = true;
        for(var i = 0; i < $scope.travel.recurring.length; i++){
            if($scope.travel.recurring[i] == false){
                tmp = false;
            }
        }
        $scope.selectedAllDays = tmp;
    };
});