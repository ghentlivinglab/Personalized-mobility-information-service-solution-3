var googleLoaded = false;
/**
 * @author Frontend team
 * @module travelListController
 * @description shows specific travels.
 * @memberof angular_module.vopApp.vopTravel
 */
angular.module('vopTravel').controller('travelShowController', function ($scope, $routeParams, $location, $route, Route, Travel, TransportationType, $timeout) {
    /**
     @member email
     @description model to tell if you are noticed by email
     @memberof angular_module.vopApp.vopTravel.travelShowController
     */
    $scope.email = false;
    /**
     @member cellphone
     @description model to tell if you are noticed by sms
     @memberof angular_module.vopApp.vopTravel.travelShowController
     */
    $scope.cellphone = false;
    /**
     @member selectedEventTypesTemp
     @description array to hold the event types temporarily
     @memberof angular_module.vopApp.vopTravel.travelShowController
     */
    $scope.selectedEventTypesTemp = [];
    /**
     @member selectedEventTypes
     @description array to hold the event types ready to send to the API
     @memberof angular_module.vopApp.vopTravel.travelShowController
     */
    $scope.selectedEventTypes = [];
    /**
     @member editTravel
     @description array to hold the event types ready to send to the API
     @memberof angular_module.vopApp.vopTravel.travelShowController
     @param travel - the travel to be edited
     */
    $scope.editTravel = function (travel) {
        $location.path("user/" + $routeParams.userId + "/travel/" + $routeParams.travelId + "/edit/")
    };
    /**
     @function back
     @description go back to the travel list page
     @memberof angular_module.vopApp.vopTravel.travelShowController
     */
    $scope.back = function () {
        $location.path("user/" + $routeParams.userId + "/travel/")
    };

    /**
     * @member transportationTypes
     * @description model to hold all the transportation types that can possibly selected
     * @memberof angular_module.vopApp.vopTravel.travelShowController
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
     @memberof angular_module.vopApp.vopTravel.travelShowController
     */
    $scope.saveRoute = function () {
        var waypoints = [];
        $scope.routeMarkers.forEach(function (entry) {
            if (entry.getMap() != null) {
                waypoints.push({
                    lat: entry.position.lat(),
                    lon: entry.position.lng()
                });
            }
        });
        angular.forEach($scope.selectedEventTypesTemp, function (entry) {
            if (entry != null) {
                $scope.selectedEventTypes.push({"type": entry});
            }
        });
        var newRoute = new Route();
        newRoute.waypoints = waypoints;
        newRoute.notify_for_event_types = $scope.selectedEventTypes;
        newRoute.notify = {email: $scope.email, cell_number: $scope.cellphone};
        newRoute.active = true;
        newRoute.interpolated_waypoints = $scope.overview_path;
		newRoute.transportation_type = $scope.transportationModel;
        newRoute.$save({userId: $routeParams.userId, travelId: $routeParams.travelId});

        $(document).ready(function () {
            $("#routeAddDiv").animate({left: "-100%"});
            $("#travelDetailDiv").css('transition', '1s');
            $("#travelDetailDiv").css('-webkit-filter', 'blur(0px)');
        });
        $scope.routeMarkers.forEach(function (m) {
            m.setMap(null);
        });
        $scope.routeMarkers = [];
        $scope.email = false;
        $scope.cellphone = false;
        $scope.selectedEventTypesTemp = [];
        $scope.selectedEventTypes = [];
        $scope.originMarker.setMap($scope.map);
        $scope.destinationMarker.setMap($scope.map);
        setTimeout(function(){
            $route.reload();
        }, 100);

    };
    
	Route.query({userId: $routeParams.userId, travelId: $routeParams.travelId}, function (data) {
		$scope.routes = [];
		var counter = 0;
		angular.forEach(data, function (entry) {
			$scope.routes.push({
				id: counter,
				route: entry
			});
			counter++;
		});
	});
	$scope.travel=Travel.get({userId: $routeParams.userId, travelId: $routeParams.travelId}, function (data) {
		$scope.travel = data;

		var i = 0;
		$scope.week = [];
		var weekdays = ["maandag", "dinsdag", "woensdag", "donderdag", "vrijdag", "zaterdag", "zondag"];
		angular.forEach($scope.travel.recurring, function (recurring) {
			if (recurring) {
				$scope.week.push(weekdays[i]);
			}
			i++;
		});
		
	});
    
    /**
     @description The function will redirect to the page of the selected route.
     @function goto
     @memberof angular_module.vopApp.vopTravel.travelShowController
     @param routeId - the id of the route to which we want to go.
     */
    $scope.goto = function (routeId) {
        var path = $location.path();
        $location.path(path + "/route/" + routeId);
    };

    /**
     @description The function will redirect to the edit-page of the selected route.
     @function editRoute
     @memberof angular_module.vopApp.vopTravel.travelShowController
     @param routeId - the id of the route we want to edit.
     */
    $scope.editRoute = function (routeId) {
        $location.path("/user/" + $routeParams.userId + "/travel/" + $routeParams.travelId + "/route/" + routeId + "/edit");
    };

    /**
     @description The function will delete the selected route.
     @function deleteRoute
     @memberof angular_module.vopApp.vopTravel.travelShowController
     @param route - the route we want to delete.
     */
    $scope.deleteRoute = function (route) {
        route.$delete({userId: $routeParams.userId, travelId: $routeParams.travelId, routeId: route.id});
        $route.reload();
    };

    /**
     @description The function will delete this travel.
     @function deleteTravel
     @memberof angular_module.vopApp.vopTravel.travelShowController
     @param travel - the travel we want to delete.
     */
    $scope.deleteTravel = function (travel) {
        travel.$delete({userId: $routeParams.userId});
        $location.path("/user/" + $routeParams.userId + "/travel/");
    };


});
