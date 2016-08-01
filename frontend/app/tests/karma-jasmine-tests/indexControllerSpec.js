describe('indexController', function() {
    beforeEach(module('vopApp'));
	beforeEach(inject(function($rootScope) {
	  	scope = $rootScope.$new();
	}));
	  
	it('should define some links', inject(function($controller) {
	 	ctrl = $controller('indexController', {$scope:scope});
		scope.toContacts();
		scope.logIn();
		scope.register();
	}));
});