// Global variable to check if google api is already loaded (needed to avoid unexpected errors)
var googleLoaded = false;

/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopPOI
 * @constructor poiEditController
 */
angular.module('vopPOI').controller('poiEditController', function($scope, $http, $location, $routeParams, $timeout, PointOfInterest, EventType) {
    /**
     * @constant icon
     * @type {string}
     */
    var icon = 'assets/images/map-pin-special.svg';
    // ...
    $scope.geocoder;
    // Variable that will contain the address-data of the added point entered by the user.
    $scope.pointAutocomplete;
    // Variable that will contain the data of the marker of the point in order to display a marker on the map.
    $scope.pointMarker;
    // Variable that will contain the data of the circle with the point at it's center.
    $scope.circle;

    // Gets the resource data in an async way through the service.
    $scope.poi=PointOfInterest.get({userId: $routeParams.userId, poiId: $routeParams.poiId}, function(data){
        /**
         * @member poi
         * @description point of interest to be edited
         * @memberof angular_module.vopApp.vopPOI.poiEditController
         */
        $scope.poi = data;

        /**
         * @member eventTypes
         * @description model to hold all the event types that can possibly selected
         * @memberof angular_module.vopApp.vopPOI.poiEditController
         * @type {*|{method, isArray}}
         */
        $scope.eventTypes = EventType.query();

        /**
         * @member tempHolder
         * @description array to temporarily hold the answers of the selected events
         * @memberof angular_module.vopApp.vopPOI.poiEditController
         * @type {Array}
         */
        $scope.tempHolder = [];


        //Checks the checkboxes that should be checked once the data has been received.
        $scope.eventTypes.$promise.then(function(data2){
            var i = 0;
            angular.forEach(data2, function(event){
                for(var j = 0; j < $scope.poi.notify_for_event_types.length; j++){
                    if(event.type == $scope.poi.notify_for_event_types[j].type){
                        $scope.tempHolder[i] = event;
                    }
                }
                i++;
            });
        });
    });

    /**
     * @member selectedEventTypes
     * @description array of the selected event types, ready to be send to the API
     * @memberof angular_module.vopApp.vopPOI.poiEditController
     * @type {Array}
     */
    $scope.selectedEventTypes = [];

    /**
     * @member selectedAll
     * @description Boolean that checks if the user wishes to select all options
     * @memberof angular_module.vopApp.vopPOI.poiEditController
     * @type {Boolean}
     */
    $scope.selectedAll = false;
    /**
     * @function save
     * @description The function will save the changes made to the point of interest and redirect to the show-page of this point of interest
     * @memberof angular_module.vopApp.vopPOI.poiEditController
     */
    $scope.save = function(){
        if($scope.pointMarker==null){
            $scope.showErrorMessage("Gelieve een addres in te geven.");
            return;
        }
        $scope.geocoder.geocode({'location': $scope.pointMarker.position}, function(results, status){
            if (status === google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    $scope.$apply(function () {
                        $scope.poi.address = formatAddress(results[0], $scope.pointMarker.getPosition());
                        angular.forEach($scope.tempHolder, function(entry){
                            if(entry!=null && entry != false){
                                $scope.selectedEventTypes.push(entry);

                            }
                        });
                        $scope.poi.notify_for_event_types = $scope.selectedEventTypes;
                        $scope.poi.$edit({userId: $routeParams.userId});
                        $location.path('/user/' + $routeParams.userId + '/poi/' + $routeParams.poiId);
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
     * @description The function will redirect to the show page of the point of interest
     * @memberof angular_module.vopApp.vopPOI.poiEditController
     */
    $scope.cancel = function(){
        $location.path('/user/' + $routeParams.userId + '/poi/' + $routeParams.poiId);
    };

    /**
     * @function submit
     * @description round about way of making the submit button work in multiple browsers as it is not inside the form
     * @memberof angular_module.vopApp.vopPOI.poiEditController
     */
    $scope.submit = function(){
        //simulate the clicking of the submit button
        $timeout(function(){$("#poiEditForm").click();},0);
    };

    /**
     * @function selectAll
     * @description functions that unchecks or checks all the boxes when necessary
     * @memberof angular_module.vopApp.vopPOI.poiEditController
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
     * @memberof angular_module.vopApp.vopPOI.poiEditController
     */
    $scope.checking = function(){
        var tmp = true;
        if($scope.tempHolder.length  == $scope.eventTypes.length) {
            for (var i = 0; i < $scope.tempHolder.length; i++) {
                if ($scope.tempHolder[i] == false) {
                    tmp = false;
                }
            }
        } else {
            tmp = false;
        }
        $scope.selectedAll = tmp;
    };
});

var formatAddress = function(address, position){
  var result={};
  result.housenumber = "1";
  address.address_components.forEach(function(entry){
    if(entry.types[0]==="route" || entry.types[0]==="premise"){
      result.street = entry.long_name;
    } else if(entry.types[0]==="street_number"){
      result.housenumber = entry.long_name;
    } else if (entry.types[0]==="locality"){
      result.city = entry.long_name;
    } else if(entry.types[0]==="country"){
      result.country = entry.short_name;
    } else if(entry.types[0]==="postal_code"){
      result.postal_code = entry.long_name;
    }
  });
  result.coordinates = {
    lat: position.lat(),
    lon: position.lng()
  };
  return result;
};
