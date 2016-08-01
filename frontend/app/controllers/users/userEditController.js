/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userEditController
 */
angular.module('vopUser').controller('userEditController', function($scope, $http, $routeParams, $location, User) {

	/**
	 * @member user
	 * @description user to be edited
	 * @memberof angular_module.vopApp.vopUser.userEditController
	 */
	$scope.user = User.get({ userId : $routeParams.id });
	/**
	 * @member notValid
	 * @description holds a boolean pertaining if the two entered passwords are the same
	 * @memberof angular_module.vopApp.vopUser.userEditController
	 * @type {boolean}
     */
	$scope.notValid = false;

	/**
	 * @function editUserFunction
	 * @description sends the made changes of the user to the server to be saved and redirects back to the user's profile page
	 * @memberof angular_module.vopApp.vopUser.userEditController
 	 **/
	$scope.editUserFunction = function() {
		//Check if user's name is valid (Because of the nature of names being an unwieldy problem we simply check if the field doesn't contain invalid characters)
		if(!(/^[^+#,*"<>;=[\]:?/|'%\/\\$¨^0-9()§!{}°_£€²³~]+$/.test($scope.user.first_name) && /^[^+#,*"<>;=[\]:?/|'%\/\\$¨^0-9()§!{}°_£€²³~]+$/.test($scope.user.last_name))){
			showError("De volgende tekens mogen niet in een voor- of achternaam voorkomen: ^ + # , * \" < > ; = [ ] : ? / | ' % / \\ $ ¨ ^ 0 1 2 3 4 5 6 7 8 9 ( ) § ! { } ° _ £ € ² ³ ~ ");
			return -1
		}

		//Check if valid cellphone number.
		if($scope.user.cell_number.replace(/ +/g, "").length > 0){
			//Cellphone number in international european notation.
			var internationalNotation = /^\+[0-9]{11}$/.test($scope.user.cell_number);
			//Belgian number internal notation.
			var nationNotation = /^[0-9]{10}$/.test($scope.user.cell_number);
			//Cellphone number in international global notation.
			var globalNotation = /^[0-9]{15}]/.test($scope.user.cell_number);
			if(!(internationalNotation || nationNotation || globalNotation)){
				showError("Gelieve een geldig gsm nummer op te geven.");
				return -2;
			}
		}

		if(!(/^[^@]+@[^@.]+\.[^@.]+$/.test($scope.user.email))){
			showError("Gelieve een geldig email op te geven.");
			return -3;
		}

		//TODO add verify answer server
		//Send edit request.
		$scope.user.$edit();
		//Redirect to the user's profile page.
		$location.path('/user/' + $routeParams.id);

	};

	/**
	 * @function showError
	 * @description helper function that goes back to the top of the page and shows an error message with the appropriate text
	 * @memberof angular_module.vopApp.vopUser.userEditController
	 * @param msg - error message that should be displayed
	 */
	var showError = function(msg){
		$scope.notCompleteMessage = msg;
		$scope.notValid = true;
		$(document).scrollTop(0);
	};

	/**
	 * @function alert
	 * @description closes the alert of the errormessage when activated
	 * @memberof angular_module.vopApp.vopUser.userEditController
	 */
	$scope.alert = function(){
		$scope.notValid = false;
	};

	/**
	 * @function goBack
	 * @description cancels the edit operation and returns to the profile page of the user
	 * @memberof angular_module.vopApp.vopUser.userEditController
	 */
	$scope.goBack = function(){
		//Redirect to the user's profile page.
		$location.path('/user/' + $routeParams.id);
	}
});
