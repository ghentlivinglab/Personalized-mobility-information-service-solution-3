angular.module('vopEvent').directive("gmapsUserEventsShow", function () {
    return {
        restrict: "A",
        scope: false,
        link: function (scope, element, attrs, ngModel) {
            var directionsService;
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

            scope.geocoder = new google.maps.Geocoder();
            directionsService = new google.maps.DirectionsService();

            scope.mapOptions = {
                // Center on Ghent!
                center: {lat: 51.0533593, lng: 3.723252},
                zoom: 10
            };

            // Make new map with mapoptions
            scope.map = new google.maps.Map(element[0].querySelector('#map'), scope.mapOptions);
            var trafficLayer = new google.maps.TrafficLayer();
            trafficLayer.setMap(scope.map);
            scope.promise.then(function () {
                var infoWindow = new google.maps.InfoWindow({
                    content: "<p>" + scope.clickedEvent.description + "</p>"
                });
                var bounds = new google.maps.LatLngBounds();
                angular.forEach(scope.userEvents, function (value) {
                    var image_url = null;
                    if (value.source.icon_url != null) {
                        image_url = value.source.icon_url;
                    } else {
                        image_url = 'assets/images/gent_slim op weg_lijn_donkergrijs.png';
                    }
                    // Check if the event is still active
                    if (value.active) {
                        var m = new google.maps.Marker({
                            position: {
                                lat: value.coordinates.lat,
                                lng: value.coordinates.lon
                            },
                            icon: SPECIALMARKERICON,
                            draggable: false,
                            animation: google.maps.Animation.DROP,
                            map: scope.map
                        });
                        bounds.extend(m.getPosition());
                        m.addListener('click', function () {
                            scope.geocoder.geocode({
                                'location': {
                                    lat: value.coordinates.lat,
                                    lng: value.coordinates.lon
                                }
                            }, function (results, status) {
                                if (status === google.maps.GeocoderStatus.OK) {
                                    if (results[0]) {
                                        scope.$apply(function () {
                                            scope.clickedEvent = value;
                                            var content = "<div id='infoWindow' style='text-align: left'><div class='row'><div class='col-sm-4'><img src='" + image_url + "' height='50' width='50'/></div><div class='col-sm-8'><p><strong>" +
                                                value.type.type + "</strong></p></div></div><strong>Beschrijving</strong><p>" +
                                                value.description + "</p><strong>Adres</strong><p>" + results[0].formatted_address + "</p><strong>Publicatie datum</strong><p>" + value.publication_time + "</p></div>";
                                            infoWindow.setContent(content);
                                            infoWindow.open(scope.map, m);
                                        });
                                    } else {
                                        alert('Google vond geen resultaten');
                                    }
                                }
                            });

                        });
                        scope.map.fitBounds(bounds);
                    }
                })
            });
        }
    }
});
