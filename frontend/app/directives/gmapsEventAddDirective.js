angular.module('vopEvent').directive("gmapsEventAdd", function ($timeout) {
	return {
		restrict: "A",
		scope: false,
		link: function (scope, element, attrs, ngModel) {
			/**
				@constant SPECIALMARKERICON
			 	@description The icon for the events
				@type {string}
		  	*/
		  	var SPECIALMARKERICON = 'assets/images/map-pin-special.svg';
			
			/**
			  @function addJam
			  @description Add a jam to the event + render on map
			  @memberof angular_module.vopApp.vopEvent.eventAddController
			*/
			scope.addJam=function(){
			  if(scope.markers==2){
				var pushJam={};
				if(scope.markerArray[0].start){
				  pushJam = {
					start_node:{
					  lat:scope.markerArray[0].mark.position.lat(),
					  lon:scope.markerArray[0].mark.position.lng()
					},
					end_node:{
					  lat:scope.markerArray[1].mark.position.lat(),
					  lon:scope.markerArray[1].mark.position.lng()
					}
				  }
				} else {
				  pushJam = {
					start_node:{
					  lat:scope.markerArray[1].mark.position.lat(),
					  lon:scope.markerArray[1].mark.position.lng()
					},
					end_node:{
					  lat:scope.markerArray[0].mark.position.lat(),
					  lon:scope.markerArray[0].mark.position.lng()
					}
				  }
				}
		  
				if(scope.jamjam.speed!=undefined){
				  pushJam.speed = scope.jamjam.speed;
				}
				if(scope.jamjam.delay!=undefined){
				  pushJam.delay = scope.jamjam.delay;
				}
				scope.event.jams.push(pushJam);
				scope.jamAddSuccess=true;
				scope.makeJamForm();
			  }
			};
			
			/**
			  @member markers
			  @description number of markers of the jams
			  @memberof angular_module.vopApp.vopEvent.eventAddController
			  @private
			*/
			scope.markers=0;
			/**
			  @member markerArray
			  @description array with all the markers used for the jams
			  @memberof angular_module.vopApp.vopEvent.eventAddController
			  @private
			*/
			scope.markerArray=[];
			/**
			@function makeJamForm
			@description Clear the jam form and reset for the next jam
			@memberof angular_module.vopApp.vopEvent.eventAddController
			*/
			scope.makeJamForm = function(){
			  scope.markers=0;
			  scope.markerArray=[];
			  /**
			  @member jamjam
			  @description model for the jam
			  @property speed - The average speed in the jam
			  @property delay - The average delay in seconds of the jam
			  @memberof angular_module.vopApp.vopEvent.eventAddController
			  */
			  scope.jamjam={};
			  if(scope.addressAutocomplete.getPlace()!=undefined){
				scope.mapOptions = {
				  center:{lat: scope.addressAutocomplete.getPlace().geometry.location.lat(),
				  			lng: scope.addressAutocomplete.getPlace().geometry.location.lng()},
				  zoom:16
				};
			  } else {
				/**
				@member mapOptions
				@description options for the google map
				@memberof angular_module.vopApp.vopEvent.eventAddController
				*/
				scope.mapOptions = {
				  // Center on Ghent
				  center: {lat: 51.0533593, lng: 3.723252},
				  zoom: 14
				};
			  }
			  element[0].querySelector("#startpointAddressAutocomplete").value='';
			  element[0].querySelector("#endpointAddressAutocomplete").value='';
			  /**
			  @member map
			  @description The google map
			  @memberof angular_module.vopApp.vopEvent.eventAddController
			  */
			  scope.map = new google.maps.Map(element[0].querySelector('#map'), scope.mapOptions);
			  scope.map.addListener('click', function(event){
				if(scope.markers<2){
				  var marker = new google.maps.Marker({
					map: scope.map,
					position: event.latLng,
					draggable:true,
					icon: SPECIALMARKERICON,
					animation:google.maps.Animation.DROP
				  });
				  marker.addListener('rightclick', function(){
					scope.markers=scope.markers-1;
					marker.setMap(null);
				  });
				  scope.markers=scope.markers+1;
				  if(scope.markers==1){
					scope.markerArray.push({
					  start:true,
					  mark: marker
					});
				  } else {
					scope.markerArray.push({
					  start:false,
					  mark: marker
					});
				  }
				}
			  });
			};
			
			/**
			@function placeAddress
			@description place address marker on the map
			@param autocomplete - the addressAutocompleter of google
			@memberof angular_module.vopApp.vopEvent.eventAddController
			*/
			scope.placeAddress= function(autocomplete){
			  if(scope.markers<2){
				var marker = new google.maps.Marker({
				  map: scope.map,
				  position: autocomplete.getPlace().geometry.location,
				  draggable:true,
				  icon:SPECIALMARKERICON,
				  animation:google.maps.Animation.DROP
				});
				marker.addListener('rightclick', function(){
				  scope.markers=scope.markers-1;
				  marker.setMap(null);
				});
				scope.markers=scope.markers+1;
				if(scope.startJamAutocomplete==autocomplete){
				  scope.markerArray.push({
					start:true,
					mark: marker
				  });
				} else {
				  scope.markerArray.push({
					start:false,
					mark: marker
				  });
				}
			  } else {
				scope.showErrorMessage("Er werden reeds 2 punten opgegeven. Gelieve eerst deze file op te slaan of een punt te verwijderen door er met de rechtermuisknop op te klikken.");
			  }
			};
			/**
			  @member addressAutocomplete
			  @member startJamAutocomplete
			  @member endJamAutocomplete
			  @private
			  @memberof angular_module.vopApp.vopEvent.eventAddController
			*/
			
			scope.addressInput = element[0].querySelector("#addressAutocomplete");
			scope.addressAutocomplete = new google.maps.places.Autocomplete(scope.addressInput, {types: ['geocode']});
			scope.startpointJamInput = element[0].querySelector("#startpointAddressAutocomplete");
			scope.endpointJamInput = element[0].querySelector("#endpointAddressAutocomplete");
			scope.startJamAutocomplete = new google.maps.places.Autocomplete(scope.startpointJamInput, {types: ['geocode']});
			scope.endJamAutocomplete = new google.maps.places.Autocomplete(scope.endpointJamInput, {types: ['geocode']});
			scope.endJamAutocomplete.addListener('place_changed', function(){
			  scope.placeAddress(scope.endJamAutocomplete);
			});
			scope.startJamAutocomplete.addListener('place_changed', function(){
			  scope.placeAddress(scope.startJamAutocomplete);
			});
			$(element[0]).ready(function(){
			  // prevent default handling
			  $('#eventAddForm').on('keyup keypress', function(e) {
				var keyCode = e.keyCode || e.which;
				if (keyCode === 13) {
				  e.preventDefault();
				  return false;
				}
			  });
			});
			
			/**
			@function showErrorMessage
			@description show an error message on the page
			@param msg - The message in string notation
			@memberof angular_module.vopApp.vopEvent.eventAddController
			*/
			scope.showErrorMessage = function(msg){
			  $(element[0]).ready(function(){
				$("#errorMessage").find("p").text(msg);
				$("#errorMessage").fadeTo(1000,1);
				$("#pageWrap").css('transition','1s');
				$("#pageWrap").css('-webkit-filter','blur(5px)');
			  });
			};
			/**
			@function close
			@description close the error message div
			@memberof angular_module.vopApp.vopEvent.eventAddController
			*/
			scope.close = function(){
			  $(element[0]).ready(function(){
				$("#errorMessage").fadeOut();
				$("#pageWrap").css('transition','1s');
				$("#pageWrap").css('-webkit-filter','blur(0px)');
			  });
			};
		}
	}
});
