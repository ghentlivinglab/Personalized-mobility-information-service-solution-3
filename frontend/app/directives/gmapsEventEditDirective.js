angular.module('vopEvent').directive("gmapsEventEdit", function ($timeout) {
	return {
		restrict: "A",
		scope: false,
		link: function (scope, element, attrs, ngModel) {
				
			/**
			 @member deletedJams
			 @private
			 @description array with all the deleted jams in json format
			 @memberof angular_module.vopApp.vopEvent.eventEditController
			 */
			scope.deletedJams = [];
			/**
			 @function deleteJam
			 @description delete the jam from the event
			 @memberof angular_module.vopApp.vopEvent.eventEditController
			 */
			scope.deleteJam = function (jam) {
				jam.show = false;
				scope.deletedJams.push(jam);
			};
			/**
			 @member addressAutocomplete
			 @description variable to hold the google autocomplete
			 @memberof angular_module.vopApp.vopEvent.eventEditController
			 @private
			 */
			scope.addressAutocomplete;
			/**
			 @function initialize
			 @description initialize the page
			 @memberof angular_module.vopApp.vopEvent.eventEditController
			 */
			scope.transportationTypes.$promise.then(function () {
				scope.event.$promise.then(function() {
				var geocoder = new google.maps.Geocoder();
				var addressInput = element[0].querySelector("#addressAutocomplete");
					scope.addressAutocomplete = new google.maps.places.Autocomplete(addressInput, {types: ['geocode']});
					//var marker =
						new google.maps.Marker({
						position: {
							lat: scope.event.coordinates.lat,
							lng: scope.event.coordinates.lon
						}
					});
					geocoder.geocode({
						'location': {
							lat: scope.event.coordinates.lat,
							lng: scope.event.coordinates.lon
						}
					}, function (results, status) {
						if (status === google.maps.GeocoderStatus.OK) {
							scope.$apply(function () {
								if (results[0]) {
									scope.event.addr = results[0].formatted_address;
								} else {
									alert('Google vond geen resultaten');
								}
							});
						}
					});
				});
			});
		}
	}
});
