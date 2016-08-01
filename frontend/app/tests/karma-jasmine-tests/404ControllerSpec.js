describe('404Controller', function() {
    var scope, ctrl, RESTdata, modelData, user1, user2, user3, user4;

    beforeEach(module('vopApp'));
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
	beforeEach(inject(function($rootScope) {
		scope = $rootScope.$new();
	}));
	  
	it('should correctly display a link to the homepage when the user is not logged in', inject(function($rootScope, $cookies, Session, $controller) {
		Session.state = Session.STATES.CLOSED;
		ctrl = $controller('404Controller', {
			$scope: scope
		});
		expect(scope.name_nxt).toEqual("de homepagina");
	}));
	
	it('should correctly display a link to the user\'s events when logged in', inject(function($rootScope, $cookies, Session, $controller) {
		Session.state = Session.STATES.OPEN;
		ctrl = $controller('404Controller', {
			$scope: scope
		});
		expect(scope.name_nxt).toEqual("mijn meldingen");
	}));
});