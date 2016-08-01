angular.module('vopTravel').directive("gmapsTravelShow", function ($compile) {
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
		var MARKERICON = 'assets/images/map-marker.svg';
		/**
		 * @constant SPECIALMARKERICON
		 * @description The icon for the marker
		 * @type {string}
		 */
		var SPECIALMARKERICON = 'assets/images/map-pin-special.svg';
		scope.markers = [];
		scope.overview_path=[];
		/**
		 @function clearMap
		 @description clear the map
		 @memberof angular_module.vopApp.vopTravel.travelShowController
		 @private
		 */
		var clearMap = function () {
			scope.markers.forEach(function (m) {
				m.setMap(null);
			});
			scope.markers = [];
			var bounds = new google.maps.LatLngBounds();
			bounds.extend(scope.originMarker.getPosition());
			bounds.extend(scope.destinationMarker.getPosition());
			scope.map.fitBounds(bounds);
		};
		scope.showRoute = function (id) {
			clearMap();
			var bounds = new google.maps.LatLngBounds();
			angular.forEach(scope.routes[id].route.waypoints, function (w) {
				var mark = new google.maps.Marker({
					position: {
						lat: w.lat,
						lng: w.lon
					},
					draggable: false,
					animation: google.maps.Animation.DROP,
					icon: MARKERICON,
					map: scope.map
				});
				scope.markers.push(mark);
				bounds.extend(mark.getPosition());
				bounds.extend(scope.originMarker.getPosition());
				bounds.extend(scope.destinationMarker.getPosition());
			});
			scope.map.fitBounds(bounds);
			scope.renderRoute(scope.markers, scope.routes[id]);
		};
		scope.travel.$promise.then(function() {
			// scope.map = new google.maps.Map(document.getElementById('map'), scope.mapOptions);
			scope.mapOptions = {
				// Center on Ghent
				center: {lat: 51.0533593, lng: 3.723252},
				zoom: 14
			};
			scope.map = new google.maps.Map(element[0].querySelector('#map'), scope.mapOptions);
			scope.originMarker = new google.maps.Marker({
				icon: SPECIALMARKERICON,
				draggable: false,
				animation: google.maps.Animation.DROP,
				map: scope.map,
				position: {
					lat: scope.travel.startpoint.coordinates.lat,
					lng: scope.travel.startpoint.coordinates.lon
				}
			});
			scope.destinationMarker = new google.maps.Marker({
				icon: SPECIALMARKERICON,
				draggable: false,
				animation: google.maps.Animation.DROP,
				map: scope.map,
				position: {
					lat: scope.travel.endpoint.coordinates.lat,
					lng: scope.travel.endpoint.coordinates.lon
				}
			});
			scope.directionsService = new google.maps.DirectionsService();
			scope.directionsDisplay = new google.maps.DirectionsRenderer();
			scope.directionsDisplayRouteAdd = new google.maps.DirectionsRenderer();
		
			scope.directionsDisplay.setOptions({suppressMarkers: true});
			scope.directionsDisplayRouteAdd.setOptions({suppressMarkers: true});
			var bounds = new google.maps.LatLngBounds();
			bounds.extend(scope.originMarker.getPosition());
			bounds.extend(scope.destinationMarker.getPosition());
			scope.map.fitBounds(bounds);
			scope.routeMap = new google.maps.Map(element[0].querySelector('#routeMap'), scope.mapOptions);
			scope.routeMap.fitBounds(bounds);
			scope.directionsDisplay.setMap(scope.map);
			scope.directionsDisplayRouteAdd.setMap(scope.routeMap);
			// Init MarkerDropper and waypointAdder
			var markerDropperDiv = element[0].querySelector('#markerDropper');
			var waypointAdderDiv = element[0].querySelector('#waypointAdder');

			new MarkerDropper(markerDropperDiv, scope, scope.routeMap, MARKERICON);
			scope.routeMap.controls[google.maps.ControlPosition.TOP_RIGHT].push(markerDropperDiv);
			var addWaypointInput = element[0].querySelector('#waypointAutocomplete');
			scope.addWaypointAutocomplete = new google.maps.places.Autocomplete(addWaypointInput, {types: ['geocode']});

			new WaypointAdder(waypointAdderDiv, "routeMap");
			scope.routeMap.controls[google.maps.ControlPosition.RIGHT_TOP].push(waypointAdderDiv);
			scope.routeMap.controls[google.maps.ControlPosition.TOP_RIGHT].push(element[0].querySelector('#mode'));
			scope.routeMap.addListener('click', function () {
				// Hide the div + clear the input field when you click outside the form
				$(element[0]).ready(function () {
					$("#addWaypoint").fadeOut();
					$("#routeMap").css('transition', '1s');
					$("#routeMap").css('-webkit-filter', 'blur(0px)');
					$("#waypointAutocomplete").val('');
				});
			});
		});
		
		scope.routeMarkers = [];
		/**
		 @function addMarker
		 @description Add a marker on the map
		 @memberof angular_module.vopApp.vopTravel.travelShowController
		 @param position - the position of the new added marker
		 @param icon - the icon of the marker
		 */
		scope.addNewMarker = function (position, icon) {
			var marker = new google.maps.Marker({
				draggable: true,
				icon: icon,
				position: position,
				map: scope.routeMap,
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
				scope.renderRoute(scope.routeMarkers)
			});
			marker.addListener('dragend', function () {
				// if(marker.getMap()==scope.map) {
				//     scope.renderRoute();
				// } else {
				scope.renderRoute(scope.routeMarkers);
				// }
			});
			scope.routeMarkers.push(marker);
			scope.renderRoute(scope.routeMarkers);
			return marker;
		};
		
		/**
		 @function addMarkerWaypoint
		 @description Add the marker from the waypointAutocomplete
		 @memberof angular_module.vopApp.vopTravel.travelShowController
		 */
		scope.addMarkerWaypoint = function () {
			scope.addNewMarker({
				lat: scope.addWaypointAutocomplete.getPlace().geometry.location.lat(),
				lng: scope.addWaypointAutocomplete.getPlace().geometry.location.lng()
			}, MARKERICON);
			// Hide the add waypoint div + clear the input field
			$(element[0]).ready(function () {
				$("#addWaypoint").fadeOut();
				$("#routeMap").css('transition', '1s');
				$("#routeMap").css('-webkit-filter', 'blur(0px)');
				$("#waypointAutocomplete").val('');
			});
		};
		
		/**
		 @function cancelRoute
		 @description cancel the route add and close the div
		 @memberof angular_module.vopApp.vopTravel.travelShowController
		 */
		scope.cancelRoute = function () {
			$(element[0]).ready(function () {
				$("#routeAddDiv").animate({left: "-100%"}, "slow");
				$("#travelDetailDiv").css('transition', '1s');
				$("#travelDetailDiv").css('-webkit-filter', 'blur(0px)');
			});
			scope.originMarker.setMap(scope.map);
			scope.destinationMarker.setMap(scope.map);
		};
		
		/**
		 @function showRouteDiv
		 @description show the route add div
		 @memberof angular_module.vopApp.vopTravel.travelShowController
		 */
		scope.showRouteDiv = function () {
			$(element[0]).ready(function () {
				$("#routeAddDiv").animate({left: "10%"});
				$("#travelDetailDiv").css('transition', '1s');
				$("#travelDetailDiv").css('-webkit-filter', 'blur(5px)');
			});
			scope.originMarker.setMap(scope.routeMap);
			scope.destinationMarker.setMap(scope.routeMap);
			scope.renderRoute(scope.routeMarkers);
		};
		
		/**
		 @function renderRoute
		 @description Render the route on the map
		 @memberof angular_module.vopApp.vopTravel.travelShowController
		 */
		scope.renderRoute = function (customMarkers, route) {
			if (customMarkers == null) {
				customMarkers = scope.routeMarkers;
			}
			var waypoints = [];
			if (scope.originMarker == null) {
				scope.showErrorMessage("Gelieve eerst een vertrekpunt in te geven.");
			} else {
				if (scope.destinationMarker == null) {
					scope.showErrorMessage("Gelieve eerst een bestemming in te geven");
				} else {
					customMarkers.forEach(function (entry) {
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
					var selectedMode;
					selectedMode = mappingDictionary[element[0].querySelector('#mode').value.substring(7)];
					if (route != null) {
						selectedMode = mappingDictionary[route.route.transportation_type];
					}
					var request = {
						origin: scope.originMarker.getPosition(),
						destination: scope.destinationMarker.getPosition(),
						waypoints: waypoints,
						optimizeWaypoints: true,
						travelMode: google.maps.TravelMode[selectedMode]
					};
					scope.directionsService.route(request, function (result, status) {
						// result.routes[0].overview_path.forEach(function(e){
						//    scope.addNewMarker({lat: e.lat(), lng: e.lng()}, MARKERICON);
						// });
						if (status == google.maps.DirectionsStatus.OK) {
							if (customMarkers == scope.markers) {
								scope.directionsDisplay.setDirections(result);
							} else {
								scope.overview_path = [];
								result.routes[0].overview_path.forEach(function(entry){
									scope.overview_path.push({lat:entry.lat(), lon:entry.lng()});
								});
								scope.directionsDisplayRouteAdd.setDirections(result);
							}
						} else {
							// Any kind of error
							scope.showErrorMessage("Er ging iets fout bij het berekenen van de route. Controleer of uw internetverbinding goed werkt en probeer daarna opnieuw.");
						}
					});
				}
			}
		};
	}
  }	
});

