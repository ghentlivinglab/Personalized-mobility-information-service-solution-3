angular.module('vopPOI').directive("gmapsPoiEdit", function ($compile, $timeout) {
  return {
	restrict: "A",
	scope: false,
	/*templateUrl:'./app/directives/templates/travelAddGmaps.html',*/
	link: function (scope, element, attrs, ngModel) {
		/**
		 * @constant SPECIALMARKERICON
		 * @description The icon for the marker
		 * @type {string}
		 */
		var icon = 'assets/images/map-pin-special.svg';
		// ...
		scope.geocoder;
		// Variable that will contain the address-data of the added point entered by the user.
		scope.pointAutocomplete;
		// Variable that will contain the data of the marker of the point in order to display a marker on the map.
		scope.pointMarker;
		// Variable that will contain the data of the circle with the point at it's center.
		scope.circle = null;
		
		scope.poi.$promise.then(function() {
			scope.geocoder = new google.maps.Geocoder();
			scope.mapOptions = {
				// Center on POI!
				center: {lat: scope.poi.address.coordinates.lat, lng: scope.poi.address.coordinates.lon},
				zoom: 10
			};
			// Make new map with mapoptions.
			scope.map = new google.maps.Map(element[0].querySelector('#map'), scope.mapOptions);
			// Initialize the little input field where the user can enter the address.
			var pointInput = element[0].querySelector("#pointAutocomplete");
			// Push input field onto the map.
			scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(element[0].querySelector("#pointAutocompleteForm"));
			scope.pointAutocomplete = new google.maps.places.Autocomplete(pointInput, {types: ['geocode']});
			scope.pointAutocomplete.addListener('place_changed', function(){
				scope.addPoint();
			});
			// Initialize the little input field where the user can enter the radius of the circle.
			scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(element[0].querySelector("#radiusForm"));
	
			$(element[0]).ready(function(){
				// Stop the default submit on enter
				$("#pointAutocompleteForm").submit(function(){
					return false;
				});
				// Tell the input field what to do if you press enter
				$("#pointAutocomplete").keyup(function(event){
					if(event.keyCode == 13){
						// Wait half a second, so we don't get errors, because not everything is loaded correctly
						setTimeout(function(){scope.addPoint();
						},500);
					}
				});
				// Stop the default submit on enter
				$("#radiusForm").submit(function(){
					return false;
				});
				// Tell the input field what to do if you press enter
				$("#radius").change(function(){
					scope.addNewCircle();
				});
			});
	
			// Create the marker of the point of interest we are going to be editing onto the map :D
			scope.pointMarker = new google.maps.Marker({
				draggable: true,
				icon: icon,
				position: {
					lat:scope.poi.address.coordinates.lat,
					lng:scope.poi.address.coordinates.lon
				},
				map: scope.map,
				animation: google.maps.Animation.DROP
			});
			scope.pointMarker.addListener('dragend', function () {
				scope.geocoder.geocode({'location': scope.pointMarker.position}, function (results, status) {
					if (status === google.maps.GeocoderStatus.OK) {
						if (results[0]) {
							element[0].querySelector("#pointAutocomplete").value = results[0].formatted_address;
						}
					}
				})
			});
			scope.geocoder.geocode({'location': scope.pointMarker.position}, function (results, status) {
				if (status === google.maps.GeocoderStatus.OK) {
					if (results[0]) {
						element[0].querySelector("#pointAutocomplete").value = results[0].formatted_address;
					}
				}
			});
			// Do the funny bounce animation when clicked.
			scope.pointMarker.addListener('click', function() {
				if (marker.getAnimation() !== null) {
					marker.setAnimation(null);
				} else {
					marker.setAnimation(google.maps.Animation.BOUNCE);
				}
			});
			scope.pointMarker.addListener('rightclick', function(){
				scope.pointMarker.setMap(null);
			});
	
			// Add circle overlay and bind to marker
			scope.circle = new google.maps.Circle({
				map: scope.map,
				radius: scope.poi.radius,
				strokeWeight: 0.3,
				strokeColor: '#000000',
				fillColor: '#009fe3',
				fillOpacity: 0.35
			});
	
			// Bind the circle to the marker of the point of interest with the marker at the circle's center.
			scope.circle.bindTo('center', scope.pointMarker, 'position');
			scope.map.fitBounds(scope.circle.getBounds());
		});
		

		/**
		 * @function addNewCircle
		 * @description Creates the circle and adds it to the pointMarker if this marker exists
		 * @memberof angular_module.vopApp.vopPOI.poiEditController
		 */
		scope.addNewCircle = function(){
			//Erase the previous circle.
			if(scope.circle != null){
				scope.circle.setMap(null);
			}
			//Create circle with new specs
			scope.circle = new google.maps.Circle({
				map: scope.map,
				radius: scope.poi.radius,
				strokeWeight: 0.3,
				strokeColor: '#000000',
				fillColor: '#009fe3',
				fillOpacity: 0.35
			});
			//If the user already has entered a marker and if this marker is visible on the map bind it to it.
			if(scope.pointMarker != null && scope.pointMarker.map != null){
				scope.circle.bindTo('center', scope.pointMarker, 'position');
			}
			scope.map.fitBounds(scope.circle.getBounds());
		};
	
		/**
		 * @function addNewMarker
		 * @description Add the marker from the pointAutocomplete
		 * @memberof angular_module.vopApp.vopPOI.poiEditController
		 * @param position - a json object containing the following data {lat: {number}, lng: {number}}
		 * @param icon - contains the data of the marker that will be placed upon the map
		 */
		scope.addNewMarker = function(position, icon){
			var marker = new google.maps.Marker({
				draggable: true,
				icon: icon,
				position: position,
				map: scope.map,
				animation: google.maps.Animation.DROP
			});
			marker.addListener('click', function() {
				if (marker.getAnimation() !== null) {
					marker.setAnimation(null);
				} else {
					marker.setAnimation(google.maps.Animation.BOUNCE);
				}
			});
			// Delete marker by rightclick
			marker.addListener('rightclick', function () {
				marker.setMap(null);
			});
			scope.map.setCenter(marker.position);
			return marker;
		};
	
		/**
		 * @function addPoint
		 * @description Attempts to display the created point on the map
		 * @memberof angular_module.vopApp.vopPOI.poiEditController
		 */
		scope.addPoint = function(){
			if(scope.pointMarker != null){
				scope.pointMarker.setMap(null);
			}
			scope.pointMarker = scope.addNewMarker({
				lat: scope.pointAutocomplete.getPlace().geometry.location.lat(),
				lng: scope.pointAutocomplete.getPlace().geometry.location.lng()
			}, icon);
			scope.mapOptions = {
				// Center on POI!
				center: {lat: scope.pointAutocomplete.getPlace().geometry.location.lat(), lng: scope.pointAutocomplete.getPlace().geometry.location.lng()},
				zoom: 14
			};
			scope.pointMarker.addListener('dragend', function () {
				scope.geocoder.geocode({'location': scope.pointMarker.position}, function (results, status) {
					if (status === google.maps.GeocoderStatus.OK) {
						if (results[0]) {
							element[0].querySelector("#pointAutocomplete").value = results[0].formatted_address;
						}
					}
				})
			});
			scope.circle.bindTo('center', scope.pointMarker, 'position');
			scope.circle.setMap(scope.map);
		};
		
		/**
		 * @function showErrorMessage
		 * @description show the error message
		 * @memberof angular_module.vopApp.vopPOI.poiEditController
		 * @param msg - the error message which will be shown
		 */
		scope.showErrorMessage = function(msg){
			$(element[0]).ready(function(){
				var errorMessage = $("#errorMessage");
				errorMessage.find("p").text(msg);
				errorMessage.fadeTo(1000,1);
				var body = $(".poiAdd");
				body.css('transition','1s');
				body.css('-webkit-filter','blur(5px)');
			});
		};
	
		/**
		 * @function close
		 * @description closes the alert div and returns the css back to normal so the user can correct his/her mistake
		 * @memberof angular_module.vopApp.vopPOI.poiEditController
		 */
		scope.close = function(){
			$(element[0]).ready(function(){
				$("#errorMessage").fadeOut();
				var body = $(".poiAdd");
				body.css('transition','1s');
				body.css('-webkit-filter','blur(0px)');
			});
		};
	
		/**
		 * @function submit
		 * @description round about way of making the submit button work in multiple browsers as it is not inside the form
		 * @memberof angular_module.vopApp.vopPOI.poiAddController
		 */
		scope.submit = function(){
			//simulate the clicking of the submit button
			$timeout(function(){$("#poiEditForm").click();},0);
		}
	}
  }	
});