/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopPOI
 * @constructor poiListController
 */
angular.module('vopPOI').controller('poiListController', function($scope, $http, $location, $route, $routeParams, PointOfInterest) {
    /**
     * @member pois
     * @description Lis of points of interests
     * @memberof angular_module.vopApp.vopPOI.poiListController
     */
    $scope.pois = PointOfInterest.query({userId: $routeParams.userId});

    var showOk = true;

    /**
     * @function goto
     * @description redirects to the details page of the selected point of interest
     * @memberof angular_module.vopApp.vopPOI.poiListController
     * @param poiId - The id of the point of interest we wish to know more about
     */
    $scope.goto = function(poiId){
        if(showOk){

            // Redirect to the show page of the point of interest
            $location.path('/user/' + $routeParams.userId + '/poi/' + poiId);
        }
    };

    /**
     * @function edit
     * @description redirects to the edit page of the selected point of interest
     * @memberof angular_module.vopApp.vopPOI.poiListController
     * @param poiId - the id of the point of interest the user wishes to edit
     */
    $scope.edit = function(poiId){
        showOk = false;

        // Redirect to the edit page of the point of interest
        $location.path('/user/' + $routeParams.userId + '/poi/' + poiId + '/edit');
    };

    /**
     * @function delete
     * @description deletes the point of interest and redirects to the user's point of interests lists
     * @memberof angular_module.vopApp.vopPOI.poiListController
     * @param poi - point of interest the user wishes to delete
     */
    $scope.delete = function(poi){
        showOk = false;

        // Sends a delete http request to the server.
		poi.$delete({userId:$routeParams.userId}).then(function successCallback() {
            //reload the page when succesfull.
            $route.reload();
        }, function errorCallback() {
            //TODO generate error message here for the user.
            //For now try reloading the page and see if that doesn't fix the problem?
            $route.reload();
        });



    };

    /**
     * @function addNew
     * @description redirects to the add page for point of interests where new points of interests can be added
     * @memberof angular_module.vopApp.vopPOI.poiListController
     */
    $scope.addNew = function(){

        // Redirects to the add page for point of interests
        $location.path('/user/' + $routeParams.userId + '/poi/add');
    }
});
