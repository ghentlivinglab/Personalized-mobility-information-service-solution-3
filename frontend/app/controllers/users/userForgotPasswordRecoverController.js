/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userForgotPasswordRecoverController
 */
angular.module('vopUser').controller('userForgotPasswordRecoverController', function($scope, $http, $routeParams, $location, UserRecover, Authentication, Session) {

    /**
     * @member notValid
     * @description model that contains if an error message should be shown or not.
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordRecoverController
     * @type {boolean}
     */
    $scope.notValid = false;

    /**
     * @member notCompleteMessage
     * @description contains the error message of what went wrong.
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordRecoverController
     * @type {string}
     */
    $scope.notCompleteMessage = "";

    /**
     * @function recover
     * @description sends a recovery request to the server in order to reinstate a new password.
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordRecoverController
     */
    $scope.recover = function () {
        if($scope.password === $scope.confirmPassword){
            var body = {
                "password" : $scope.password,
                "email" : $routeParams.mail
            };
            UserRecover.getUserRecover($routeParams.code, body).then(function successCallback(response) {
                // this callback will be called asynchronously
                // when the response is available
                var credentials = {email: $routeParams.mail, password: $scope.password};
                Authentication.login(credentials, response.id).then(function(){
                    //Redirect to the profile page of the freshly created user.
                    $location.path(Session.getUserUrl() + '/event');
                });
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                if(response.status === 401){
                    showError("Iets is fout gegaan tijdens de verwerking van uw verzoek.");
                }
            });
        } else {
            showError("Gelieve, 2 identieke wachtwoorden op te geven.");
        }
    };

    /**
     * @function showError
     * @description helper function that goes back to the top of the page and shows an error message with the appropriate text
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordRecoverController
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
     * @memberof angular_module.vopApp.vopUser.userForgotPasswordRecoverController
     */
    $scope.alert = function(){
        $scope.notValid = false;
    };
});
