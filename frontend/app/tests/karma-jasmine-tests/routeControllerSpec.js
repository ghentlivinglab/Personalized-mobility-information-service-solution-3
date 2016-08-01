describe('Route Controllers', function() {
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


  describe('routeAddController', function() {
    var scope, ctrl, $httpBackend, transportationTypes, eventTypes;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
      	$httpBackend = _$httpBackend_;
      	scope = $rootScope.$new();
      	ctrl = $controller('routeAddController', {$scope: scope});
	  	
	  	eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  	//transportationTypes=["car","bus","bike","train","streetcar"];
    }));

    it('should create models for eventtypes using data received from their respective endpoints', inject(function($timeout) {
	 	$httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      	//$httpBackend.when('GET','https://vopro7.ugent.be/api/transportationtype').respond(transportationTypes);
      	$httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
		$httpBackend.flush();
		//$timeout.flush();
		expect(scope.entries).toEqualData(eventTypes);
		//expect(scope.transportationTypes).toEqualData(transportationTypes);
    }));
  });
  
  describe('routeEditController', function() {
    var scope, ctrl, $httpBackend, travel, route, transportationTypes, eventTypes;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $templateCache, $compile) {
      $httpBackend = _$httpBackend_;

      $routeParams.userId = 'userId';
      $routeParams.travelId = 'travelId';
      $routeParams.routeId = 'routeId';
      scope = $rootScope.$new();
      ctrl = $controller('routeEditController', {$scope: scope, $route:{reload: function() {}}});
	  
	  travel={"id":"travelId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/travel/travelId"}],"name":"help","recurring":[false,false,true,false,false,true,false],"startpoint":{"city":"Gent","street":"Ekkergemstraat","housenumber":"127-129","country":"BE","coordinates":{"lat":51.0514755,"lon":3.70652223},"postal_code":9000},"endpoint":{"city":"Gent","street":"Pollepelstraat","housenumber":"3","country":"BE","coordinates":{"lat":51.0487213,"lon":3.72416997},"postal_code":9000},"time_interval":["04:56","04:56"]};
	  route={"id":"routeId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId/travel/travelId/route/routeId"}],"notify":{"email":false,"cell_number":false},"active":true,"waypoints":[{"lat":51.0500755,"lon":3.71713352}],"transportation_type":"car","notify_for_event_types":[{"type":"Jam"},{"type":"Misc"}]};
	  eventTypes=[{"type":"Accident"},{"type":"Jam"},{"type":"Weatherhazard"},{"type":"Hazard"},{"type":"Misc"},{"type":"Construction"},{"type":"Road_closed"}];
	  transportationTypes=["car","bus","bike","train","streetcar"];
	  
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel/travelId').respond(travel);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId/travel/travelId/route/routeId').respond(route);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/eventtype').respond(eventTypes);
      $httpBackend.when('GET','https://vopro7.ugent.be/api/transportationtype').respond(transportationTypes);
      $httpBackend.when('POST','https://vopro7.ugent.be/api/access_token').respond({token:'token'});
	  
	  var hlep = $templateCache.get('app/views/routes/routeEdit.html');
		/*var hlep = angular.element(
			'<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
		);*/
		template = $compile(hlep)(scope);
		scope.$digest();
    }));

    it('should correctly retrieve a travel from the backend and display it', inject(function($timeout) {
		$httpBackend.flush();
		$timeout.flush();
		expect(scope.travel).toEqualData(travel);
    	expect(scope.route).toEqualData(route);
    }));
	
	it('should correctly update all of the required fields and send a PUT request to the "route" endpoint when the edit button is pressed', inject(function($timeout) {
		$httpBackend.flush();
		$timeout.flush();
		
		$httpBackend.expect('PUT', 'https://vopro7.ugent.be/api/user/userId/travel/travelId/route/routeId').respond(200);
		
		scope.overview_path = [{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352},{"lat":51.0500755,"lon":3.71713352}];
		scope.editRoute();
		$timeout.flush();
		$httpBackend.flush();
		
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
    }));
	
	it('should define a link', inject(function() {
		scope.goBack();
    }));
  });
});