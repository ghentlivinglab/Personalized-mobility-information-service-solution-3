/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userChangePassController
 */
angular.module('vopUser').controller('userChangePassController', function($scope, $http, $routeParams, $location, User, UserPassword) {

    /**
     * @member user
     * @description user to be edited
     * @memberof angular_module.vopApp.vopUser.userChangePassController
     */
    $scope.user = User.get({ userId : $routeParams.id });

    /**
     * @member userPassword
     * @description Model containing old and new password
     * @memberof angular_module.vopApp.vopUser.userChangePassController
     */
    $scope.userPassword = new UserPassword();
    $scope.userPassword.old_password = "";
    $scope.userPassword.new_password = "";

    /**
     * @member notCompleteMessage
     * @description contains the error message of what the user should do in order to successfully registrate himself
     * @memberof angular_module.vopApp.vopUser.userChangePassController
     * @type {string}
     */
    $scope.notCompleteMessage = "";

    /**
     * @member notValid
     * @description model that contains if the user has completed all the correct steps
     * @memberof angular_module.vopApp.vopUser.userChangePassController
     * @type {boolean}
     */
    $scope.notValid = false;

    /**
     * @function addUserFunction
     * @description checks if (most of) the fields are correct and if so tries to add the user to the database.
     * @memberof angular_module.vopApp.vopUser.userChangePassController
     */
    $scope.save = function(){

        //var credentials = {email: $scope.user.email, password: $scope.userPassword.old_password};

        //Check if entered passwords are the same.
        if($scope.userPassword.new_password != $scope.confirmPassword){
            //The user's passwords did not match.
            showError("Gelieve 2 identieke wachtwoorden in te geven.");
            return -1;
        }
        $scope.userPassword.$save({userId: $routeParams.id}, function (response) {
            $location.path('/user/' + $routeParams.id);
        }, function(errorResponse){
            showError("Uw oude wachtwoord is niet correct.");
            return -1;
        });

        return 0;
    };

    /**
     * @function showError
     * @description helper function that goes back to the top of the page and shows an error message with the appropriate text
     * @memberof angular_module.vopApp.vopUser.userChangePassController
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
     * @memberof angular_module.vopApp.vopUser.userChangePassController
     */
    $scope.alert = function(){
        $scope.notValid = false;
    };

    /**
     * @function goBack
     * @description cancels the edit operation and returns to the profile page of the user
     * @memberof angular_module.vopApp.vopUser.userChangePassController
     */
    $scope.goBack = function(){
        //Redirect to the user's profile page.
        $location.path('/user/' + $routeParams.id);
    };
});
