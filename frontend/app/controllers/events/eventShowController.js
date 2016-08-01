var googleLoaded = false;
/**
@author Frontend team
@memberof angular_module.vopApp.vopEvent
@constructor eventShowController
*/
angular.module('vopEvent').controller('eventShowController', function($scope, $http, $routeParams, $interval, Event, $q, $location) {
	/**
	@function back
	@description Go back to the event list.
	@memberof angular_module.vopApp.vopEvent.eventShowController
	*/
	$scope.back=function(){
		$location.path("/event/")
	};
	
	/**
	@function editEvent
	@description Go to the edit event page
	@memberof angular_module.vopApp.vopEvent.eventShowController
	*/
	$scope.editEvent=function(){
		$location.path("/event/"+$routeParams.id+"/edit/");
	};
	/**
  @function initialize
  @description initialize the page
  @memberof angular_module.vopApp.vopEvent.eventShowController
  */
	// GET request naar server voor data op te vragen
	/**
	@member event
	@description The event filled with data from the server
	@memberof angular_module.vopApp.vopEvent.eventShowController
	*/
	$scope.event = Event.get({ eventId : $routeParams.id });
});
