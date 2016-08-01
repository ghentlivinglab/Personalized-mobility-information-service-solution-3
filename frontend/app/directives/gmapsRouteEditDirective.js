angular.module('vopRoute').directive("gmapsRouteEdit", function ($timeout) {
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

		var bounds, originMarker, destinationMarker, addWaypointAutocomplete, directionsDisplay, directionsService;
		scope.overview_path=[];
		
		scope.travel.$promise.then(function() {
			bounds = new google.maps.LatLngBounds();
			directionsService = new google.maps.DirectionsService();
			directionsDisplay = new google.maps.DirectionsRenderer();
			scope.mapOptions = {
				// Center on Ghent
				center: {lat: 51.0533593, lng: 3.723252},
				zoom: 14
			};
			// Make new map with mapoptions
			scope.map = new google.maps.Map(element[0].querySelector('#map'), scope.mapOptions);
			directionsDisplay.setMap(scope.map);
			
			
			// Init MarkerDropper and waypointAdder
			var markerDropperDiv = element[0].querySelector('#markerDropper');
			var waypointAdderDiv = element[0].querySelector('#waypointAdder');

			new MarkerDropper(markerDropperDiv, scope, scope.map, MARKERICON);
			scope.map.controls[google.maps.ControlPosition.TOP_RIGHT].push(markerDropperDiv);
			var addWaypointInput = element[0].querySelector('#waypointAutocomplete');
			scope.addWaypointAutocomplete = new google.maps.places.Autocomplete(addWaypointInput, {types: ['geocode']});

			new WaypointAdder(waypointAdderDiv, "routeMap");
			scope.map.controls[google.maps.ControlPosition.RIGHT_TOP].push(waypointAdderDiv);
			
			// Init showRouteButton
			// var showRouteButtonDiv = document.createElement('div');
			// new ShowRouteButton(showRouteButtonDiv, this);
			// scope.map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(showRouteButtonDiv);
			scope.map.controls[google.maps.ControlPosition.TOP_RIGHT].push(element[0].querySelector("#mode"));
			originMarker = new google.maps.Marker({
				icon: SPECIALMARKERICON,
				draggable: false,
				map: scope.map,
				position: {
					lat: scope.travel.startpoint.coordinates.lat,
					lng: scope.travel.startpoint.coordinates.lon
				},
				animation: google.maps.Animation.DROP
			});
			destinationMarker = new google.maps.Marker({
				icon: SPECIALMARKERICON,
				draggable: false,
				map: scope.map,
				position: {
					lat: scope.travel.endpoint.coordinates.lat,
					lng: scope.travel.endpoint.coordinates.lon
				},
				animation: google.maps.Animation.DROP
			});
			scope.route.waypoints.forEach(function (w) {
				bounds.extend(
					scope.addNewMarker({
						lat: w.lat,
						lng: w.lon
					}, MARKERICON).getPosition());
			});
	
			bounds.extend(originMarker.getPosition());
			bounds.extend(destinationMarker.getPosition());
			scope.map.fitBounds(bounds);
	
			// Add listener to map
			scope.map.addListener('click', function () {
				// Hide the div + clear the input field when you click outside the form
				$(element[0]).ready(function () {
					$("#addWaypoint").fadeOut();
					var tmp = $("#editRouteDiv");
					tmp.css('transition', '1s');
					tmp.css('-webkit-filter', 'blur(0px)');
					$("#waypointAutocomplete").val('');
				});
			});
		});
	
		/**
		 * @function renderRoute
		 * @description renders the route on the map
		 * @memberof angular_module.vopApp.vopRoute.routeEditController
		 */
		scope.renderRoute = function () {
			var waypoints = [];
			if (originMarker == null) {
				scope.showErrorMessage("Gelieve eerst een vertrekpunt in te geven.");
			} else {
				if (destinationMarker == null) {
					scope.showErrorMessage("Gelieve eerst een bestemming in te geven");
				} else {
					scope.markers.forEach(function (entry) {
						if (entry.getMap() != null) {
							if (!(entry == originMarker || entry == destinationMarker)) {
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
					if(selectedMode!=null) {
						var request = {
							origin: originMarker.getPosition(),
							destination: destinationMarker.getPosition(),
							waypoints: waypoints,
							optimizeWaypoints: true,
							travelMode: google.maps.TravelMode[selectedMode]
						};
						directionsDisplay.setOptions({suppressMarkers: true});
						directionsService.route(request, function (result, status) {
							if (status == google.maps.DirectionsStatus.OK) {
								scope.overview_path = [];
								result.routes[0].overview_path.forEach(function(entry){
									scope.overview_path.push({lat:entry.lat(), lon:entry.lng()});
								});
								directionsDisplay.setDirections(result);
							} else {
								// Any kind of error
								scope.showErrorMessage("Er ging iets fout bij het berekenen van de route. Controleer of uw internetverbinding goed werkt en probeer daarna opnieuw.");
							}
						});
					}
				}
			}
		};
		
		/**
		 @function addMarkerWaypoint
		 @description Add the marker from the waypointAutocomplete
		 @memberof angular_module.vopApp.vopRoute.routeEditController
		 */
		scope.addMarkerWaypoint = function () {
			scope.addNewMarker({
				lat: addWaypointAutocomplete.getPlace().geometry.location.lat(),
				lng: addWaypointAutocomplete.getPlace().geometry.location.lng()
			}, MARKERICON);
			// Hide the add waypoint div + clear the input field
			$(element[0]).ready(function () {
				$("#addWaypoint").fadeOut();
				var tmp = $("#editRouteDiv");
				tmp.css('transition', '1s');
				tmp.css('-webkit-filter', 'blur(0px)');
				$("#waypointAutocomplete").val('');
			});
		};
	
		scope.markers = [];
	
		/**
		 @function addMarker
		 @description Add a marker on the map
		 @memberof angular_module.vopApp.vopRoute.routeEditController
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
				for (var i = 0; i < scope.markers.length; i++) {
					if (scope.markers[i] == marker) {
						delete scope.markers[i];
					}
				}
				marker.setMap(null);
			});
			marker.addListener('dragend', function () {
				scope.renderRoute();
			});
	
			scope.markers.push(marker);
			scope.renderRoute();
			return marker;
		};
	}
  }	
});
