/**
 @author Frontend team
 @memberof angular_module.vopApp.vopAuth
 @constructor authenticationController
 */
angular.module('vopAuth').controller('authenticationController', function($location, $scope, Authentication, Session) {
	// obliged to declare $scope-variables????
	$scope.credentials;
	$scope.notValid = false;

	/**
     @description This function will log in the user.
     @function logIn
     @memberof angular_module.vopApp.vopAuth.authenticationController
     */
	$scope.logIn = function(){
		if(!($scope.credentials.email == null || $scope.credentials.password == null)){
			Authentication.login($scope.credentials).then(function(response){
				//succes
				$location.path('/myEvents');
			}, function(response){
				if(response.code === 401){
					$scope.notCompleteMessage = "Uw wachtwoord en/of email is verkeerd.";
				}
				$scope.notValid = true;
			});
		} else {
			$scope.notValid = true;
			$scope.notCompleteMessage = "Gelieve een geldig wachtwoord en email in te geven";
		}
	};

	/**
	 * @function alert
	 * @description closes the alert of the errormessage when activated
	 * @memberof angular_module.vopApp.vopAuth.authenticationController
	 */
	$scope.alert = function(){
		$scope.notValid = false;
	};

	/**
	 * @function forgotPass
	 * @description links to the page where you the user can resolve the forgotten password issue
	 * @memberof angular_module.vopApp.vopAuth.authenticationController
	 */
	$scope.forgotPass = function(){
		$location.path("user/forgot_password_request/");
	};
});

/**
 @author Frontend-team
 @memberof angular_module.vopApp.vopAuth
 @constructor 403Controller
 */
angular.module('vopAuth').controller('403Controller', function($location, $scope, Session) {

	/**
     @description This function will log in the user.
     @function logIn
     @memberof angular_module.vopApp.vopAuth.authenticationController
     */
	$scope.logIn = function(){
		
	}

});
