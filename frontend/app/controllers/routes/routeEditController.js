// Global variable to check if google api is already loaded (needed to avoid unexpected errors)
var googleLoaded = false;
/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopRoute
 * @constructor routeEditController
 */
angular.module('vopRoute').controller('routeEditController', function ($scope, $http, $routeParams, $location, Route, TransportationType, EventType, Travel, $timeout) {

    /**
     * @function goBack
     * @description Redirects the user to his page with all his travels
     * @memberof angular_module.vopApp.vopRoute.routeEditController
     */
    $scope.goBack = function () {
        $location.path("/user/" + $routeParams.userId + "/travel/" + $routeParams.travelId);
    };

    //Get data through service
    $scope.route = Route.get({
        userId: $routeParams.userId,
        travelId: $routeParams.travelId,
        routeId: $routeParams.routeId
    }, function (data) {
        /**
         @member route
         @description route to edit
         @memberof angular_module.vopApp.vopRoute.routeEditController
         */
        $scope.route = data;
    });

    //Get data through service
    $scope.travel = Travel.get({userId: $routeParams.userId, travelId: $routeParams.travelId}, function (data) {
        /**
         @member travel
         @description travel to edit
         @memberof angular_module.vopApp.vopRoute.routeEditController
         */
        $scope.travel = data;
		$scope.selectedEventTypesTemp = [];
		// Hacky oplossing, maar ze werkt
		// scope.transportationTypes = TransportationType.query(function(response){
		// 	$timeout(function(){
		// 		document.getElementById("mode").value=response[0];
		// 		scope.renderRoute();
		// 	}, 1000);
		// });
		$scope.eventTypes = EventType.query(function () {
			$scope.route.notify_for_event_types.forEach(function (t) {
				for (i = 0; i < $scope.eventTypes.length; i++) {
					if ($scope.eventTypes[i].type == t.type) {
						$scope.selectedEventTypesTemp[i] = t.type;
					}
				}
			});
		});
    });

    /**
     * @member transportationTypes
     * @description model to hold all the transportation types that can possibly selected
     * @memberof angular_module.vopApp.vopRoute.routeEditController
     * @type {*|{method, isArray}}
     */
    $scope.transportationTypes = TransportationType.query(function (response) {
        $timeout(function () {
            $scope.transportationModel = response[0];
            // document.getElementById("mode").value = response[0];
        }, 1000)
    });
	
	/**
	 @description The function will update the route with the changed values
	 @function editRoute
	 @memberof angular_module.vopApp.vopRoute.routeEditController
	 */
	$scope.editRoute = function () {
		//Empty it for good measurement.
		$scope.route.notify_for_event_types = [];
		$scope.route.waypoints = [];
		$scope.route.transportation_type = $scope.transportationModel; //TODO: check if this is actually defined
		$scope.markers.forEach(function (m) {
			$scope.route.waypoints.push({
				lat: m.position.lat(),
				lon: m.position.lng()
			});
		});
		$scope.selectedEventTypesTemp.forEach(function (type) {
			if(type!=false) {
				$scope.route.notify_for_event_types.push({
					type: type
				});
			}
		});
		$scope.route.interpolated_waypoints = $scope.overview_path;
		//Save changes.
		$scope.route.$edit({userId: $routeParams.userId, travelId: $routeParams.travelId, routeId: $scope.route.id});
		//Go back to the route list.

		$timeout(function(){
			$location.path("/user/" + $routeParams.userId + "/travel/" + $routeParams.travelId);
		},100);

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
});
