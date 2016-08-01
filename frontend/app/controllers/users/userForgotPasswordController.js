/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userForgotPasswordController
 */
angular.module('vopUser').controller('userForgotPasswordController', function($scope, $http, $routeParams, $location, UserForgotPassword) {

    /**
     * @member userForgotPassword
     * @description Model containing the email of the user who forgot his/her password
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordController
     */
    $scope.userForgotPassword = new UserForgotPassword();
    $scope.userForgotPassword.email = "";

    /**
     * @member notCompleteMessage
     * @description contains the error message of what the user should do in order to successfully registrate himself
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordController
     * @type {string}
     */
    $scope.notCompleteMessage = "";

    $scope.successMessage = "";

    /**
     * @member notValid
     * @description model that contains if the user has completed all the correct steps
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordController
     * @type {boolean}
     */
    $scope.notValid = false;

    $scope.valid = false;

    /**
     * @function addUserFunction
     * @description checks if (most of) the fields are correct and if so tries to add the user to the database.
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordController
     */
    $scope.save = function(){
        if(!(/^[^@]+@[^@.]+\.[^@.]+$/.test($scope.userForgotPassword.email))){
            showError("Gelieve een geldig email op te geven.");
            return -3;
        } else {
            $scope.userForgotPassword.$save(function(response){
                showSuccess("Uw verzoek is verwerkt, gelieve uw mail te bekijken voor de heractiveringscode.")
            });

            return 0;
        }

    };

    /**
     * @function showError
     * @description helper function that goes back to the top of the page and shows an error message with the appropriate text
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordController
     * @param msg - error message that should be displayed
     */
    var showError = function(msg){
        $scope.notCompleteMessage = msg;
        $scope.valid = false;
        $scope.notValid = true;
        $(document).scrollTop(0);
    };

    var showSuccess = function(msg){
        $scope.successMessage = msg;
        $scope.notValid = false;
         $scope.valid = true;
        $(document).scrollTop(0);
    }

    /**
     * @function alert
     * @description closes the alert of the errormessage when activated
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordController
     */
    $scope.alert = function(){
        $scope.notValid = false;
    };
});
