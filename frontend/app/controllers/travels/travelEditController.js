/**
* @author Frontend team
* @module routeEditController
* @description Controller related to editing routes.
* @memberof angular_module.vopApp.vopTravel
*/
angular.module('vopTravel').controller('travelEditController', function ($scope, $http, $routeParams, $location, Travel) {
  Travel.get({userId: $routeParams.userId, travelId: $routeParams.travelId},function(data) {
    /**
  	@member travel
  	@description travel to edit
  	@memberof angular_module.vopApp.vopTravel.travelEditController
  	*/
    $scope.travel = data;
    $scope.travel.time=[];
    // Make new dates. Only the time is important, so use a dummy date (because it's needed for the controller of date).
    $scope.travel.time[0]=new Date("January 1, 1970 "+$scope.travel.time_interval[0]);
    $scope.travel.time[1]=new Date("January 1, 1970 "+$scope.travel.time_interval[1]);

  });

  /**
  @description The function will save the changes made to the travel.
  @function editTravel
  @memberof angular_module.vopApp.vopTravel.travelEditController
  */
  $scope.editTravel = function () {
    $scope.travel.time_interval[0]=formatTime($scope.travel.time[0]);
    $scope.travel.time_interval[1]=formatTime($scope.travel.time[1]);
    //Save changes.
    $scope.travel.$edit({userId: $routeParams.userId});
    //Go back to the route list.
    $location.path("/user/" + $routeParams.userId + "/travel/" + $routeParams.travelId);
  };

  /**
   @description The function will cancel all made changes and revert to the show-page of the travel.
   @function cancel
   @memberof angular_module.vopApp.vopTravel.travelEditController
   */
  $scope.cancel=function(){
    $location.path("/user/" + $routeParams.userId + "/travel/" + $routeParams.travelId);
  };

  /**
   * @function selectAllDays
   * @description functions that unchecks or checks all the boxes when necessary
   * @memberof angular_module.vopApp.vopTravel.travelEditController
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
   * @memberof angular_module.vopApp.vopTravel.travelEditController
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



/**
* @function formatTime
* @description Converts the hours and minutes of a javascript date to a string (formatted HH:mm).
* @param date - a javascript date.
*/
var formatTime = function(date) {
  var hours,minutes;
  if (date.getMinutes() < 10) {
    minutes = "0" + date.getMinutes().toString();
  } else {
    minutes = date.getMinutes().toString();
  }
  if (date.getHours() < 10) {
    hours = "0" + date.getHours().toString();
  } else {
    hours = date.getHours().toString();
  }
  return hours + ":" + minutes;
};
