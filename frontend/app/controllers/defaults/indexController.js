/**
 @author Frontend team
 @memberof angular_module.vopApp
 @constructor indexController
 */
angular.module('vopApp').controller('indexController', function($scope, $http, $location) {

    /**
     @description The function will redirect to the contact page.
     @function toContacts
     @memberof angular_module.vopApp.indexController
     */
    $scope.toContacts = function(){
        $location.path('/contact/');
    };

    /**
     @description The function will redirect to the log in page.
     @function logIn
     @memberof angular_module.vopApp.indexController
     */
    $scope.logIn = function(){
        $location.path('/login/');
    };

    /**
     @description The function will redirect to the registration page.
     @function register
     @memberof angular_module.vopApp.indexController
     */
    $scope.register = function(){
        $location.path('/user/add');
    };
});
