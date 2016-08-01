/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userListController
 */

angular.module('vopUser').controller('userListController', function($scope, $http, $route, $location, User) {

	/**
	 * @member users
	 * @description model containing all the users
	 * @memberof angular_module.vopApp.vopUser.userListController
	 */
	$scope.users = User.query();

	/**
	 * @function deleteUserFunction
	 * @description deletes the selected user
	 * @memberof angular_module.vopApp.vopUser.userListController
	 * @param userToBeDeleted - selected user that will be deleted
     */
	$scope.deleteUserFunction = function(userToBeDeleted){
		$(document).ready(function () {
			$("#overlappingForm").fadeIn();
			$("#userList").css('transition', '1s');
			$("#userList").css('-webkit-filter', 'blur(5px)');
		});
		$scope.selectedForRemoval = userToBeDeleted;
	};

	$scope.yes = function(){
		$scope.selectedForRemoval.$delete(function(){
			//Reloads the user page after a succesfull deletion.
			$route.reload();
		});
	};
	$scope.no = function(){
		$(document).ready(function () {
			$("#overlappingForm").fadeOut();
			$("#userList").css('transition', '1s');
			$("#userList").css('-webkit-filter', 'blur(0px)');
		});
	};
});
