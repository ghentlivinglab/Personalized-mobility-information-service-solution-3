// Global variable to check if google api is already loaded (needed to avoid unexpected errors)
var googleLoaded = false;

/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopPOI
 * @constructor poiShowController
 */
angular.module('vopPOI').controller('poiShowController', function($scope, $http, $location, $routeParams, PointOfInterest) {
    /**
     * @constant icon
     * @type {string}
     */
    var icon = 'assets/images/map-pin-special.svg';

    /**
     * @member poi
     * @description the point of interest
     * @memberof anguler_module.vopApp.vopPOI.poiShowController
     */
	$scope.poi = PointOfInterest.get({userId: $routeParams.userId, poiId: $routeParams.poiId});

    /**
     * @function edit
     * @description redirects to the edit page of this point of interest
     * @memberof angular_module.vopApp.vopPOI.poiShowController
     */
    $scope.edit = function(){

        // Redirect to the edit page for this point of interest.
        $location.path('/user/' + $routeParams.userId + '/poi/' + $routeParams.poiId + '/edit');
    };
    /**
     * @function delete
     * @description redirects to the list page of the points of interests of the current user and deletes the poi we were on
     * @memberof angular_module.vopApp.vopPOI.poiShowController
     */
    $scope.delete = function(){

        // Delete this point of interest with an async http request using the poi services.
		$scope.poi.$delete({userId: $routeParams.userId});
        // Redirect to the list of points of interests of the user.
        $location.path('/user/' + $routeParams.userId + '/poi');
    };

    /**
     * @function goBack
     * @description redirects to the list page of the points of interests of the current user
     * @memberof angular_module.vopApp.vopPOI.poiShowController
     */
    $scope.goBack = function(){

        // Redirects to the list page of the points of interests
        $location.path('/user/' + $routeParams.userId + '/poi');
    }
});
