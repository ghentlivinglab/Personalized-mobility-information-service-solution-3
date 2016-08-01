/**
  @class angular_module.myApp
  @description the angular app
*/
angular.module('vopApp',
    ["ngRoute",
    'ngMessages',
    'pascalprecht.translate',
    'vopUser',
    'vopPOI',
    'vopTravel',
    'vopRoute',
    'vopEvent',
    'vopAuth',
    'vopResources',
    'vopRabbit']
)
.constant("HOSTNAME", {"URL": "https://vopro7.ugent.be/api/"})
.constant("RABBITMQ", {
    "BROKER_URL": "wss://vopro7.ugent.be:443/rabbitmq/ws",
    "VHOST": "vopro",
    "RESSOURCE_URL": "/exchange/events_vopro7_xchg/user",
    "USERNAME": "vopro_user_web",
    "PWD": "user_web"
});

angular.module('vopUser', []);
angular.module('vopPOI', []);
angular.module('vopTravel', []);
angular.module('vopRoute', []);
angular.module('vopEvent', []);
angular.module('vopAuth', ['ngCookies']);
angular.module('vopRabbit', []);


/**
 @function config
 @description configure all the routes
 @param $routeProvider - this parameter will redirect to the right views
 **/
angular.module('vopApp').config(function ($routeProvider) {
    $routeProvider
    // route to root page
        .when('/', {
            // redirect to index page
            redirectTo: '/index'
        })

        // route to the index page
        .when('/index', {
            // specify controller for this route
            controller: 'indexController',
            // specify partial view for this route
            templateUrl: 'app/views/defaults/index.html',
            resolve: {
                //check if there is no user logged in
                notLoggedIn: function (Authorization) {
                    return Authorization.notLoggedInCheck();
                }
            }

        })

        // route to the contact page
        .when('/contact', {
            // specify controller for this route
            controller: 'contactController',
            // specify partial view for this route
            templateUrl: 'app/views/defaults/contact.html'
        })

        // route to the error page
        .when('/404', {
            // specify controller for this route
            controller: '404Controller',
            // specify partial view for this route
            templateUrl: 'app/views/defaults/404.html'
        })


        // route to for unauthorized access
        .when('/403', {
            // specify controller for this route
            controller: '404Controller',
            // specify partial view for this route
            templateUrl: 'app/views/defaults/403.html',
            resolve: {
                refreshAccess: function(Session){
                    // 403 means that the user's access-token is not valid for what he was trying to do
                    return Session.refreshAccessToken();
                }
            }
        })

        // route to the login-page
        .when('/login', {
            // specify controller for this route
            controller: 'authenticationController',
            // specify partial view for this route
            templateUrl: 'app/views/defaults/login.html',
            resolve: {
                //check if there is no user logged in
                notLoggedIn: function (Authorization) {
                    return Authorization.notLoggedInCheck();
                }
            }
        })

        // route to use to log out
        .when('/logout', {
            redirectTo: '/index',
            resolve: {
                //check if there is a user logged in
                loggedIn: function (Authorization, Authentication) {
                    return Authorization.loggedInCheck().then(function () {
                        Authentication.logout();
                    });
                }
            }
        })

        /*********************
         *   PSUEDO-ROUTES   *
         *********************/
        // Psuedo-route for profile used in the side-bar
        .when('/profile', {
            resolve: {
                // check if there is a user logged in
                loggedIn: function (Authorization, Session, $q, $location) {
                    return Authorization.loggedInCheck().then(function () {
                        $location.path(Session.getUserUrl());
                    });
                }
            }
        })
        // Psuedo-route for travels used in the side-bar
        .when('/myTravels', {
            resolve: {
                // check if there is a user logged in
                loggedIn: function (Authorization, Session, $q, $location) {
                    return Authorization.loggedInCheck().then(function () {
                        $location.path(Session.getUserUrl() + '/travel');
                    });
                }
            }
        })
        // Psuedo-route for poi used in the side-bar
        .when('/myPOIs', {
            resolve: {
                // check if there is a user logged in
                loggedIn: function (Authorization, Session, $q, $location) {
                    return Authorization.loggedInCheck().then(function () {
                        $location.path(Session.getUserUrl() + '/poi');
                    });
                }
            }
        })
        // Psuedo-route for events used in redirections and in the side-bar
        .when('/myEvents', {
            resolve: {
                // check if there is a user logged in
                loggedIn: function(Authorization, Session, $q, $location){
                    return Authorization.loggedInCheck().then(function () {
                        $location.path(Session.getUserUrl() + '/event');
                    });
                }
            }
        })

        // default route is the error page
        .otherwise({
            redirectTo: '/404'
        });
});

/**
 @function config module user
 @description configure all the routes
 @param $routeProvider - this parameter will redirect to the right views
 **/
