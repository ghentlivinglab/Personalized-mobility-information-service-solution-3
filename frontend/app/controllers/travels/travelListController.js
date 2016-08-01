/**
* @author Frontend team
* @module travelListController
* @description shows list travels.
* @memberof angular_module.vopApp.vopTravel
*/
angular.module('vopTravel').controller('travelListController', function ($scope, $http, $routeParams, $location, Route, Travel) {
  Travel.query({userId: $routeParams.userId},function(data){
    /**
    @member travelList
    @description The list of the travels
    @memberof angular_module.vopApp.vopTravel.travelListController
    */
    $scope.travelList = data;

    var counter = 0;
    angular.forEach(data,function(travel){

      var i = 0;
      $scope.travelList[counter].week = [];
      var weekdays=["maandag","dinsdag","woensdag","donderdag","vrijdag","zaterdag","zondag"];
      angular.forEach(travel.recurring, function (recurring){
        if(recurring){
          $scope.travelList[counter].week.push(weekdays[i]);
        }
        i++;
      });

      //If is_arrival_time == true than the user had entered an arrival time else it is a departure time.
      if(travel.is_arrival_time){
        $scope.travelList[counter].is_arrival_time = "aankomsttijd";
      } else {
        $scope.travelList[counter].is_arrival_time = "vertrektijd";
      }

      $scope.travelList[counter].hasRoutes = false;

      //Data routes
      //Strange construction because of async call to fetch the data!
      //Not all route data is returned in order!
      Route.query({userId: $routeParams.userId, travelId: $scope.travelList[counter].id},
        function(answer){
          if(answer.length != 0){
            var travelId = answer[0].links[0].href.split("/")[7];
            for(var i = 0; i < $scope.travelList.length; i++){
              if(travelId == $scope.travelList[i].id){
                $scope.travelList[i].hasRoutes = true;
                $scope.travelList[i].routes = answer;
              }
            }
          }
        }
      );
      counter++;
    });

  });
  /**
  @function travelDetails
  @description go to the travelShow page
  @memberof angular_module.vopApp.vopTravel.travelListController
  */
  $scope.travelDetails = function(travelId){
    var path = $location.path();
    $location.path(path + travelId);
  };

  /**
  @description The function will redirect to the infopage of the selected route.
  @function goto
  @memberof angular_module.vopApp.vopTravel.travelListController
  @param travelId: the id of the travel.
  @param routeId: the id of the route to which we want to go.
  */
  $scope.goto = function(travelId, routeId){
    var path = $location.path();
    $location.path(path + travelId + "/route/" + routeId);
  };

  /**
  @description The function will redirect to the edit-page of the selected route.
  @function editRoute
  @memberof angular_module.vopApp.vopTravel.travelListController
  @param travelId: the id of the travel.
  @param routeId: the id of the route we want to edit.
  */
  $scope.editRoute = function(travelId, routeId){
    var path = $location.path();
    $location.path(path + travelId + "/route/" + routeId + "/edit");
  };

  // /**
  // @description The function will delete the selected route.
  // @function deleteRoute
  // @memberof angular_module.vopApp.vopTravel.travelListController
  // @param travelId: the id of the travel.
  // @param routeId: the id of the route we want to delete.
  // */
  // $scope.deleteRoute = function(travelId, routeId) {
  //   var path = $location.path();
  //   $http.delete("https://vopro7.ugent.be/api" + path + travelId + "/route/" + routeId);
  //   $location.path(path);
  // };

  /**
  @description The function will redirect to the add-page of routes.
  @function addRoute
  @memberof angular_module.vopApp.vopTravel.travelListController
  @param travelId: the id of the travel.
  */
  $scope.addRoute = function(travelId){
    var path = $location.path();
    $location.path(path + travelId + "/route/add");
  };

  /**
  @description The function will redirect to the edit-page of the selected travel.
  @function editTravel
  @memberof angular_module.vopApp.vopTravel.travelListController
  @param travelId: the id of the travel we want to edit.
  */
  $scope.editTravel = function(travelId){
    var path = $location.path();
    $location.path(path + travelId + "/edit");
  };

  /**
  @description The function will redirect to the add-page of travels.
  @function addTravel
  @memberof angular_module.vopApp.vopTravel.travelListController
  */
  $scope.addTravel = function(){
    var path = $location.path();
    $location.path(path + "add");
  };

  // /**
  // @description The function will delete the selected travel.
  // @function deleteTravel
  // @memberof angular_module.vopApp.vopTravel.travelListController
  // @param travelId: the id of the travel we want to delete.
  // */
  // $scope.deleteTravel = function(travelId){
  //   var path = $location.path();
  //   $http.delete("https://vopro7.ugent.be/api" + path + travelId);
  //   $location.path(path);
  // };
});
