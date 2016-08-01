describe('TravelListCtrl', function() {
  var scope, ctrl, $httpBackend, RESTdata;

  RESTdata=[{id:654, links:[], name:"hallo",route:{"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/route/2"}],"name":"LiamVermeir2","startpoint":{"id":"242","city":"Dendermonde","postal_code":"1317","street":"Babbelaarstraat","housenumber":"217","country":"Belgie","coordinate":{"lat":50.4711723,"lon":4.1318326}},"endpoint":{"id":"644","city":"Antwerpen","postal_code":"1262","street":"Achterstraat","housenumber":"800","country":"Belgie","coordinate":{"lat":50.817112,"lon":3.09416413}},"waypoints":[{"lat":50.7753296,"lon":3.16776896}],"transportation_types":["BICYCLE"],"notify_for_event_types":[{"id":"2","type":null,"subtype":null,"relevant_for_transportation_types":null}]},
			date:"",time_interval:["08:30","09:00"],is_arrival_time:true,recurring:[true,false,true,false,true,false,true]},
			{id:654, links:[], name:"hallo",route:{"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2/route/2"}],"name":"LiamVermeir2","startpoint":{"id":"242","city":"Dendermonde","postal_code":"1317","street":"Babbelaarstraat","housenumber":"217","country":"Belgie","coordinate":{"lat":50.4711723,"lon":4.1318326}},"endpoint":{"id":"644","city":"Antwerpen","postal_code":"1262","street":"Achterstraat","housenumber":"800","country":"Belgie","coordinate":{"lat":50.817112,"lon":3.09416413}},"waypoints":[{"lat":50.7753296,"lon":3.16776896}],"transportation_types":["BICYCLE"],"notify_for_event_types":[{"id":"2","type":null,"subtype":null,"relevant_for_transportation_types":null}]},
			date:"",time_interval:["08:30","09:00"],is_arrival_time:false,recurring:[true,false,true,false,true,false,true]}];

  beforeEach(module('project'));
  beforeEach(module('travelService'));
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

  beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
    $httpBackend = _$httpBackend_;
    $httpBackend.expectGET('/api/user/userId/travel').respond(RESTdata);

    $routeParams.userId = 'userId';
    scope = $rootScope.$new();
    ctrl = $controller('TravelListCtrl', {$scope: scope});
  }));

  it('should should create "travelList" model with 1 travel for each of the travels received from backend', inject(function($controller) {
    $httpBackend.flush();
    expect(scope.travelList).toEqualData(RESTdata);
  }));
});