// Global variable to check if google api is already loaded (needed to avoid unexpected errors)
var googleLoaded = false;

/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopPOI
 * @constructor poiAddController
 */
angular.module('vopPOI').controller('poiAddController', function ($scope, $http, $location, $routeParams, $timeout, PointOfInterest, EventType) {

    /**
     * @member poi
     * @description point of interest to be added
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     */
    $scope.poi = new PointOfInterest();

    /**
     * @member poi.active
     * @description point of interest to be added, particularly pertaining to the active field of the poi-object setting this to a default false value
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     * @type {boolean}
     */
    $scope.poi.active = true;

    /**
     * @member poi.notify_for_event_types
     * @description point of interest to be added, particularly pertaining to the event types of the poi-object. This array contains all the events for which the user wishes to be notified for this particular poi
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     * @type {array}
     */
    $scope.poi.notify_for_event_types = [];

    /**
     * @member poi.notify
     * @description point of interest to be added, particularly pertaining to the notify field of the poi-object. This field contains all the data on which devices the user wishes to be notified
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     * @type {{email: boolean, cell_number: boolean}}
     */
    $scope.poi.notify = {email: false, cell_number: false};

    /**
     * @member eventTypes
     * @description model to hold all the event types that can possibly selected
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     * @type {*|{method, isArray}}
     */
    $scope.eventTypes = EventType.query();

    /**
     * @member tempHolder
     * @description array to temporarily hold the answers of the selected events
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     * @type {Array}
     */
    $scope.tempHolder = [];

    /**
     * @member selectedEventTypes
     * @description array of the selected event types, ready to be send to the API
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     * @type {Array}
     */
    $scope.selectedEventTypes = [];

    /**
     * @function save
     * @description The function will save the created point of interest and redirect to the show-page of the all points of interests
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     */
    $scope.save = function () {
        if ($scope.pointMarker == null) {
            $scope.showErrorMessage("Gelieve een punt van interesse op te geven.");
            return;
        }
        $scope.geocoder.geocode({'location': $scope.pointMarker.position}, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    $scope.$apply(function () {
                        $scope.poi.address = formatAddress(results[0], $scope.pointMarker.getPosition());
                        angular.forEach($scope.tempHolder, function (entry) {
                            if (entry != null && entry != false) {
                                $scope.selectedEventTypes.push(entry);
                            }
                        });
                        $scope.poi.notify_for_event_types = $scope.selectedEventTypes;
                        $scope.poi.$save({userId: $routeParams.userId}, function (response) {
                            $location.path('/user/' + $routeParams.userId + '/poi/');
                        });
                    });
                } else {
                    window.alert('No results found');
                }
            } else {
                window.alert('Geocoder failed due to: ' + status);
            }
        });

    };

    /**
     * @function cancel
     * @description The function will redirect to the list page of the all the points of interests and terminate the creation of a new point of interest
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     */
    $scope.cancel = function () {
        $location.path('/user/' + $routeParams.userId + '/poi');
    };

    /**
     * @function submit
     * @description round about way of making the submit button work in multiple browsers as it is not inside the form
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     */
    $scope.submit = function () {
        //simulate the clicking of the submit button
        $timeout(function () {
            $("#poiAddForm").click();
        }, 0);
    };

    /**
     * @function selectAll
     * @description functions that unchecks or checks all the boxes when necessary
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     */
    $scope.selectAll = function(){
        if($scope.selectedAll){
            var k = 0;
            angular.forEach($scope.eventTypes, function(type){
                $scope.tempHolder[k] = type;
                k++;
            });
        } else {
            for(var i = 0; i < $scope.eventTypes.length; i++){
                $scope.tempHolder[i] = false;
            }
        }
    };

    /**
     * @function checking
     * @description functions that unchecks or checks the selected all option when necessary
     * @memberof angular_module.vopApp.vopPOI.poiAddController
     */
    $scope.checking = function(){
        var tmp = true;
        if($scope.tempHolder.length == $scope.eventTypes.length){
            for(var i = 0; i < $scope.tempHolder.length; i++){
                if($scope.tempHolder[i] == false){
                    tmp = false;
                }
            }
        } else {
            tmp = false;
        }
        $scope.selectedAll = tmp;
    };
});