var googleLoaded = false;
/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userEventsShowController
 */
angular.module('vopEvent').controller('userEventsShowController', function ($scope, $http, $routeParams, UserEvent, $compile, $q) {

    /**
     * @TODO ask help here for here!
     * @param start_node
     * @param end_node
     */
    // $scope.jamRender = function (start_node, end_node) {
    //     markers.forEach(function (m) {
    //         m.setMap(null);
    //     });
    //     var jamMarkerStart = new google.maps.Marker({
    //         position: {
    //             lat: start_node.lat,
    //             lng: start_node.lon
    //         },
    //         icon: MARKERICON,
    //         draggable: false,
    //         animation: google.maps.Animation.DROP,
    //         map: $scope.map
    //     });
    //     var jamMarkerEnd = new google.maps.Marker({
    //         position: {
    //             lat: end_node.lat,
    //             lng: end_node.lon
    //         },
    //         icon: MARKERICON,
    //         draggable: false,
    //         animation: google.maps.Animation.DROP,
    //         map: $scope.map
    //     });
    //     bounds.extend(jamMarkerStart.getPosition());
    //     bounds.extend(jamMarkerEnd.getPosition());
    //
    //     var request = {
    //         origin: jamMarkerStart.getPosition(),
    //         destination: jamMarkerEnd.getPosition(),
    //         optimizeWaypoints: true,
    //         travelMode: google.maps.DirectionsTravelMode.DRIVING,
    //     };
    //     directionsService.route(request, function (response, status) {
    //         if (status == google.maps.DirectionsStatus.OK) {
    //             directionsDisplay.setDirections(response);
    //         }
    //     });
    //     markers.push(jamMarkerStart);
    //     markers.push(jamMarkerEnd);
    //     $scope.map.fitBounds(bounds);
    // };

	/**
	 *    @member userEvents
	 *    @description List of all the events which are relevant for the user
	 *    @memberof angular_module.vopApp.vopUser.userEventsShowController
	 */
	$scope.promise=UserEvent.getUserEvents($routeParams.userId).then(function(response) {
		$scope.userEvents = response.data;
	});


    /**
     * @member clickedEvent
     * @description This variable represents the event that is clicked.
     * @memberof angular_module.vopApp.vopUser.userEventsShowController
     */
    $scope.clickedEvent = {};
});
