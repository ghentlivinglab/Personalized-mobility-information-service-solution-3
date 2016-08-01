angular.module('vopTravel').directive("gmapsTravelAdd", function ($timeout) {
  return {
	restrict: "A",
	scope: false,
	link: function (scope, element, attrs, ngModel) {
		/**
		 * @constant SPECIALMARKERICON
		 * @description The icon for the marker
		 * @type {string}
		 */
		var MARKERICON = 'assets/images/map-marker.svg';
		/**
		 * @constant SPECIALMARKERICON
		 * @description The icon for the marker
		 * @type {string}
		 */
		var SPECIALMARKERICON = 'assets/images/map-pin-special.svg';
		scope.markers=[];
		
		scope.overview_path=[];
		scope.routeMarkers=[];
		/**
		 @function cancelRoute
		 @description cancel the routeAdd and remove the overlay
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 */
		scope.cancelRoute = function () {
			$(element[0]).ready(function () {
				$("#overlappingForm").fadeOut();
				$("#travels").css('transition', '1s');
				$("#travels").css('-webkit-filter', 'blur(0px)');
			});
		};
		
		/**
		 @function showRouteForm
		 @description show the div where you can add a route to the travel
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 */
		scope.showRouteForm = function () {
			if (scope.originMarker == null || scope.destinationMarker == null) {
				scope.showErrorMessage("Gelieve eerst een vertrekpunt en een bestemming op te geven.");
			} else {
				$(element[0]).ready(function () {
					$("#overlappingForm").fadeIn();
					$("#travels").css('transition', '1s');
					$("#travels").css('-webkit-filter', 'blur(5px)');
				});
			}
		};
		scope.bounds = new google.maps.LatLngBounds();
		scope.geocoder = new google.maps.Geocoder();
		scope.directionsService = new google.maps.DirectionsService();
		scope.directionsDisplay = new google.maps.DirectionsRenderer();
		scope.mapOptions = {
			// Center on Ghent
			center: {lat: 51.0533593, lng: 3.723252},
			zoom: 14
		};
		// Make new map with mapoptions
		scope.map = new google.maps.Map(element[0].querySelector('#map'), scope.mapOptions);
		scope.directionsDisplay.setMap(scope.map);
		// Add the textfields for origin and destination
		var startInput = element[0].querySelector("#startpointAutocomplete");
		var endInput = element[0].querySelector("#endpointAutocomplete");
		var addWaypointInput = element[0].querySelector('#waypointAutocomplete');
		scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(element[0].querySelector("#startpointAutocompleteForm"));
		scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(element[0].querySelector("#endpointAutocompleteForm"));
		scope.startpointAutocomplete = new google.maps.places.Autocomplete(startInput, {types: ['geocode']});
		scope.endpointAutocomplete = new google.maps.places.Autocomplete(endInput, {types: ['geocode']});
		scope.addWaypointAutocomplete = new google.maps.places.Autocomplete(addWaypointInput, {types: ['geocode']});
		scope.startpointAutocomplete.addListener('place_changed', function () {
			if (scope.startpointAutocomplete.getPlace() != null) {
				scope.addOrigin();
			}
			if (scope.originMarker != null && scope.destinationMarker != null) {
				scope.renderRoute();
			}
		});
		scope.endpointAutocomplete.addListener('place_changed', function () {
			if (scope.endpointAutocomplete.getPlace() != null) {
				scope.addDestination();
			}
			if (scope.originMarker != null && scope.destinationMarker != null) {
				scope.renderRoute();
			}
		});
	
		// Init MarkerDropper and waypointAdder
		var markerDropperDiv = element[0].querySelector('#markerDropper');
		var waypointAdderDiv = element[0].querySelector('#waypointAdder');
		
		new MarkerDropper(markerDropperDiv, scope, scope.map, MARKERICON);
		scope.map.controls[google.maps.ControlPosition.TOP_RIGHT].push(markerDropperDiv);
		
		new WaypointAdder(waypointAdderDiv, "map");
		scope.map.controls[google.maps.ControlPosition.RIGHT_TOP].push(waypointAdderDiv);
		
		scope.map.controls[google.maps.ControlPosition.TOP_RIGHT].push(element[0].querySelector("#mode"));
		// Add listener to map
		scope.map.addListener('click', function () {
			// Hide the div + clear the input field when you click outside the form
			$(element[0]).ready(function () {
				$("#addWaypoint").fadeOut();
				$("#map").css('transition', '1s');
				$("#map").css('-webkit-filter', 'blur(0px)');
				$("#waypointAutocomplete").val('');
			});
		});
		$(element[0]).ready(function () {
			// Stop the default submit on enter
			$("#startpointAutocompleteForm").submit(function (event) {
				return false;
			});
			$("#endpointAutocompleteForm").submit(function (event) {
				return false;
			});
		});
		
		
		/**
		 @function addMarkerWaypoint
		 @description Add the marker from the waypointAutocomplete
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 */
		scope.addMarkerWaypoint = function () {
			scope.addNewMarker({
				lat: scope.addWaypointAutocomplete.getPlace().geometry.location.lat(),
				lng: scope.addWaypointAutocomplete.getPlace().geometry.location.lng()
			}, MARKERICON);
			// Hide the add waypoint div + clear the input field
			$(element[0]).ready(function () {
				$("#addWaypoint").fadeOut();
				$("#map").css('transition', '1s');
				$("#map").css('-webkit-filter', 'blur(0px)');
				$("#waypointAutocomplete").val('');
			});
		};
		/**
		 @function addMarker
		 @description Add a marker on the map
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 @param position - the position of the new added marker
		 @param icon - the icon of the marker
		 */
		scope.addNewMarker = function (position, icon) {
			var marker = new google.maps.Marker({
				draggable: true,
				icon: icon,
				position: position,
				map: scope.map,
				animation: google.maps.Animation.DROP
			});
			marker.addListener('click', function () {
				if (marker.getAnimation() !== null) {
					marker.setAnimation(null);
				} else {
					marker.setAnimation(google.maps.Animation.BOUNCE);
				}
			});
			// Delete marker by rightclick
			marker.addListener('rightclick', function () {
				marker.setMap(null);
				if (marker != scope.originMarker && marker != scope.destinationMarker) {
					scope.renderRoute();
				}
			});
			marker.addListener('dragend', function () {
				if (scope.originMarker != null && scope.destinationMarker != null) {
					scope.renderRoute();
				}
			});
			scope.markers.push(marker);
			if (scope.originMarker != null && scope.destinationMarker != null) {
				scope.renderRoute();
			}
			return marker;
		};
		/**
		 @function renderRoute
		 @description Render the route on the map
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 */
		scope.renderRoute = function () {
			var waypoints = [];
			if (scope.originMarker == null) {
				scope.showErrorMessage("Gelieve eerst een vertrekpunt in te geven.");
			} else {
				if (scope.destinationMarker == null) {
					scope.showErrorMessage("Gelieve eerst een bestemming in te geven");
				} else {
					scope.markers.forEach(function (entry) {
						if (entry.getMap() != null) {
							if (!(entry == scope.originMarker || entry == scope.destinationMarker)) {
								waypoints.push({
									location: entry.position,
									stopover: true
								});
							}
						}
					});
	
					var mappingDictionary = {
						"car": "DRIVING",
						"bus": "TRANSIT",
						"streetcar": "TRANSIT",
						"train": "TRANSIT",
						"bike": "BICYCLING"
					};
					var selectedMode = mappingDictionary[element[0].querySelector("#mode").value];
					var request = {
						origin: scope.originMarker.getPosition(),
						destination: scope.destinationMarker.getPosition(),
						waypoints: waypoints,
						optimizeWaypoints: true,
						travelMode: google.maps.TravelMode[selectedMode]
					};
					scope.directionsDisplay.setOptions({suppressMarkers: true});
					scope.directionsService.route(request, function (result, status) {
						if (status == google.maps.DirectionsStatus.OK) {
							scope.overview_path = [];
							result.routes[0].overview_path.forEach(function(entry){
								scope.overview_path.push({lat:entry.lat(), lon:entry.lng()});
							});
	
							scope.directionsDisplay.setDirections(result);
						} else {
							// Any kind of error
							scope.showErrorMessage("Er ging iets fout bij het berekenen van de route. Controleer of uw internetverbinding goed werkt en probeer daarna opnieuw.");
						}
					});
				}
			}
		};
		/**
		 @function addOrigin
		 @description Add the marker of the origin
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 */
		scope.addOrigin = function () {
			var m = scope.addNewMarker({
				lat: scope.startpointAutocomplete.getPlace().geometry.location.lat(),
				lng: scope.startpointAutocomplete.getPlace().geometry.location.lng()
			}, SPECIALMARKERICON);
			if (scope.originMarker != null) {
				scope.markers.forEach(function (entry) {
					if (entry == scope.originMarker) {
						entry.setMap(null);
					}
				});
			}
			scope.originMarker = m;
			scope.originMarker.addListener('dragend', function () {
				scope.geocoder.geocode({'location': scope.originMarker.position}, function (results, status) {
					if (status === google.maps.GeocoderStatus.OK) {
						if (results[0]) {
							element[0].querySelector("#startpointAutocomplete").value = results[0].formatted_address;
						}
					}
				})
			});
			scope.originMarker.addListener('rightclick', function () {
				element[0].querySelector("#startpointAutocomplete").value = '';
				scope.originMarker = null;
			});
			extendBounds();
		};
	
		var extendBounds = function () {
			scope.bounds = new google.maps.LatLngBounds();
			if(scope.originMarker!=null){
				scope.bounds.extend(scope.originMarker.getPosition());
			}
			if(scope.destinationMarker!=null){
				scope.bounds.extend(scope.destinationMarker.getPosition());
			}
			scope.markers.forEach(function(entry){
				scope.bounds.extend(entry.getPosition());
			});
			scope.map.fitBounds(scope.bounds);
		};
		/**
		 @function addMarkerWaypoint
		 @description Add the marker of the destination
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 */
		scope.addDestination = function () {
			m = scope.addNewMarker({
				lat: scope.endpointAutocomplete.getPlace().geometry.location.lat(),
				lng: scope.endpointAutocomplete.getPlace().geometry.location.lng()
			}, SPECIALMARKERICON);
			if (scope.destinationMarker != null) {
				scope.markers.forEach(function (entry) {
					if (entry == scope.destinationMarker) {
						entry.setMap(null);
					}
				});
			}
			scope.destinationMarker = m;
			scope.destinationMarker.addListener('dragend', function () {
				scope.geocoder.geocode({'location': scope.destinationMarker.position}, function (results, status) {
					if (status === google.maps.GeocoderStatus.OK) {
						if (results[0]) {
							element[0].querySelector("#endpointAutocomplete").value = results[0].formatted_address;
						}
					}
				})
			});
			scope.destinationMarker.addListener('rightclick', function () {
				element[0].querySelector("#endpointAutocomplete").value = '';
				scope.destinationMarker = null;
			});
			extendBounds();
		};
		/**
		 @function showErrorMessage
		 @description show the error message
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 @param msg - the error message which will be shown
		 */
		scope.showErrorMessage = function (msg) {
			$(element[0]).ready(function () {
				$("#errorMessage").find("p").text(msg);
				$("#errorMessage").fadeTo(1000, 1);
				$("#map").css('transition', '1s');
				$("#map").css('-webkit-filter', 'blur(5px)');
			});
		};
		/**
		 @function showErrorMessage
		 @description close the error message div
		 @memberof angular_module.vopApp.vopTravel.travelAddController
		 */
		scope.close = function () {
			$(element[0]).ready(function () {
				$("#errorMessage").fadeOut();
				$("#map").css('transition', '1s');
				$("#map").css('-webkit-filter', 'blur(0px)');
			});
		};
		
		
		scope.submit = function () {
			//simulate the clicking of the submit button
			$timeout(function () {
				$("#travelAddForm").click();
			}, 0);
		}
	}
  }	
});
