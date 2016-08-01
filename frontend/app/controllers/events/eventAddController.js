// Global variable to check if google api is already loaded (needed to avoid unexpected errors)
var googleLoaded = false;
/**
@author Frontend team
@memberof angular_module.vopApp.vopEvent
@constructor eventAddController
*/
angular.module('vopEvent').controller('eventAddController', function($scope, $http, $location, Event, TransportationType, general, $q, $route) {

  /**
  @member jam
  @description Model which is used in the html view
  @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.jam;
  /**
  @member event
  @description Json object which represents an event and which will be posted to the server
  @property active - Boolean which says if the event is still active
  @property jams - Array with all jam objects in JSON format
  @property coordinates - The lat and lon of the event (json representation of coordinates)
  @property relevant_for_transportation_types - JSONArray of the relevant transportation types
  @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.event = new Event();
  $scope.event.active = false; // Allows set active on false. If checked on html form, this will be true
  $scope.event.jams=[];
  /**
    @member jamAddSuccess
    @description variable which says wheter a jam has been added successfully (true) or not (false)
    @default false
    @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.jamAddSuccess=false;

  /**
    @member selectedTransportationTypesTemp
    @description variable which holds temporarily the selected transportation types
    @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.selectedTransportationTypesTemp=[];
  /**
    @member transportationTypes
    @description array of strings with all the possible transportation types
    @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.transportationTypes = TransportationType.query();
  
  

	/**
	  @function fillEventObject
	  @private
	  @description function to make an event ready to send to the API
	  @memberof angular_module.vopApp.vopEvent.eventAddController
	*/
	var fillEventObject = function(){
	  if($scope.jam && $scope.markers<2 && $scope.event.jams.length==0){
		$scope.showErrorMessage("Gelieve een begin- en eindpunt van een file aan te duiden.");
	  } else {
		$scope.event.coordinates={};
		$scope.event.coordinates.lat = $scope.addressAutocomplete.getPlace().geometry.location.lat();
		$scope.event.coordinates.lon = $scope.addressAutocomplete.getPlace().geometry.location.lng();
		$scope.event.relevant_for_transportation_types = [];
		$scope.selectedTransportationTypesTemp.forEach(function(t){
		  $scope.event.relevant_for_transportation_types.push(t);
		});
	  }
	};
  
  /**
    @member defer
    @description promise
    @memberof angular_module.vopApp.vopEvent.eventAddController
    @private
  */
  var defer = $q.defer();
  /**
    @function createEvent
    @description Send an event to the API
    @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.createEvent = function() {
    defer.promise.then(function(){
      fillEventObject();
      $scope.addJam();
    }).then(function(){
      // Post to the server
      $scope.event.$save(function() {
        $location.path('/event'); // Redirect to event page
        $route.reload();
      });
    });
    defer.resolve();
  };
  
  /**
  @function goBack
  @description Go back to the eventList page.
  @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.goBack = function(){
    $location.path('/event');
  };
  
  /**
    @member eventtypes
    @description array of strings with all the event types
    @memberof angular_module.vopApp.vopEvent.eventAddController
  */
  $scope.eventtypes = general.EventType().query(function(){});
});
