/**
 * @author Frontend team
 * @memberof angular_module.vopApp.vopUser
 * @constructor userShowController
 */
angular.module('vopUser').controller('userShowController', function($scope, $http, $routeParams, $location, User, general, Roles, Session) {

    /**
     * @member user
     * @description model that contains the user's data
     * @memberof angular_module.vopApp.vopUser.userShowController
     */
    $scope.user = User.get({ userId : $routeParams.id });

    /**
     * @function editUser
     * @description redirects to the edit page of this user
     * @memberof angular_module.vopApp.vopUser.userShowController
     */
    $scope.editUser = function(){
        $location.path('/user/'+ $routeParams.id + '/edit/');
    };

    /**
     * @function delete
     * @description deletes this user
     * @memberof angular_module.vopApp.vopUser.userShowController
     */
    $scope.delete = function(){
        $(document).ready(function () {
            $("#overlappingForm").fadeIn();
            $("#userShow").css('transition', '1s');
            $("#userShow").css('-webkit-filter', 'blur(5px)');
        });
    };

    /**
     * @function changePass
     * @description redirects to the page one which a user can change his password
     * @memberof angular_module.vopApp.vopUser.userShowController
     */
    $scope.changePass = function(){
        //delete token and return to homepage.
        $location.path('/user/' + $routeParams.id + '/change_pass');
    };

    $scope.admin = Roles.getAdmin() == Session.getUserRole();

    /**
     * @function downloadDatadump
     * @description if the user is an administrator than this function makes a dump of all the data in the database so the database
     * @memberof angular_module.vopApp.vopUser.userShowController
     */
    $scope.downloadDatadump = function(){
        general.datadump().success(function(data){
            if (!data) {
                console.error('No data');
                return;
            }

            var filename = 'dataDump.json';
            if (typeof data === 'object') {
                data = JSON.stringify(data, undefined, 2);
            }

            var blob = new Blob([data], {type: 'text/json'}),
                e = document.createEvent('MouseEvents'),
                a = document.createElement('a');

            a.download = filename;
            a.href = window.URL.createObjectURL(blob);
            a.dataset.downloadurl = ['text/json', a.download, a.href].join(':');
            e.initEvent('click', true, false, window,
                0, 0, 0, 0, 0, false, false, false, false, 0, null);
            a.dispatchEvent(e);
        });
    };

    $scope.yes = function(){
        //delete token and return to homepage.
        $location.path('/logout');
        //delete user
        $scope.user.$delete(function(){});
    };
    $scope.no = function(){
        $(document).ready(function () {
            $("#overlappingForm").fadeOut();
            $("#userShow").css('transition', '1s');
            $("#userShow").css('-webkit-filter', 'blur(0px)');
        });
    };
});
