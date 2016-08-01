/**
 @author Frontend team
 @memberof angular_module.vopApp.vopEvent
 @constructor angular_module.vopApp.vopEvent.eventListController
 */
angular.module('vopEvent').controller('eventListController', function ($scope, $http, $location, Event) {

    /**
     @description array of string with the event types
     @member events
     @memberof angular_module.vopApp.vopEvent.eventListController
     */
    $scope.events = Event.query(function(){
        $scope.loading = false;
    });


    $scope.loading = true;

    /**
     @description The function will redirect to the details page of the event
     @function goto
     @memberof angular_module.vopApp.vopEvent.eventListController
     @param event - The event where to go to
     */
    $scope.goto = function (event) {
        $location.path('/event/' + event.id);

    };
    /**
     @description The function will redirect to the add new event page
     @function addNewEvent
     @memberof angular_module.vopApp.vopEvent.eventListController
     */
    $scope.addNewEvent = function () {
        $location.path('/event/add');
    };
    /**
     @description delete the given event from the server
     @function deleteEvent
     @memberof angular_module.vopApp.vopEvent.eventListController
     @param event - The event to be deleted
     */
    $scope.deleteEvent = function (event) {
        event.$delete(function () {
            $location.path('/event/');
        });
    };

    $scope.sortType = 'publication_time';
    $scope.sortReverse = true;
    $scope.searchEvent='';


});
