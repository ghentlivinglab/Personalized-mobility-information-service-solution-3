/**
  @class angular_module.myApp
  @description the angular app
*/
var app = angular.module('myApp',["ngRoute",
  'routeControllers',
  'travelControllers',
  'routeDirectives',
  'travelService',
  'routeService',
  'eventTypeService',
  'ngMessages'
  //'uiGmapgoogle-maps'
]);
/**
  @function config
  @description configure all the routes
  @param $routeProvider - this parameter will redirect to the right views
**/
app.config(function ($routeProvider) {
  $routeProvider
  // Specify routes with controller and templateUrl

  // route to all events
  .when('/event/', {
    // specify controller for this route
    controller: 'eventListController',
    // specify partial view for this route
    templateUrl: 'app/views/events/eventList.html'
  })
  // route to add event
  .when('/event/add', {
    // specify controller for this route
    controller: 'eventAddController',
    // specify partial view for this route
    templateUrl: 'app/views/events/eventAdd.html'
  })
  // route to specific events with details
  .when('/event/:id', {
    // specify controller for this route
    controller: 'eventShowController',
    // specify partial view for this route
    templateUrl: 'app/views/events/eventShow.html'
  })


  // Route to all users
  .when('/user/',{
    // specify controller for this route
    controller: 'userListController',
    // specify partial view for this route
    templateUrl: 'app/views/users/userList.html'
  })
  // route to add a user
  .when('/user/add', {
    // specify controller for this route
    controller: 'userAddController',
    // specify partial view for this route
    templateUrl: 'app/views/users/userAdd.html'
  })
  // route to edit a user
  .when('/user/:id/edit',{
    // specify controller for this route
    controller:'userEditController',
    // specify partial view for this route
    templateUrl: 'app/views/users/userEdit.html'
  })
  // route to fetch specific user information
  .when('/user/:id', {
    // specify controller for this route
    controller: 'userShowController',
    // specify partial view for this route
    templateUrl: 'app/views/users/userShow.html'
  })

  // route of all routes!
  .when('/user/:userId/travel/:travelId/route/', {
    // specify controller for this route
    controller: 'RouteListCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/route_list.html'
  })
  // route to add a route
  .when('/user/:userId/travel/:travelId/route/add/', {
    // specify controller for this route
    controller: 'AddRouteCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/route_add.html'
  })
  // route to the specific details of a route
  .when('/user/:userId/travel/:travelId/route/:routeId/', {
    // specify controller for this route
    controller: 'ShowRouteCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/route_show.html'
  })
  // route to edit the details of a specific route
  .when('/user/:userId/travel/:travelId/route/:routeId/edit/', {
    // specify controller for this route
    controller: 'EditRouteCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/route_edit.html'
  })

  //route to all travels
  .when('/user/:userId/travel/', {
    // specify controller for this route
    controller: 'TravelListCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/travel_list.html'
  })
  // route to add a travel
  .when('/user/:userId/travel/add', {
    // specify controller for this route
    controller: 'AddTravelCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/travel_add.html'
  })
  // route to the specific details of a travel
  .when('/user/:userId/travel/:travelId', {
    // specify controller for this route
    controller: 'ShowTravelCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/travel_show.html'
  })
  // route to edit the specific details of a travel
  .when('/user/:userId/travel/:travelId/edit', {
    // specify controller for this route
    controller: 'EditTravelCtrl',
    // specify partial view for this route
    templateUrl: 'app/views/routes/travel_edit.html'
  })

  // route to inbox of user
  .when('/user/:id/event', {
    // specify controller for this route
    controller: 'userEventsShowController',
    // specify partial view for this route
    templateUrl: 'app/views/users/userEventsShow.html'
  })

  // route to the index page
  .when('/index', {
    // specify controller for this route
    controller: 'indexController',
    // specify partial view for this route
    templateUrl: 'app/views/defaults/index.html'
  })

  // route to the contact page
  .when('/contact', {
    // specify controller for this route
    controller: 'contactController',
    // specify partial view for this route
    templateUrl: 'app/views/defaults/contact.html'
  })

  // route to the contact page
  .when('/notImp', {
    // specify controller for this route
    controller: 'contactController',
    // specify partial view for this route
    templateUrl: 'app/views/defaults/notImp.html'
  })

  // route to the error page
  .when('/404', {
    // specify controller for this route
    controller: '404Controller',
    // specify partial view for this route
    templateUrl: 'app/views/defaults/404.html'
  })

  // default route is the error page
  .otherwise({
    redirectTo: '/404'
  });
});

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