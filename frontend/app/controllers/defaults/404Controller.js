/**
 @author Frontend team
 @memberof angular_module.vopApp
 @constructor 404Controller
 */
angular.module('vopApp').controller('404Controller', function($scope, $location, Session) {
    var nxt_url;

	if(Session.isOpen()){
		$scope.name_nxt = "mijn meldingen";
		nxt_url = '/myEvents';
	} else {
		$scope.name_nxt = "de homepagina";
		nxt_url = '/index';
	}

    /**
     @description The function will redirect to the index page if no user is logged in.
     Otherwise it will redirect to the profile of the logged in user.
     @function goHome
     @memberof angular_module.vopApp.404Controller
     */
    $scope.goHome = function(){
        $location.path(nxt_url);
    };

});
