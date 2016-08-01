var googleLoaded = false;
/**
 @author Frontend team
 @memberof angular_module.vopApp.vopEvent
 @constructor eventEditController
 */
angular.module('vopEvent').controller('eventEditController', function ($scope, $http, $location, Event, $routeParams, EventType, TransportationType) {
    /**
     @member transportationTypes
     @description array of strings with all the possible transportation types
     @memberof angular_module.vopApp.vopEvent.eventEditController
     */
    $scope.transportationTypes = TransportationType.query(function () {
        /**
         @member event
         @description The event
         @memberof angular_module.vopApp.vopEvent.eventEditController
         */
        $scope.event = new Event();
        $scope.event = Event.get({eventId: $routeParams.eventId}, function (data) {
			$scope.event = data;
            if ($scope.event.jams != null) {
                $scope.event.jams.forEach(function (j) {
                    j.show = true;
                });
            }
            $scope.event.relevant_for_transportation_types.forEach(function (t) {
                for (i = 0; i < $scope.transportationTypes.length; i++) {
                    if ($scope.transportationTypes[i] == t) {
                        $scope.selectedEventTypesTemp[i] = t;
                    }
                }
            });
        });
    });
    /**
     @member eventtypes
     @description array of strings with all the event types
     @memberof angular_module.vopApp.vopEvent.eventEditController
     */
    $scope.eventTypes = EventType.query();
    /**
     @member selectedEventTypesTemp
     @description variable which holds temporarily the selected event types
     @memberof angular_module.vopApp.vopEvent.eventEditController
     */
    $scope.selectedEventTypesTemp = [];

    /**
     @function editEvent
     @description edit the event and save it to the API
     @memberof angular_module.vopApp.vopEvent.eventEditController
     */
    $scope.editEvent = function () {
        $scope.event.relevant_for_transportation_types = [];
        $scope.selectedEventTypesTemp.forEach(function (type) {
            if (type != false) {
                $scope.event.relevant_for_transportation_types.push(type);
            }
        });
        var newJams = [];
        $scope.deletedJams.forEach(function (deleted) {
            for (i = 0; i < $scope.event.jams.length; i++) {
                if ($scope.event.jams[i] == deleted) {
                    delete $scope.event.jams[i];
                }
            }
        });
        if ($scope.event.jams != null) {
            $scope.event.jams.forEach(function (jj) {
                if (jj != undefined) {
                    newJams.push(jj);
                }
            });
        }
        $scope.event.jams = newJams;
        // If the address is changed, get the new coordinates
        if ($scope.addressAutocomplete.getPlace() != null) {
            $scope.event.coordinates = {
                lat: $scope.addressAutocomplete.getPlace().geometry.location.lat(),
                lon: $scope.addressAutocomplete.getPlace().geometry.location.lng()
            };
        }
        // else, do nothing (use the same coordinates as before)
        $scope.event.$edit({eventId : $routeParams.eventId});
        $scope.goBack();
    };
    /**
     @function goBack
     @description go back to the event list
     @memberof angular_module.vopApp.vopEvent.eventEditController
     */
    $scope.goBack = function () {
        $location.path("/event/" + $routeParams.eventId);
    };
});
