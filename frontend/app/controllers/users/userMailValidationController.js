/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userMailValidationController
 */
angular.module('vopUser').controller('userMailValidationController', function($scope, $routeParams, $http, $location, UserMailValidation) {

    /**
     * @member notValid
     * @description model that contains if an error message should be shown or not.
     * @memberof angular_module.vopApp.vopUser.userMailValidationController
     * @type {boolean}
     */
    $scope.notValid = false;

    /**
     * @member notValidSuccess
     * @description model that contains if a success message should be shown or not.
     * @memberof angular_module.vopApp.vopUser.userMailValidationController
     * @type {boolean}
     */
    $scope.notValidSuccess = false;

    /**
     * @member notCompleteMessage
     * @description contains the error message of what went wrong/right.
     * @memberof angular_module.vopApp.vopUser.userMailValidationController
     * @type {string}
     */
    $scope.notCompleteMessage = "";

    /**
     * @function showError
     * @description helper function that goes back to the top of the page and shows an error message with the appropriate text
     * @memberof angular_module.vopApp.vopUser.userMailValidationController
     * @param msg - error message that should be displayed
     */
    var showError = function(msg){
        $scope.notCompleteMessage = msg;
        $scope.notValid = true;
        $(document).scrollTop(0);
    };

    /**
     * @function showError
     * @description helper function that goes back to the top of the page and shows an error message with the appropriate text
     * @memberof angular_module.vopApp.vopUser.userMailValidationController
     * @param msg - error message that should be displayed
     */
    var showSuccess = function(msg){
        $scope.notCompleteMessage = msg;
        $scope.notValidSuccess = true;
        $(document).scrollTop(0);
    };

    /**
     * @function alert
     * @description closes the alert of the errormessage when activated
     * @memberof angular_module.vopApp.vopUser.userMailValidationController
     */
    $scope.alert = function(){
        $scope.notValid = false;
    };

    /**
     * @function alertSuccess
     * @description closes the alert of the success message when activated
     * @memberof angular_module.vopApp.vopUser.userMailValidationController
     */
    $scope.alertSuccess = function(){
        $scope.notValidSuccess = false;
    };

    var body = {
        "email" : $routeParams.mail,
        "email_verification_pin" : $routeParams.code
    };

    UserMailValidation.VerifyMail(body).then(function successCallback() {
        // this callback will be called asynchronously
        // when the response is available
        showSuccess("Uw mail is succesvol gevalideerd.");
    }, function errorCallback(response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        if(response.status === 405){
            showError("De gebruikte link is vervallen of is al eens gebruikt.");
        }
    });
});
