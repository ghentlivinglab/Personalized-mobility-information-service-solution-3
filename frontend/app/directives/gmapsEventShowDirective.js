angular.module('vopEvent').directive("gmapsEventShow", function ($timeout) {
    return {
        restrict: "A",
        scope: false,
        link: function (scope, element, attrs, ngModel) {
            var lt;
            var lg;
            var geocoder, bounds, directionsService, directionsDisplay, markers = [];
            /**
             @constant MARKERICON
             @description The icon for the jams
             @type {string}
             */
            var MARKERICON = 'assets/images/map-marker.svg';
            /**
             @constant SPECIALMARKERICON
             @description The icon for the events
             @type {string}
             */
            var SPECIALMARKERICON = 'assets/images/map-pin-special.svg';
            /**
             @function jamRender
             @description Render the jam
             @memberof angular_module.vopApp.vopEvent.eventShowController
             @param start_node - The start node of the jam
             @param end_node - The end node of the jam
             */
            scope.jamRender = function (start_node, end_node) {
                markers.forEach(function (m) {
                    m.setMap(null);
                });
                var jamMarkerStart = new google.maps.Marker({
                    position: {
                        lat: start_node.lat,
                        lng: start_node.lon
                    },
                    icon: MARKERICON,
                    draggable: false,
                    animation: google.maps.Animation.DROP,
                    map: scope.map
                });
                var jamMarkerEnd = new google.maps.Marker({
                    position: {
                        lat: end_node.lat,
                        lng: end_node.lon
                    },
                    icon: MARKERICON,
                    draggable: false,
                    animation: google.maps.Animation.DROP,
                    map: scope.map
                });
                bounds.extend(jamMarkerStart.getPosition());
                bounds.extend(jamMarkerEnd.getPosition());

                var request = {
                    origin: jamMarkerStart.getPosition(),
                    destination: jamMarkerEnd.getPosition(),
                    optimizeWaypoints: true,
                    travelMode: google.maps.DirectionsTravelMode.DRIVING
                };
                directionsService.route(request, function (response, status) {
                    if (status == google.maps.DirectionsStatus.OK) {
                        directionsDisplay.setDirections(response);
                    }
                });
                markers.push(jamMarkerStart);
                markers.push(jamMarkerEnd);
                scope.map.fitBounds(bounds);
            };

            scope.event.$promise.then(function (data) {

                /**
                 @member lastEditDate
                 @description The last time when edited in data format
                 @memberof angular_module.vopApp.vopEvent.eventShowController
                 */
                scope.lastEditDate = new Date(data.last_edit_time);
                lt = data.coordinates.lat;
                lg = data.coordinates.lon;
                var options = {
                    center: {
                        lat: lt,
                        lng: lg
                    },
                    zoom: 14
                };
                directionsService = new google.maps.DirectionsService();

                geocoder = new google.maps.Geocoder();
                /**
                 @member map
                 @description The google map
                 @memberof angular_module.vopApp.vopEvent.eventShowController
                 */
                scope.map = new google.maps.Map(element[0].querySelector('#map'), options);
                var marker = new google.maps.Marker({
                    position: {
                        lat: lt,
                        lng: lg
                    },
                    map: scope.map,
                    animation: google.maps.Animation.DROP,
                    draggable: false,
                    icon: SPECIALMARKERICON
                });

                // ADRES WERKT NIET!!
                // Replace coordinates with addresses
                geocoder.geocode({'location': marker.position}, function (results, status) {
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
                directionsDisplay = new google.maps.DirectionsRenderer({
                    polylineOptions: {
                        strokeColor: "red"
                    }
                });
                directionsDisplay.setMap(scope.map);
                directionsDisplay.setOptions({
                    suppressMarkers: true,
                    preserveViewport: true
                });
                bounds = new google.maps.LatLngBounds();
                bounds.extend(marker.getPosition());
                scope.map.fitBounds(bounds);
                /**
                 @member pubDate
                 @description The publication date in data format
                 @memberof angular_module.vopApp.vopEvent.eventShowController
                 */
                scope.pubDate = new Date(data.publication_time);
            });
        }
    }
});
