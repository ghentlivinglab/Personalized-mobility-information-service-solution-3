describe('Point of Interest Controllers', function () {
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

  describe('poiListController', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, poi1, poi2, poi3;

    poi1 = {"id":"poiId","address":{"id":"797","city":"Dendermonde","street":"Beekstraat","housenumber":"171","country":"Belgie","coordinates":{"lat":49.9230385,"lon":4.15445852},"postal_code":3074},"name":"home","radius":684648,"active":true,"notify_for_event_types":[{"type":"ROAD_CLOSED"},{"type":"JAM_HEAVY_TRAFFIC"},{"type":"JAM_MODERATE_TRAFFIC"},{"type":"WEATHERHAZARD"},{"type":"HAZARD_ON_ROAD_CONSTRUCTION"},{"type":"DEFAULT"}],"notify":{"email":true,"cell_number":true}};
    poi2 = {"id":"2","address":{"id":"797","city":"Dendermonde","street":"Beekstraat","housenumber":"171","country":"Belgie","coordinates":{"lat":49.9230385,"lon":4.15445852},"postal_code":3074},"name":"home","radius":684648,"active":true,"notify_for_event_types":[{"type":"ROAD_CLOSED"},{"type":"JAM_HEAVY_TRAFFIC"},{"type":"JAM_MODERATE_TRAFFIC"},{"type":"WEATHERHAZARD"},{"type":"HAZARD_ON_ROAD_CONSTRUCTION"},{"type":"DEFAULT"}],"notify":{"email":true,"cell_number":true}};
    poi3 = {"id":"3","address":{"id":"797","city":"Dendermonde","street":"Beekstraat","housenumber":"171","country":"Belgie","coordinates":{"lat":49.9230385,"lon":4.15445852},"postal_code":3074},"name":"home","radius":684648,"active":true,"notify_for_event_types":[{"type":"ROAD_CLOSED"},{"type":"JAM_HEAVY_TRAFFIC"},{"type":"JAM_MODERATE_TRAFFIC"},{"type":"WEATHERHAZARD"},{"type":"HAZARD_ON_ROAD_CONSTRUCTION"},{"type":"DEFAULT"}],"notify":{"email":true,"cell_number":true}};

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
       	$httpBackend = _$httpBackend_;

       	$routeParams.userId = 'userId';
       	$routeParams.poiId = 'poiId';
      	scope = $rootScope.$new();
	  
	  	scope.reloadMock = function() {};
		spyOn(scope, 'reloadMock');
	  
      	$httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/point_of_interest').respond([poi1, poi2 ,poi3]);
      	$httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId').respond(poi1);
      	$httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
      	ctrl = $controller('poiListController', {$scope: scope, $route:{reload: scope.reloadMock}});
	  	var hlep = $templateCache.get('app/views/pois/poiList.html');
	  	/*var hlep = angular.element(
			  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  	);*/
	  	template = $compile(hlep)(scope);
	  	scope.$digest();
    }));

    it('should create "pois" model with 1 poi for each of the pois received from the backend', inject(function($controller) {
      	$httpBackend.flush();
      	expect(scope.pois).toEqualData([poi1, poi2, poi3]);
    }));

    it('should correctly delete pois', inject(function($controller, _$httpBackend_) {
      	$httpBackend.flush();
      	_$httpBackend_.expect('DELETE','https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId').respond(poi1);
      	scope.delete(scope.pois[0]);
	  	_$httpBackend_.flush();
    }));
	
	it('should correctly reload when deletion fails', inject(function($controller, _$httpBackend_) {
      	$httpBackend.flush();
      	_$httpBackend_.expect('DELETE','https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId').respond(666);
      	scope.delete(scope.pois[0]);
	  	_$httpBackend_.flush();
		expect(scope.reloadMock).toHaveBeenCalled();
    }));
	
	it('should define some links', function() {
		scope.edit('poiId');
		scope.goto('poiId');
		scope.addNew();
	});
  });
  
  describe('poiShowController', function() {
    	var scope, ctrl, $httpBackend, poi, template;

    	beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
      		$httpBackend = _$httpBackend_;

      		$routeParams.userId = 'userId';
      		$routeParams.poiId = 'poiId';
      		scope = $rootScope.$new();
      		ctrl = $controller('poiShowController', {$scope: scope, $route:{}});
	  
	  		poi={"id":"poiId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId"}],"address":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487232,"lon":3.7241699999999582},"postal_code":9000},"name":"test","radius":15000,"active":true,"notify":{"email":false,"cell_number":false},"notify_for_event_types":[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}]};
	  
      		$httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId').respond(poi);
      		$httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  		var hlep = $templateCache.get('app/views/pois/poiShow.html');
	  		/*var hlep = angular.element(
			  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  		);*/
	  		template = $compile(hlep)(scope);
	  		scope.$digest();
    	}));

    	it('should correctly create a poi model using a poi retrieved from the backend', inject(function() {
			$httpBackend.flush();
    		expect(scope.poi).toEqualData(poi);
    	}));
	
		it('should define some links', function() {
			scope.edit();
			scope.goBack();
		});
	
		it('should correctly delete the poi', function() {
			$httpBackend.flush();
			$httpBackend.expect('DELETE','https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId').respond(200);
			scope.delete();
			$httpBackend.flush();
		});
  });
  
  describe('poiEditController', function() {
    var scope, ctrl, $httpBackend, poi, template, eventTypes;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      $routeParams.poiId = 'poiId';
      scope = $rootScope.$new();
      ctrl = $controller('poiEditController', {$scope: scope, $route:{}});
	  
	  poi={"id":"poiId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId"}],"address":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487232,"lon":3.7241699999999582},"postal_code":9000},"name":"test","radius":15000,"active":true,"notify":{"email":false,"cell_number":false},"notify_for_event_types":[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}]};
	  eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId').respond(poi);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
	  var hlep = $templateCache.get('app/views/pois/poiEdit.html');
	  /*var hlep = angular.element(
		  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  );*/
	  template = $compile(hlep)(scope);
	  scope.$digest();
    }));

    it('should correctly create a poi model using a poi retrieved from the backend', inject(function() {
		$httpBackend.flush();
    	expect(scope.poi).toEqualData(poi);
    }));
	
	it('should correctly edit the poi', inject(function($timeout) {
		$httpBackend.flush();
		var place = {address_components:[{"long_name":"Burgstraat","short_name":"Burgstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],adr_address:"<span class=\"street-address\">Burgstraat</span>, <span class=\"postal-code\">9000</span> <span class=\"locality\">Gent</span>, <span class=\"country-name\">Belgium</span>",formatted_address:"Burgstraat, 9000 Gent, Belgium",geometry:{"location":{"lat":51.0563394,"lng":3.7174691999999823}},icon:"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png","id":"3e9a38e52a4ccf37fc1652c167448920cf9c19c1","name":"Burgstraat","place_id":"ChIJlWQOakBxw0cR_X2rfJAHHhQ",reference:"CoQBewAAABaTw0ExeLZ21dIzl7Or4znkWGYNDCaDYefmBKjEOiWj_EjPYviASxOntdQHDufJpM1MI4zypaSKLVukStJn_NDyWlWngTD3qweHgjFH1Xg8EjWlksFIxb_wMLyaDsszphRxay_t6wFNkeyeZiz1HCva03QNpO3eYiPLUC7k7z3MEhCzK6abnmVALQQnWsqu_VTLGhQSzOgGU2B9I6WU9kDXU6fb3VqVWQ",scope:"GOOGLE",types:["route"],url:"https://maps.google.com/?q=Burgstraat,+9000+Gent,+Belgium&ftid=0x47c371406a0e6495:0x141e07907cab7dfd","vicinity":"Gent","html_attributions":[]};
		spyOn(scope.pointAutocomplete, 'getPlace').and.returnValue(place);
		spyOn(place.geometry.location, 'lat').and.returnValue(51.0563394);
		spyOn(place.geometry.location, 'lng').and.returnValue(3.7174691999999823);
		scope.addPoint();
		
		scope.poi.radius=10000;
		scope.addNewCircle();
		
		scope.submit(); //This doesn't actually do anything in the context of unit tests, but we're just making sure it doen't cause any errors
		$timeout.flush();
		
		scope.checking();
		scope.selectedAll = true;
		scope.selectAll();
		expect(scope.tempHolder).toEqualData(scope.eventTypes);
		scope.selectedAll = false;
		scope.selectAll();
		angular.forEach(scope.tempHolder, function(el) {
			expect(el).toEqual(false);
		});
		scope.selectedAll = true;
		scope.checking();
		expect(scope.selectedAll).toBe(false);
		
		var results = [{"address_components":[{"long_name":"38","short_name":"38","types":["street_number"]},{"long_name":"Bonifantenstraat","short_name":"Bonifantenstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],"formatted_address":"Bonifantenstraat 38, 9000 Gent, Belgium","geometry":{"location":{"lat":51.0564067,"lng":3.7173033999999916},"location_type":"ROOFTOP","viewport":{"south":51.05505771970849,"west":3.7159544197085097,"north":51.05775568029149,"east":3.7186523802914735}},"place_id":"ChIJqXeFQkBxw0cR2LMD-MHK1hY","types":["street_address"]}];
		var status="NOT OK";
		
		var geocode = function(location, callback) {
				callback(results, status);
		};
		
		spyOn(scope.geocoder, 'geocode').and.callFake(geocode);
		var al = spyOn(window, 'alert');
		scope.save();
		expect(window.alert).toHaveBeenCalledWith('Geocoder failed due to: NOT OK');
		
		status=google.maps.GeocoderStatus.OK;
		$httpBackend.expect('PUT','https://vopro7.ugent.be/api/user/userId/point_of_interest/poiId').respond(200);
		scope.save();
		$httpBackend.flush();
		
		results=[];
		scope.save();
		expect(window.alert).toHaveBeenCalledWith('No results found');
    }));

    it('should define a link to return to the poi list page', inject(function() {
		scope.cancel();
    }));
  });
  
  describe('poiAddController', function() {
    var scope, ctrl, $httpBackend, poi, eventTypes, template;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile, $templateCache) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      scope = $rootScope.$new();
      ctrl = $controller('poiAddController', {$scope: scope, $route:{}});
	  
	  poi={"address":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487232,"lon":3.7241699999999582},"postal_code":9000},"name":"test","radius":15000,"active":true,"notify":{"email":false,"cell_number":false},"notify_for_event_types":[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}]};
	  eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
	  var hlep = $templateCache.get('app/views/pois/poiAdd.html');
	  /*var hlep = angular.element(
		  '<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
	  );*/
	  template = $compile(hlep)(scope);
	  scope.$digest();
    }));

    it('should correctly add pois', inject(function($timeout) {
		$httpBackend.flush();
		var place = {address_components:[{"long_name":"Burgstraat","short_name":"Burgstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],adr_address:"<span class=\"street-address\">Burgstraat</span>, <span class=\"postal-code\">9000</span> <span class=\"locality\">Gent</span>, <span class=\"country-name\">Belgium</span>",formatted_address:"Burgstraat, 9000 Gent, Belgium",geometry:{"location":{"lat":51.0563394,"lng":3.7174691999999823}},icon:"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png","id":"3e9a38e52a4ccf37fc1652c167448920cf9c19c1","name":"Burgstraat","place_id":"ChIJlWQOakBxw0cR_X2rfJAHHhQ",reference:"CoQBewAAABaTw0ExeLZ21dIzl7Or4znkWGYNDCaDYefmBKjEOiWj_EjPYviASxOntdQHDufJpM1MI4zypaSKLVukStJn_NDyWlWngTD3qweHgjFH1Xg8EjWlksFIxb_wMLyaDsszphRxay_t6wFNkeyeZiz1HCva03QNpO3eYiPLUC7k7z3MEhCzK6abnmVALQQnWsqu_VTLGhQSzOgGU2B9I6WU9kDXU6fb3VqVWQ",scope:"GOOGLE",types:["route"],url:"https://maps.google.com/?q=Burgstraat,+9000+Gent,+Belgium&ftid=0x47c371406a0e6495:0x141e07907cab7dfd","vicinity":"Gent","html_attributions":[]};
		spyOn(scope.pointAutocomplete, 'getPlace').and.returnValue(place);
		spyOn(place.geometry.location, 'lat').and.returnValue(51.0563394);
		spyOn(place.geometry.location, 'lng').and.returnValue(3.7174691999999823);
		scope.addPoint();
		
		scope.submit(); //This doesn't actually do anything in the context of unit tests, but we're just making sure it doen't cause any errors
		$timeout.flush();
		
		scope.poi.radius=10000;
		scope.addNewCircle();
		
		scope.checking();
		scope.selectedAll = true;
		scope.selectAll();
		expect(scope.tempHolder).toEqualData(scope.eventTypes);
		scope.selectedAll = false;
		scope.selectAll();
		angular.forEach(scope.tempHolder, function(el) {
			expect(el).toEqual(false);
		});
		
		var results = [{"address_components":[{"long_name":"38","short_name":"38","types":["street_number"]},{"long_name":"Bonifantenstraat","short_name":"Bonifantenstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],"formatted_address":"Bonifantenstraat 38, 9000 Gent, Belgium","geometry":{"location":{"lat":51.0564067,"lng":3.7173033999999916},"location_type":"ROOFTOP","viewport":{"south":51.05505771970849,"west":3.7159544197085097,"north":51.05775568029149,"east":3.7186523802914735}},"place_id":"ChIJqXeFQkBxw0cR2LMD-MHK1hY","types":["street_address"]}];
		var status="NOT OK";
		
		var geocode = function(location, callback) {
				callback(results, status);
		};
		
		spyOn(scope.geocoder, 'geocode').and.callFake(geocode);
		var al = spyOn(window, 'alert');
		scope.save();
		expect(window.alert).toHaveBeenCalledWith('Geocoder failed due to: NOT OK');
		
		status=google.maps.GeocoderStatus.OK;
		$httpBackend.expect('POST','https://vopro7.ugent.be/api/user/userId/point_of_interest').respond(200);
		scope.save();
		$httpBackend.flush(); //successful add
		
		results=[];
		scope.save();
		expect(window.alert).toHaveBeenCalledWith('No results found');
    }));
	
	it('should define a link to return to the list of pois', function() {
		scope.cancel();
	});
  });
});