angular.module('vopUser').config(function ($routeProvider) {
    $routeProvider

        // route to all users (operator only)
        .when('/user/', {
            controller: 'userListController',
            templateUrl: 'app/views/users/userList.html',
            resolve: {
                // check permissions of user
                permission: function (Roles, Authorization) {
                    return Authorization.permissionCheck([Roles.getOperator()]);
                }
            }
        })
        // route to all users (admin only)
        .when('/userAdmin/', {
            controller: 'userListAdminController',
            templateUrl: 'app/views/users/userListAdmin.html',
            resolve: {
                // check permissions of user
                permission: function (Roles, Authorization) {
                    return Authorization.permissionCheck([Roles.getAdmin()]);
                }
            }
        })
        // route that links to the forgotten password page
        .when('/user/forgot_password_request/', {
            controller: 'userForgotPasswordController',
            templateUrl: 'app/views/users/userForgotPassword.html',
            resolve: {
                //check if there is no user logged in
                notLoggedIn: function (Authorization) {
                    return Authorization.notLoggedInCheck();
                }
            }
        })
        // route that links to the recover forgotten password page
        .when('/forgot_password/:mail/:code', {
            controller: 'userForgotPasswordRecoverController',
            templateUrl: 'app/views/users/userForgotPasswordRecover.html',
            resolve: {
                //check if there is no user logged in
                notLoggedIn: function (Authorization) {
                    return Authorization.notLoggedInCheck();
                }
            }
        })
        // route that links to the validate email page
        .when('/validation/:mail/:code', {
            controller: 'userMailValidationController',
            templateUrl: 'app/views/users/userMailValidation.html'
        })
        // route to add a user
        .when('/user/add', {
            controller: 'userAddController',
            templateUrl: 'app/views/users/userAdd.html',
            resolve: {
                //check if there is no user logged in
                notLoggedIn: function (Authorization) {
                    return Authorization.notLoggedInCheck();
                }
            }
        })
        // route to edit a user
        .when('/user/:id/edit', {
            controller: 'userEditController',
            templateUrl: 'app/views/users/userEdit.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id);
                }
            }
        })
        // route to change a user's password
        .when('/user/:id/change_pass', {
            controller: 'userChangePassController',
            templateUrl: 'app/views/users/userChangePass.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) {
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id);
                }
            }
        })
        // route to fetch specific user information
        .when('/user/:id', {
            controller: 'userShowController',
            templateUrl: 'app/views/users/userShow.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id);
                }
            }
        })

});


/**
 @function config module POI
 @description configure all the routes
 @param $routeProvider - this parameter will redirect to the right views
 **/
angular.module('vopPOI').config(function ($routeProvider) {
    $routeProvider
    //route to all POIs
        .when('/user/:userId/poi/', {
            // specific controller for this
            controller: 'poiListController',
            // specific partial view for this
            templateUrl: 'app/views/pois/poiList.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id);
                }
            }
        })
        // route to add a POI
        .when('/user/:userId/poi/add', {
            // specific controller for this
            controller: 'poiAddController',
            // specific partial view for this
            templateUrl: 'app/views/pois/poiAdd.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id);
                }
            }
        })
        // route to the specific details of a poi
        .when('/user/:userId/poi/:poiId', {
            // specific controller for this
            controller: 'poiShowController',
            // specific partial view for this
            templateUrl: 'app/views/pois/poiShow.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id);
                }
            }
        })
        // route to edit the specific details of a poi
        .when('/user/:userId/poi/:poiId/edit', {
            // specific controller for this
            controller: 'poiEditController',
            // specific partial view for this
            templateUrl: 'app/views/pois/poiEdit.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id);
                }
            }
        })
});

/**
 @function config module Travel
 @description configure all the routes
 @param $routeProvider - this parameter will redirect to the right views
 **/
angular.module('vopTravel').config(function ($routeProvider) {
    $routeProvider
    //route to all travels
        .when('/user/:userId/travel/', {
            // specify controller for this route
            controller: 'travelListController',
            // specify partial view for this route
            templateUrl: 'app/views/travels/travelList.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.userId);
                }
            }
        })
        // route to add a travel
        .when('/user/:userId/travel/add', {
            // specify controller for this route
            controller: 'travelAddController',
            // specify partial view for this route
            templateUrl: 'app/views/travels/travelAdd.html',
            resolve: {
                // check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.userId);
                }
            }
        })
        // route to the specific details of a travel
        .when('/user/:userId/travel/:travelId', {
            // specify controller for this route
            controller: 'travelShowController',
            // specify partial view for this route
            templateUrl: 'app/views/travels/travelShow.html',
            resolve: {
                //check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.userId);
                }
            }
        })
        // route to edit the specific details of a travel
        .when('/user/:userId/travel/:travelId/edit', {
            // specify controller for this route
            controller: 'travelEditController',
            // specify partial view for this route
            templateUrl: 'app/views/travels/travelEdit.html',
            resolve: {
                //check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.userId);
                }
            }
        })
});

