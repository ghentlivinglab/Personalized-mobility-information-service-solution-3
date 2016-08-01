/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userAddController
 */
angular.module('vopUser').controller('userAddController', function($scope, $http, $location, User, Authentication, Session) {

    /**
     * @member user
     * @description user to be added
     * @memberof angular_module.vopApp.vopUser.userAddController
     */
    $scope.user = new User();

    /**
     * @member user.mute_notifications
     * @description field of the user that defines if the user wishes to receive notifications
     * @memberof angular_module.vopApp.vopUser.userAddController
     * @type {boolean}
     */
    $scope.user.mute_notifications = false;

    /**
     * @member notChecked
     * @description checks if the user of the website agrees to our terms of agreement (else he can't make an account)
     * @memberof angular_module.vopApp.vopUser.userAddController
     * @type {boolean}
     */
    $scope.notChecked = false;

    /**
     * @member notCompleteMessage
     * @description contains the error message of what the user should do in order to successfully registrate himself
     * @memberof angular_module.vopApp.vopUser.userAddController
     * @type {string}
     */
    $scope.notCompleteMessage = "";

    /**
     * @member notValid
     * @description model that contains if the user has completed all the correct steps
     * @memberof angular_module.vopApp.vopUser.userAddController
     * @type {boolean}
     */
    $scope.notValid = false;

    $scope.user.cell_number = "";

    /**
     * @function addUserFunction
     * @description checks if (most of) the fields are correct and if so tries to add the user to the database.
     * @memberof angular_module.vopApp.vopUser.userAddController
     */
    $scope.addUserFunction = function(){

        var credentials = {email: $scope.user.email, password: $scope.user.password};

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

        //Check if entered passwords are the same.
        if($scope.user.password != $scope.confirmPassword){
            //The user's passwords did not match.
            showError("Gelieve 2 identieke wachtwoorden in te geven.");
            return -4;
        }

        //Did the user agree to our terms of services?
        if($scope.notChecked){
            //Send post request to the server.
            $scope.user.$save(function (response) {
                //no hi when response is error message :D
                Authentication.login(credentials, response.id).then(function(){
                    //Redirect to the profile page of the freshly created user.
                    $location.path(Session.getUserUrl() + '/travel/add');
                }, function(error){
                    //NEVER GETS HERE NO CLUE AS TO WHY =< ===> IT COMES HERE WHEN THE REGISTRATION SUCCEEDED BUT THE LOG-IN FAILED
                    showError("Iets is verkeerd gegaan tijdens het automatisch inloggen met uw profiel. Gelieve later proberen in te loggen met uw nieuw profiel.");
                });

            }, function(errorResponse){
                if(errorResponse.status === 409){
                    showError("Dit email-account is al in gebruik of is niet geldig.");
                } else {
                    showError("Iets is verkeerd gegaan tijdens de registratie van uw account. De server kon niet bereikt worden. Gelieve uw internetverbinding nog eens na te kijken.");
                }
            });
        } else {
            //Did not agree to the terms of service.
            showError("Zonder u akkoord te verklaren met onze terms of service, kunnen wij u onze diensten niet aanbieden.");
            return -5;
        }

        return 0;
    };

    /**
     * @function showError
     * @description helper function that goes back to the top of the page and shows an error message with the appropriate text
     * @memberof angular_module.vopApp.vopUser.userAddController
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
     * @memberof angular_module.vopApp.vopUser.userAddController
     */
    $scope.alert = function(){
        $scope.notValid = false;
    };
});
