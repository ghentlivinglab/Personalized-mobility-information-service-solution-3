angular.module('vopPOI').directive("gmapsPoiShow", function ($compile) {
  return {
	restrict: "A",
	scope: false,
	/*templateUrl:'./app/directives/templates/travelAddGmaps.html',*/
	link: function (scope, element, attrs, ngModel) {
		var icon = 'assets/images/map-pin-special.svg';
		scope.poi.$promise.then(function() {
			scope.mapOptions = {
				// Center on POI
				center: {lat:scope.poi.address.coordinates.lat,
					lng:scope.poi.address.coordinates.lon},
				zoom: 10
			};
	
			// Make new map with mapoptions
			scope.map = new google.maps.Map(element[0].querySelector('#map'), scope.mapOptions);
	
			// Create the marker at the location of the point of interest with following settings (see body).
			var marker = new google.maps.Marker({
				draggable: false,
				icon: icon,
				position: {
					lat:scope.poi.address.coordinates.lat,
					lng:scope.poi.address.coordinates.lon
				},
				map: scope.map,
				animation: google.maps.Animation.DROP
			});
	
			// Little extra to make the marker jump up and down when you click on it :D
			marker.addListener('click', function() {
				if (marker.getAnimation() !== null) {
					marker.setAnimation(null);
				} else {
					marker.setAnimation(google.maps.Animation.BOUNCE);
				}
			});
	
			// Add circle overlay and bind to marker
			var circle = new google.maps.Circle({
				map: scope.map,
				radius: scope.poi.radius,
				strokeWeight: 0.3,
				strokeColor: '#000000',
				fillColor: '#009fe3',
				fillOpacity: 0.35
			});
			//var bounds =
			new google.maps.LatLngBounds();
			// Bind the circle to the marker with the marker at it's center
			circle.bindTo('center', marker, 'position');
			scope.map.fitBounds(circle.getBounds());
		});
	}
  }	
});