/**
 @function config module Route
 @description configure all the routes
 @param $routeProvider - this parameter will redirect to the right views
 **/
angular.module('vopRoute').config(function ($routeProvider) {
    $routeProvider
    // route to edit the details of a specific route
        .when('/user/:userId/travel/:travelId/route/:routeId/edit/', {
            // specify controller for this route
            controller: 'routeEditController',
            // specify partial view for this route
            templateUrl: 'app/views/routes/routeEdit.html',
            resolve: {
                //check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.userid);
                }
            }
        })
});


/**
 @function config module Event
 @description configure all the routes
 @param $routeProvider - this parameter will redirect to the right views
 **/
angular.module('vopEvent').config(function ($routeProvider) {
    $routeProvider
    // Specify routes with controller and templateUrl

    // route to all events
        .when('/event/', {
            // specify controller for this route
            controller: 'eventListController',
            // specify partial view for this route
            templateUrl: 'app/views/events/eventList.html',
            resolve: {
                //check permissions of user
                permission: function (Roles, Authorization) {
                    return Authorization.permissionCheck([Roles.getOperator()]);
                }
            }
        })
        // route to add event
        .when('/event/add', {
            // specify controller for this route
            controller: 'eventAddController',
            // specify partial view for this route
            templateUrl: 'app/views/events/eventAdd.html',
            resolve: {
                //check permissions of user
                permission: function (Roles, Authorization) {
                    return Authorization.permissionCheck([Roles.getOperator()]);
                }
            }
        })
        // route to specific events with details
        .when('/event/:id', {
            // specify controller for this route
            controller: 'eventShowController',
            // specify partial view for this route
            templateUrl: 'app/views/events/eventShow.html',
            resolve: {
                //check permissions of user
                permission: function (Roles, Authorization) {
                    return Authorization.permissionCheck([Roles.getOperator()]);
                }
            }
        })
        .when('/event/:eventId/edit', {
            // specify controller for this route
            controller: 'eventEditController',
            // specify partial view for this route
            templateUrl: 'app/views/events/eventEdit.html',
            resolve: {
                //check permissions of user
                permission: function (Roles, Authorization) {
                    return Authorization.permissionCheck([Roles.getOperator()]);
                }
            }
        })
        // route to inbox of user
        .when('/user/:userId/event', {
            // specify controller for this route
            controller: 'userEventsShowController',
            // specify partial view for this route
            templateUrl: 'app/views/users/userEventsShow.html',
            resolve: {
                //check permissions of user
                //$routeParams can't be used here: use $route.current.params.id !!parameters are only loaded in $routeParams after location actually changed!!
                permission: function (RabbitReceiver, Roles, Authorization, $route) { 
                    return Authorization.permissionCheck([Roles.getUser()], $route.current.params.id).then(function(){
                        return RabbitReceiver.hasSeenEvents();
                    });
                }
            }
        })
});

angular.module('vopApp').config(['$translateProvider', function ($translateProvider) {
    $translateProvider
        .translations('nl', translations_nl)
        .preferredLanguage('nl');
    $translateProvider.useSanitizeValueStrategy('escape');
}]);

angular.module('vopApp').run(['RabbitReceiver', function(RabbitReceiver){
    RabbitReceiver.startReceiving();
}]);

var formatAddress = function (address, position) {
    var result = {};
    result.housenumber = "1";
    address.address_components.forEach(function (entry) {
        if (entry.types[0] === "route" || entry.types[0] === "premise") {
            result.street = entry.long_name;
        } else if (entry.types[0] === "street_number") {
            result.housenumber = entry.long_name;
        } else if (entry.types[0] === "locality") {
            result.city = entry.long_name;
        } else if (entry.types[0] === "country") {
            result.country = entry.short_name;
        } else if (entry.types[0] === "postal_code") {
            result.postal_code = entry.long_name;
        }
    });
    result.coordinates = {
        lat: position.lat(),
        lon: position.lng()
    };
    return result;
};


/**
 * @function formatTime
 * @description Converts the hours and minutes of a javascript date to a string (formatted HH:mm).
 * @param date - a javascript date.
 */
var formatTime = function (date) {
    var hours, minutes;
    if (date.getMinutes() < 10) {
        minutes = "0" + date.getMinutes().toString();
    } else {
        minutes = date.getMinutes().toString();
    }
    if (date.getHours() < 10) {
        hours = "0" + date.getHours().toString();
    } else {
        hours = date.getHours().toString();
    }
    return hours + ":" + minutes;
};
