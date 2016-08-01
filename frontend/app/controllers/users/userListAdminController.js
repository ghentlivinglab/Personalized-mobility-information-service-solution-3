/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userListAdminController
 */

angular.module('vopUser').controller('userListAdminController', function($scope, $http, $route, $location, User, UserAdmin, UserRoles, HOSTNAME) {

    /**
     * @member users
     * @description model containing all the users (with roles but we can't delete because of the called upon service)
     * @memberof angular_module.vopApp.vopUser.userListAdminController
     */
    $scope.users = UserAdmin.query();

    /**
     * @member users
     * @description model containing all the users (without roles but with these we can delete because of the called upon service)
     * @memberof angular_module.vopApp.vopUser.userListAdminController
     */
    $scope.users2 = User.query();

    UserRoles.query().$promise.then(
        //success
        function( value ){
            $scope.roles = value;
            $scope.selectedRole = $scope.roles[value.length - 1];
        },

        //error
        function( error ){
            /*Do something with error*/
        }
    );

    /**
     * @function deleteUserFunction
     * @description deletes the selected user
     * @memberof angular_module.vopApp.vopUser.userListAdminController
     * @param userToBeDeletedId - selected user's id that will be deleted
     */
    $scope.deleteUserFunction = function(userToBeDeletedId){
        angular.forEach($scope.users2, function(user){
            if(user.id == userToBeDeletedId){
                $scope.selectedForRemoval = user;
            }
        });
        $(document).ready(function () {
            $("#overlappingForm").fadeIn();
            $("#userList").css('transition', '1s');
            $("#userList").css('-webkit-filter', 'blur(5px)');
        });

    };

    /**
     * @function deleteUser
     * @description deletes the selected user
     * @memberof angular_module.vopApp.vopUser.userListAdminController
     */
    $scope.deleteUser = function(){
        $scope.selectedForRemoval.$delete(function(){
            //Reloads the user page after a succesfull deletion.
            $route.reload();
        });
    };

    /**
     * @function deleteNo
     * @description cancels the action of delete
     * @memberof angular_module.vopApp.vopUser.userListAdminController
     */
    $scope.deleteNo = function(){
        $(document).ready(function () {
            $("#overlappingForm").fadeOut();
            $("#userList").css('transition', '1s');
            $("#userList").css('-webkit-filter', 'blur(0px)');
        });
    };

    $scope.changeRole = function(){
        $http({
            url: HOSTNAME.URL+'admin/permission/' + $scope.selectedForChange.id,
            method: 'POST',
            data: '\"' + $scope.selectedRole +'\"'
        }).then(function(response){
            $route.reload();
        });
    };

    $scope.noChangeRole = function(){
        $(document).ready(function () {
            $("#changeRole").fadeOut();
            $("#userList").css('transition', '1s');
            $("#userList").css('-webkit-filter', 'blur(0px)');
        });
    };

    $scope.selectForRoleChange = function(user){
        $scope.selectedForChange = user;
        $(document).ready(function () {
            $("#changeRole").fadeIn();
            $("#userList").css('transition', '1s');
            $("#userList").css('-webkit-filter', 'blur(5px)');
        });
    };
});
