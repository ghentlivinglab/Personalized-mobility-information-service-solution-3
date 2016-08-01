describe('Event Controllers', function () {
  beforeEach(module('vopApp'));
  beforeEach(module('templates'));

  beforeEach(function() {
    jasmine.getEnv().addMatchers({
      toEqualData: function(util, customEqualityTesters) { 
                return { 
                    compare: function(actual, expected) { 
                        return { 
                            pass: angular.equals(actual, expected)
                        };
                    } 
                };
            } 
    });
  });

  describe('eventListController', function() {
    var scope, ctrl, $httpBackend, event1, event2, event3;

    event1 = {"id":"eventId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/eventId"}],"coordinates":{"lat":51.061436,"lon":3.680263},"active":false,"description":"Dit is een test event.","jam":{"speed":26,"delay":2569,"start_node":{"lat":51.048344,"lon":3.758815},"end_node":{"lat":51.047207,"lon":3.773301}},"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"1","type":"ROAD_CLOSED","subtype":"ROAD_CLOSED_EVENT","relevant_for_transportation_types":["bus","car","bike","streetcar"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
    event2 = {"id":"1","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/1"}],"coordinates":{"lat":51.079049,"lon":3.691705},"active":false,"description":"Dit is een test event.","jam":{"speed":17,"delay":3015,"start_node":{"lat":51.031899,"lon":3.671271},"end_node":{"lat":51.027925,"lon":3.753934}},"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"3","type":"JAM","subtype":"JAM_MODERATE_TRAFFIC","relevant_for_transportation_types":["bus","car"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
    event3 = {"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/2"}],"coordinates":{"lat":51.024944,"lon":3.751568},"active":false,"description":"Dit is een test event.","jam":{"speed":4,"delay":601,"start_node":{"lat":51.074479,"lon":3.70258},"end_node":{"lat":51.076489,"lon":3.758721}},"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"6","type":"DEFAULT","subtype":null,"relevant_for_transportation_types":["bus","car","bike","streetcar","train"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
    
    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
      $httpBackend = _$httpBackend_;
      $httpBackend.when('GET','https://vopro7.ugent.be/api/event').respond([event1, event2, event3]);

      scope = $rootScope.$new();
      ctrl = $controller('eventListController', {$scope: scope});
    }));

    it('should create "eventList" model with 1 event for each of the events received from the backend', inject(function($controller) {
      $httpBackend.flush();
      expect(scope.events).toEqualData([event1, event2, event3]);
    }));
	
	it('should correctly delete events', inject(function($controller, _$httpBackend_) {
      $httpBackend.flush();
      _$httpBackend_.expect('DELETE','https://vopro7.ugent.be/api/event/eventId').respond(scope.events[0]);
      scope.deleteEvent(scope.events[0]);
	  _$httpBackend_.flush();
    }));
  });
  
  

  describe('eventListController', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, event1, event2, event3, poi;

    event1 = {"id":"eventId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/eventId"}],"coordinates":{"lat":51.061436,"lon":3.680263},"active":false,"description":"Dit is een test event.","jam":{"speed":26,"delay":2569,"start_node":{"lat":51.048344,"lon":3.758815},"end_node":{"lat":51.047207,"lon":3.773301}},"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"1","type":"ROAD_CLOSED","subtype":"ROAD_CLOSED_EVENT","relevant_for_transportation_types":["bus","car","bike","streetcar"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
    event2 = {"id":"1","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/1"}],"coordinates":{"lat":51.079049,"lon":3.691705},"active":false,"description":"Dit is een test event.","jam":{"speed":17,"delay":3015,"start_node":{"lat":51.031899,"lon":3.671271},"end_node":{"lat":51.027925,"lon":3.753934}},"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"3","type":"JAM","subtype":"JAM_MODERATE_TRAFFIC","relevant_for_transportation_types":["bus","car"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
    event3 = {"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/2"}],"coordinates":{"lat":51.024944,"lon":3.751568},"active":false,"description":"Dit is een test event.","jam":{"speed":4,"delay":601,"start_node":{"lat":51.074479,"lon":3.70258},"end_node":{"lat":51.076489,"lon":3.758721}},"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"6","type":"DEFAULT","subtype":null,"relevant_for_transportation_types":["bus","car","bike","streetcar","train"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
	
	poi={"id":"poiId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId"}],"address":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487232,"lon":3.7241699999999582},"postal_code":9000},"name":"test","radius":15000,"active":true,"notify":{"email":false,"cell_number":false},"notify_for_event_types":[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}]}

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      $routeParams.eventId = 'eventId';
      scope = $rootScope.$new();
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/event').respond([event1, event2, event3]);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
      ctrl = $controller('eventListController', {$scope: scope, $route:{reload: function() {}}});
	  var hlep = $templateCache.get('app/views/events/eventList.html');
	  /*var hlep = angular.element(
		  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  );*/
	  template = $compile(hlep)(scope);
	  scope.$digest();
    }));

    it('should create "events" model with 1 poi for each of the pois received from the backend', inject(function($controller) {
      $httpBackend.flush();
      expect(scope.events).toEqualData([event1, event2, event3]);
    }));

    it('should correctly delete events', inject(function($controller, _$httpBackend_) {
      $httpBackend.flush();
      _$httpBackend_.expect('DELETE','https://vopro7.ugent.be/api/event/eventId').respond(event1);
      scope.deleteEvent(scope.events[0]);
	  _$httpBackend_.flush();
    }));
	
	it('should define a link', function() {
		scope.goto('eventId');
	});
  });
  
  describe('eventShowController', function() {
    var scope, ctrl, $httpBackend, event, template;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      $routeParams.id = 'eventId';
      scope = $rootScope.$new();
      ctrl = $controller('eventShowController', {$scope: scope, $route:{}});
	  
	  event={"id":"eventId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/eventId"}],"coordinates":{"lat":51.061436,"lon":3.680263},"active":false,"description":"Dit is een test event.","jam":{"speed":26,"delay":2569,"start_node":{"lat":51.048344,"lon":3.758815},"end_node":{"lat":51.047207,"lon":3.773301}},"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"1","type":"ROAD_CLOSED","subtype":"ROAD_CLOSED_EVENT","relevant_for_transportation_types":["bus","car","bike","streetcar"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/event/eventId').respond(event);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  var hlep = $templateCache.get('app/views/events/eventShow.html');
	  /*var hlep = angular.element(
		  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  );*/
	  template = $compile(hlep)(scope);
	  scope.$digest();
    }));

    it('should correctly create an event model using an event retrieved from the backend', inject(function() {
		$httpBackend.flush();
    	expect(scope.event).toEqualData(event);
    }));
	
	it('should define some links', function() {
		scope.editEvent();
		scope.back();
	});
  });
  
  describe('eventEditController', function() {
    var scope, ctrl, $httpBackend, event, template, eventTypes;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      $routeParams.eventId = 'eventId';
      scope = $rootScope.$new();
      ctrl = $controller('eventEditController', {$scope: scope, $route:{}});
	  
	  event={"id":"eventId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/eventId"}],"coordinates":{"lat":51.061436,"lon":3.680263},"active":false,"description":"Dit is een test event.","jams":[{"speed":26,"delay":2569,"start_node":{"lat":51.048344,"lon":3.758815},"end_node":{"lat":51.047207,"lon":3.773301}}],"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"relevant_for_transportation_types":["bus","car","bike","streetcar"],"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
	  eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  transportationTypes=["car","bus","bike","train","streetcar"];
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/event/eventId').respond(event);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/transportationtype').respond(transportationTypes);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
	  var hlep = $templateCache.get('app/views/events/eventEdit.html');
	  /*var hlep = angular.element(
		  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  );*/
	  template = $compile(hlep)(scope);
	  scope.$digest();
    }));

    it('should correctly create an event model using an event retrieved from the backend', inject(function() {
		$httpBackend.flush();
		event.jams[0].show=true;
    	expect(scope.event).toEqualData(event);
    }));
	
	it('should correctly edit the event', inject(function() {
		
		$httpBackend.flush();
     	$httpBackend.expect('PUT','https://vopro7.ugent.be/api/event/eventId').respond(event);
		scope.selectedEventTypes = scope.event.relevant_transportation_types;
		scope.deletedJams = [event.jams[0]];
		
		event.jams[0].show=true;
		var place = {address_components:[{"long_name":"Burgstraat","short_name":"Burgstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],adr_address:"<span class=\"street-address\">Burgstraat</span>, <span class=\"postal-code\">9000</span> <span class=\"locality\">Gent</span>, <span class=\"country-name\">Belgium</span>",formatted_address:"Burgstraat, 9000 Gent, Belgium",geometry:{"location":{"lat":51.0563394,"lng":3.7174691999999823}},icon:"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png","id":"3e9a38e52a4ccf37fc1652c167448920cf9c19c1","name":"Burgstraat","place_id":"ChIJlWQOakBxw0cR_X2rfJAHHhQ",reference:"CoQBewAAABaTw0ExeLZ21dIzl7Or4znkWGYNDCaDYefmBKjEOiWj_EjPYviASxOntdQHDufJpM1MI4zypaSKLVukStJn_NDyWlWngTD3qweHgjFH1Xg8EjWlksFIxb_wMLyaDsszphRxay_t6wFNkeyeZiz1HCva03QNpO3eYiPLUC7k7z3MEhCzK6abnmVALQQnWsqu_VTLGhQSzOgGU2B9I6WU9kDXU6fb3VqVWQ",scope:"GOOGLE",types:["route"],url:"https://maps.google.com/?q=Burgstraat,+9000+Gent,+Belgium&ftid=0x47c371406a0e6495:0x141e07907cab7dfd","vicinity":"Gent","html_attributions":[]};
		spyOn(scope.addressAutocomplete, 'getPlace').and.returnValue(place);
		spyOn(place.geometry.location, 'lat').and.returnValue(51.0563394);
		spyOn(place.geometry.location, 'lng').and.returnValue(3.7174691999999823);
		scope.editEvent();
		$httpBackend.flush();
    }));
  });
  
  describe('eventAddController', function() {
    var scope, ctrl, $httpBackend, event, eventTypes, template;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      scope = $rootScope.$new();
      ctrl = $controller('eventAddController', {$scope: scope, $route:{}});
	  
	  event={"address":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487232,"lon":3.7241699999999582},"postal_code":9000},"name":"test","radius":15000,"active":true,"notify":{"email":false,"cell_number":false},"notify_for_event_types":[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}]};
	  eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  transportationTypes=["car","bus","bike","train","streetcar"];
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/transportationtype').respond(transportationTypes);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
	  var hlep = $templateCache.get('app/views/events/eventAdd.html');
	  /*var hlep = angular.element(
		  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  );*/
	  template = $compile(hlep)(scope);
	  scope.$digest();
    }));

    it('should correctly add events', inject(function() {
		$httpBackend.flush();
     	$httpBackend.expect('POST','https://vopro7.ugent.be/api/event').respond(event);
		
		var place = {address_components:[{"long_name":"Burgstraat","short_name":"Burgstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],adr_address:"<span class=\"street-address\">Burgstraat</span>, <span class=\"postal-code\">9000</span> <span class=\"locality\">Gent</span>, <span class=\"country-name\">Belgium</span>",formatted_address:"Burgstraat, 9000 Gent, Belgium",geometry:{"location":{"lat":function() {},"lng":function() {}}},icon:"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png","id":"3e9a38e52a4ccf37fc1652c167448920cf9c19c1","name":"Burgstraat","place_id":"ChIJlWQOakBxw0cR_X2rfJAHHhQ",reference:"CoQBewAAABaTw0ExeLZ21dIzl7Or4znkWGYNDCaDYefmBKjEOiWj_EjPYviASxOntdQHDufJpM1MI4zypaSKLVukStJn_NDyWlWngTD3qweHgjFH1Xg8EjWlksFIxb_wMLyaDsszphRxay_t6wFNkeyeZiz1HCva03QNpO3eYiPLUC7k7z3MEhCzK6abnmVALQQnWsqu_VTLGhQSzOgGU2B9I6WU9kDXU6fb3VqVWQ",scope:"GOOGLE",types:["route"],url:"https://maps.google.com/?q=Burgstraat,+9000+Gent,+Belgium&ftid=0x47c371406a0e6495:0x141e07907cab7dfd","vicinity":"Gent","html_attributions":[]};
		spyOn(scope.addressAutocomplete, 'getPlace').and.returnValue(place);
		spyOn(place.geometry.location, 'lat').and.returnValue(51.0563394);
		spyOn(place.geometry.location, 'lng').and.returnValue(3.7174691999999823);
		
		scope.makeJamForm();
		scope.placeAddress(scope.addressAutocomplete);
		scope.placeAddress(scope.addressAutocomplete);
		scope.jamjam.delay = 100;
		scope.jamjam.speed = 100;
		scope.addJam(); //Causes an exception because latLng is undefined, then used as the positions for several markers
		scope.makeJamForm();
		scope.placeAddress(scope.addressAutocomplete);
		scope.placeAddress(scope.addressAutocomplete);
		scope.addJam();
		
		scope.fillEventObject();
		scope.createEvent();
		$httpBackend.flush();
    }));
  });
});