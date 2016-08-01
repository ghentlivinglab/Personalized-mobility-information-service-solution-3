describe('Travel Controllers', function() {
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
  beforeEach(module('vopApp'));
  beforeEach(module('templates'));

  describe('travelListController', function() {
    var scope, ctrl, $httpBackend, RESTData, modelData, route2, route3, route4, week;

    route2 = {"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/2/route/2"}],"name":"LiamVermeir2","startpoint":{"id":"619","city":"Zele","street":"Bergekouter","housenumber":"305","country":"Belgie","coordinates":{"lat":49.1813545,"lon":4.98633385},"postal_code":3352},"endpoint":{"id":"797","city":"Dendermonde","street":"Beekstraat","housenumber":"171","country":"Belgie","coordinates":{"lat":49.9230385,"lon":4.15445852},"postal_code":3074},"waypoints":[{"lat":49.7376747,"lon":4.74123478}],"transportation_types":["bus"],"notify_for_event_types":[{"id":"2","type":null,"subtype":null,"relevant_for_transportation_types":null}]};
    route3 = {"id":"3","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/902/route/3"}],"name":"LiamVermeir3","startpoint":{"id":"619","city":"Zele","street":"Bergekouter","housenumber":"305","country":"Belgie","coordinates":{"lat":49.1813545,"lon":4.98633385},"postal_code":3352},"endpoint":{"id":"797","city":"Dendermonde","street":"Beekstraat","housenumber":"171","country":"Belgie","coordinates":{"lat":49.9230385,"lon":4.15445852},"postal_code":3074},"waypoints":[{"lat":49.7376747,"lon":4.74123478}],"transportation_types":["bus"],"notify_for_event_types":[{"id":"2","type":null,"subtype":null,"relevant_for_transportation_types":null}]};
	
	RESTData=[{"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/2"}],"name":"TravelTest","recurring":[true,true,true,true,true,true,true],"startpoint":{"city":"Aalst","street":"Babbelaarstraat","housenumber":"151","country":"Belgie","coordinates":{"lat":49.7785149,"lon":4.55809879},"postal_code":8200},"endpoint":{"city":"Aarschot","street":"Beekpad","housenumber":"168","country":"Belgie","coordinates":{"lat":50.68993,"lon":3.10359406},"postal_code":3120},"time_interval":["12:00","13:00"],"is_arrival_time":false},
	{"id":"902","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/902"}],"name":"TravelTestVanNICKJEEEE","recurring":[true,true,true,true,true,true,true],"startpoint":{"city":"Roeselareee","street":"Abrahamsssaaweg","housenumber":"3010","country":"Belgie","coordinates":{"lat":51.3766479,"lon":4.79285955},"postal_code":3541},"endpoint":{"city":"Kortrijkaka","street":"Abbeeltjesstraat","housenumber":"206","country":"Belgie","coordinates":{"lat":51.0371933,"lon":4.20691156},"postal_code":9179},"time_interval":["10:00","13:00"],"is_arrival_time":false}];
	
	week = ["maandag","dinsdag","woensdag","donderdag","vrijdag","zaterdag","zondag"];

    modelData=[{"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/2"}],"routes":[route2], hasRoutes:true,"name":"TravelTest","recurring":[true,true,true,true,true,true,true], week:["maandag","dinsdag","woensdag","donderdag","vrijdag","zaterdag","zondag"],"startpoint":{"city":"Aalst","street":"Babbelaarstraat","housenumber":"151","country":"Belgie","coordinates":{"lat":49.7785149,"lon":4.55809879},"postal_code":8200},"endpoint":{"city":"Aarschot","street":"Beekpad","housenumber":"168","country":"Belgie","coordinates":{"lat":50.68993,"lon":3.10359406},"postal_code":3120},"time_interval":["12:00","13:00"],"is_arrival_time":'vertrektijd'},
	{"id":"902","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/902"}],"routes":[route3], hasRoutes:true,"name":"TravelTestVanNICKJEEEE","recurring":[true,true,true,true,true,true,true], week:["maandag","dinsdag","woensdag","donderdag","vrijdag","zaterdag","zondag"],"startpoint":{"city":"Roeselareee","street":"Abrahamsssaaweg","housenumber":"3010","country":"Belgie","coordinates":{"lat":51.3766479,"lon":4.79285955},"postal_code":3541},"endpoint":{"city":"Kortrijkaka","street":"Abbeeltjesstraat","housenumber":"206","country":"Belgie","coordinates":{"lat":51.0371933,"lon":4.20691156},"postal_code":9179},"time_interval":["10:00","13:00"],"is_arrival_time":'vertrektijd'}];

    //modelData = [{"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/2"}],"route":route2,"date":"2010 24 June 10:12am","recurring":[true,true,true,true,true,true,true],"time_interval":["12:00","13:00"],"is_arrival_time":false},{"id":"3","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/3"}],"route":route3,"date":"2010 24 June 10:12am","recurring":[true,true,true,true,true,true,true],"time_interval":["12:00","13:00"],"is_arrival_time":false},{"id":"4","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/travel/4"}],"route":route4,"date":"2010 24 June 10:12am","recurring":[true,true,true,true,true,true,true],"time_interval":["12:00","13:00"],"is_arrival_time":false}];

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
      $httpBackend = _$httpBackend_;
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel').respond(RESTData);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel/2/route').respond([route2]);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel/902/route').respond([route3]);
      $routeParams.userId = 'userId';
      scope = $rootScope.$new();
      ctrl = $controller('travelListController', {$scope: scope});
    }));

    it('should create "travelList" model with 1 travel for each of the travels received from the backend', inject(function($controller) {
      $httpBackend.flush();
      expect(scope.travelList[0].routes).toEqualData(modelData[0].routes);
      expect(scope.travelList[0].hasRoutes).toEqualData(modelData[0].hasRoutes);
      expect(scope.travelList[0].week).toEqualData(modelData[0].week);
      expect(scope.travelList[0].is_arrival_time).toEqualData(modelData[0].is_arrival_time);
	  
      expect(scope.travelList[1].routes).toEqualData(modelData[1].routes);
      expect(scope.travelList[1].hasRoutes).toEqualData(modelData[1].hasRoutes);
      expect(scope.travelList[1].week).toEqualData(modelData[1].week);
      expect(scope.travelList[1].is_arrival_time).toEqualData(modelData[1].is_arrival_time);
	  
      expect(scope.travelList).toEqualData(modelData);
    }));
	
	it('should define some links', function() {
		scope.travelDetails('travelId');
		scope.goto('travelId','routeId');
		scope.editRoute('travelId', 'routeId');
		scope.addRoute('travelId');
		scope.editTravel('travelId');
		scope.addTravel();
	});
  });

  describe('String/Date Conversion', function () {
    /*it('should correctly convert travel.date between string and date', inject(function() {
      expect(formatDate(parseDate("2010 24 June 10:12am"))).toEqualData("2010 24 June 10:12am");
      expect(formatDate(parseDate("2010 24 June 10:12pm"))).toEqualData("2010 24 June 10:12pm");
      expect(formatDate(parseDate("2010 1 June 10:12pm"))).toEqualData("2010 1 June 10:12pm");
      expect(formatDate(parseDate("2010 1 January 10:12pm"))).toEqualData("2010 1 January 10:12pm");
      expect(formatDate(parseDate("2010 1 January 00:00pm"))).toEqualData("2010 1 January 00:00pm");
      expect(formatDate(parseDate("2010 1 January 11:00pm"))).toEqualData("2010 1 January 11:00pm");
      expect(formatTime(parseTime("10:12"))).toEqualData("10:12");
      expect(formatTime(parseTime("00:00"))).toEqualData("00:00");
    }));
    */
	it('should correctly convert travel.time_interval between string and date', inject(function() {
      expect(formatTime(new Date("January 1, 1970 10:12"))).toEqualData("10:12");
      expect(formatTime(new Date("January 1, 1970 00:00"))).toEqualData("00:00");
    }));
  });

  describe('travelShowController', function() {
    var scope, ctrl, $httpBackend, travel, routes, transportationTypes, eventTypes;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $templateCache, $compile, $timeout) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      $routeParams.travelId = 'travelId';
      scope = $rootScope.$new();
      ctrl = $controller('travelShowController', {$scope: scope, $route:{reload: function() {}}});
	  
	  travel={"id":"travelId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/travel/travelId"}],"name":"help","recurring":[false,false,true,false,false,true,false],"startpoint":{"city":"Gent","street":"Ekkergemstraat","housenumber":"127-129","country":"BE","coordinates":{"lat":51.0514755,"lon":3.70652223},"postal_code":9000},"endpoint":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487213,"lon":3.72416997},"postal_code":9000},"time_interval":["04:56","04:56"]};
	  routes=[{"id":"routeId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/travel/travelId/route/routeId"}],"notify":{"email":false,"cell_number":false},"active":true,"waypoints":[{"lat":51.0500755,"lon":3.71713352}],"transportation_type":"car","notify_for_event_types":[{"type":"Jam"},{"type":"Misc"}]}];
	  eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  transportationTypes=["car","bus","bike","train","streetcar"];
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel/travelId').respond(travel);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel/travelId/route').respond(routes);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/transportationtype').respond(transportationTypes);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
	  var hlep = $templateCache.get('app/views/travels/travelShow.html');
		/*var hlep = angular.element(
			'<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
		);*/
		template = $compile(hlep)(scope);
		scope.$digest();
		$httpBackend.flush();
		$timeout.flush();
    }));

    it('should correctly retrieve a travel from the backend and display it', inject(function() {
		expect(scope.travel).toEqualData(travel);
    	expect(scope.routes).toEqualData([{id:0,route:routes[0]}]);
    }));
	
	it('should correctly delete routes', inject(function($controller, _$httpBackend_) {
      _$httpBackend_.expect('DELETE','https://vopro7.ugent.be/api/user/userId/travel/travelId/route/routeId').respond(scope.routes[0]);
      scope.deleteRoute(scope.routes[0].route);
	  _$httpBackend_.flush();
    }));
	
	it('should define some links', function() {
		scope.back();
		scope.goto('routeId');
		scope.editTravel('travelId');
		scope.editRoute('routeId');
	});
	
	it("should correctly add routes", inject(function($controller) {
		$httpBackend.expect('POST','https://vopro7.ugent.be/api/user/userId/travel/travelId/route').respond(200);
	
		status=google.maps.GeocoderStatus.OK;
		scope.overview_path = [{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352}];
		scope.saveRoute();
		$httpBackend.flush();
	}));
	
	it('should correctly delete the travel', function() {
		$httpBackend.expect('DELETE','https://vopro7.ugent.be/api/user/userId/travel/travelId').respond(200);
		scope.deleteTravel(scope.travel);
		$httpBackend.flush();
	});
  });
  
  describe('travelEditController', function() {
    var scope, ctrl, $httpBackend, travel, model;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $templateCache, $compile) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      $routeParams.travelId = 'travelId';
      scope = $rootScope.$new();
      ctrl = $controller('travelEditController', {$scope: scope, $route:{reload: function() {}}});
	  
	  travel={"id":"travelId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/travel/travelId"}],"name":"help","recurring":[false,false,true,false,false,true,false],"startpoint":{"city":"Gent","street":"Ekkergemstraat","housenumber":"127-129","country":"BE","coordinates":{"lat":51.0514755,"lon":3.70652223},"postal_code":9000},"endpoint":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487213,"lon":3.72416997},"postal_code":9000},"time_interval":["04:56","04:56"]};
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel/travelId').respond(travel);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
	  var hlep = $templateCache.get('app/views/travels/travelEdit.html');
		/*var hlep = angular.element(
			'<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
		);*/
		template = $compile(hlep)(scope);
		scope.$digest();
    }));

    it('should correctly create a model for a travel and its route using travels and routes retreived from the backend', inject(function() {
		$httpBackend.flush();
		model = travel;
		model.time=[];
		
    	model.time[0]=new Date("January 1, 1970 "+model.time_interval[0]);
    	model.time[1]=new Date("January 1, 1970 "+model.time_interval[1]);
	
		expect(scope.travel).toEqualData(model);
    }));

    it("should correctly make a PUT request to the travel user's travel resource when the edit button is clicked", inject(function($controller) {
		$httpBackend.flush();
      	$httpBackend.expect('PUT','https://vopro7.ugent.be/api/user/userId/travel/travelId').respond(travel);
      	scope.editTravel();
		
		scope.checkingDays();
		scope.selectedAllDays = true;
		scope.selectAllDays();
		angular.forEach(scope.travel.recurring, function(el, key) {
			expect(el).toEqual(true);
		});
		scope.selectedAllDays = false;
		scope.selectAllDays();
		angular.forEach(scope.travel.recurring, function(el) {
			expect(el).toEqual(false);
		});
		
      	$httpBackend.flush();
    }));
  });
  
  describe('travelAddController', function() {
	  	var scope, ctrl, $httpBackend, travel, model, eventTypes, transportationTypes, status, results;
	
		beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $templateCache, $compile) {
		  	$httpBackend = _$httpBackend_;
	
		  	$routeParams.userId = 'userId';
		  	$routeParams.travelId = 'travelId';
		  	scope = $rootScope.$new();
		  	ctrl = $controller('travelAddController', {$scope: scope, $route:{reload: function() {}}});
		  
		  	travel={"id":"travelId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/travel/travelId"}],"name":"help","recurring":[false,false,true,false,false,true,false],"startpoint":{"city":"Gent","street":"Ekkergemstraat","housenumber":"127-129","country":"BE","coordinates":{"lat":51.0514755,"lon":3.70652223},"postal_code":9000},"endpoint":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487213,"lon":3.72416997},"postal_code":9000},"time_interval":["04:56","04:56"]};
	  		eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  		transportationTypes=["car","bus","bike","train","streetcar"];
		  
		  	$httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
      		$httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      		$httpBackend.when('GET','https://vopro7.ugent.be/api/transportationtype').respond(transportationTypes);
		  
		  	var hlep = $templateCache.get('app/views/travels/travelAdd.html');
			/*var hlep = angular.element(
				'<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
			);*/
			template = $compile(hlep)(scope);
			scope.$digest();
			
			$httpBackend.flush();
			//$httpBackend.expect('POST','https://vopro7.ugent.be/api/user/userId/travel/travelId').respond(travel);
			
			var place1 = {address_components:[{"long_name":"Burgstraat","short_name":"Burgstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],adr_address:"<span class=\"street-address\">Burgstraat</span>, <span class=\"postal-code\">9000</span> <span class=\"locality\">Gent</span>, <span class=\"country-name\">Belgium</span>",formatted_address:"Burgstraat, 9000 Gent, Belgium",geometry:{"location":{"lat":51.0563394,"lng":3.7174691999999823}},icon:"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png","id":"3e9a38e52a4ccf37fc1652c167448920cf9c19c1","name":"Burgstraat","place_id":"ChIJlWQOakBxw0cR_X2rfJAHHhQ",reference:"CoQBewAAABaTw0ExeLZ21dIzl7Or4znkWGYNDCaDYefmBKjEOiWj_EjPYviASxOntdQHDufJpM1MI4zypaSKLVukStJn_NDyWlWngTD3qweHgjFH1Xg8EjWlksFIxb_wMLyaDsszphRxay_t6wFNkeyeZiz1HCva03QNpO3eYiPLUC7k7z3MEhCzK6abnmVALQQnWsqu_VTLGhQSzOgGU2B9I6WU9kDXU6fb3VqVWQ",scope:"GOOGLE",types:["route"],url:"https://maps.google.com/?q=Burgstraat,+9000+Gent,+Belgium&ftid=0x47c371406a0e6495:0x141e07907cab7dfd","vicinity":"Gent","html_attributions":[]}; //'place' object to use in mocking google maps' automplete service
			
			spyOn(scope.startpointAutocomplete, 'getPlace').and.returnValue(place1);
			spyOn(place1.geometry.location, 'lat').and.returnValue(51.0563394);
			spyOn(place1.geometry.location, 'lng').and.returnValue(3.7174691999999823);
			
			var place2 = {address_components:[{"long_name":"Burgstraat","short_name":"Burgstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],adr_address:"<span class=\"street-address\">Burgstraat</span>, <span class=\"postal-code\">9000</span> <span class=\"locality\">Gent</span>, <span class=\"country-name\">Belgium</span>",formatted_address:"Burgstraat, 9000 Gent, Belgium",geometry:{"location":{"lat":51.0563394,"lng":3.7174691999999823}},icon:"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png","id":"3e9a38e52a4ccf37fc1652c167448920cf9c19c1","name":"Burgstraat","place_id":"ChIJlWQOakBxw0cR_X2rfJAHHhQ",reference:"CoQBewAAABaTw0ExeLZ21dIzl7Or4znkWGYNDCaDYefmBKjEOiWj_EjPYviASxOntdQHDufJpM1MI4zypaSKLVukStJn_NDyWlWngTD3qweHgjFH1Xg8EjWlksFIxb_wMLyaDsszphRxay_t6wFNkeyeZiz1HCva03QNpO3eYiPLUC7k7z3MEhCzK6abnmVALQQnWsqu_VTLGhQSzOgGU2B9I6WU9kDXU6fb3VqVWQ",scope:"GOOGLE",types:["route"],url:"https://maps.google.com/?q=Burgstraat,+9000+Gent,+Belgium&ftid=0x47c371406a0e6495:0x141e07907cab7dfd","vicinity":"Gent","html_attributions":[]};
			spyOn(scope.endpointAutocomplete, 'getPlace').and.returnValue(place2);
			spyOn(place2.geometry.location, 'lat').and.returnValue(51.0563394);
			spyOn(place2.geometry.location, 'lng').and.returnValue(3.7174691999999823);
			
			scope.addOrigin();
			scope.addDestination();
			
			scope.time=[new Date(), new Date()];
			
			results = [{"address_components":[{"long_name":"38","short_name":"38","types":["street_number"]},{"long_name":"Bonifantenstraat","short_name":"Bonifantenstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],"formatted_address":"Bonifantenstraat 38, 9000 Gent, Belgium","geometry":{"location":{"lat":51.0564067,"lng":3.7173033999999916},"location_type":"ROOFTOP","viewport":{"south":51.05505771970849,"west":3.7159544197085097,"north":51.05775568029149,"east":3.7186523802914735}},"place_id":"ChIJqXeFQkBxw0cR2LMD-MHK1hY","types":["street_address"]}];
			
			var geocode = function(location, callback) {
					callback(results, status);
			};
			
			spyOn(scope.geocoder, 'geocode').and.callFake(geocode);
			var al = spyOn(window, 'alert');
		}));
	
		it("should correctly log an error message when the add button is clicked and geocoding fails", inject(function($controller) {
			status="NOT OK";
			scope.createTravel();
			expect(window.alert).toHaveBeenCalledWith('Geocoder failed due to: NOT OK');
		}));
		
		it("should correctly make a POST request to the travel resource when the add button is clicked", inject(function($controller) {
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user/userId/travel').respond(travel);
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user/userId/travel/travelId/route').respond(travel);
		
			scope.checking();
			scope.selectedAll = true;
			scope.selectAll();
			angular.forEach(scope.selectedEventTypesTemp, function(el, key) {
				expect(el).toEqual(scope.eventTypes[key].type);
			});
			scope.selectedAll = false;
			scope.selectAll();
			angular.forEach(scope.selectedEventTypesTemp, function(el) {
				expect(el).toEqual(false);
			});
			
			scope.checkingDays();
			scope.selectedAllDays = true;
			scope.selectAllDays();
			angular.forEach(scope.travel.recurring, function(el, key) {
				expect(el).toEqual(true);
			});
			scope.selectedAllDays = false;
			scope.selectAllDays();
			angular.forEach(scope.travel.recurring, function(el) {
				expect(el).toEqual(false);
			});
		
			status=google.maps.GeocoderStatus.OK;
			scope.overview_path = [{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352}];
			scope.saveRoute();
			scope.createTravel();
			$httpBackend.flush();
		}));
		
		it("should correctly add a route and make a POST request to the travel resource when the add button is clicked and the route isn't saved yet", inject(function($controller) {
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user/userId/travel').respond(travel);
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user/userId/travel/travelId/route').respond(travel);
			status=google.maps.GeocoderStatus.OK;
			scope.routes=[];
			scope.overview_path = [{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352}];
			scope.createTravel();
			$httpBackend.flush();
			expect(scope.routes).toEqualData([{"id":"travelId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/travel/travelId"}],"name":"help","recurring":[false,false,true,false,false,true,false],"startpoint":{"city":"Gent","street":"Ekkergemstraat","housenumber":"127-129","country":"BE","coordinates":{"lat":51.0514755,"lon":3.70652223},"postal_code":9000},"endpoint":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487213,"lon":3.72416997},"postal_code":9000},"time_interval":["04:56","04:56"]}]);
		}));
		
		it("should correctly log an error message when the geocoder returns no results", inject(function($controller) {
			status=google.maps.GeocoderStatus.OK;
			results=[];
			scope.createTravel();
			expect(window.alert).toHaveBeenCalledWith('No results found');
		}));
  	});
});