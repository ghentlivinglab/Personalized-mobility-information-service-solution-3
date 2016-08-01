angular.module('vopApp').service('general', function ($routeParams, $http, $resource, HOSTNAME) {

    this.APIhost = 'https://vopro7.ugent.be/api';

    //Fetches the event types that have been defined
    this.EventType = function () {
        return $resource(HOSTNAME.URL + 'eventtype/', {});
    };

    //Fetches the transportation types that have been defined
    this.TransportationTypes = function () {
        return $resource(HOSTNAME.URL + 'transportationtype/', {});
    };

    // Download datadump
    this.datadump = function () {
        return $http({
            method: 'POST',
            url: HOSTNAME.URL + 'admin/data_dump/'
        });
    };

});
