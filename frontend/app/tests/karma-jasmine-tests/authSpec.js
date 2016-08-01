  describe('authentication/authorization', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, user1, user2, user3, user4;

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
	
	describe('403Controller', function() {
		it('should exist', inject(function($controller, $rootScope) {
			var scope = $rootScope.$new();
			ctrl = $controller('403Controller', {$scope: scope});
			scope.logIn(); //Not sure if this is supposed to be here
		}));
	});

	describe('Authentication', function() {
	  beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, Session) {
		$httpBackend = _$httpBackend_;
  
		scope = $rootScope.$new();
		ctrl = $controller('authenticationController', {
		  $scope: scope
		});
	  }));
  
  	  describe('Login', function() {
		it('should correctly create a new session when the login button is pressed and the credentials are correct', inject(function(Session, $rootScope) {
			var refreshToken = {token:'refresh_token',user_id:'userId',user_url:'/user/userId',role:"ROLE_USER"};
			var accessToken = {token:'access_token'}
			var refreshHeaders = {token:'refresh_token'};
			var session;
			scope.credentials = {email: "email@email.email", password: "password"};
		  
		  	$httpBackend.when('POST','https://vopro7.ugent.be/api/refresh_token/logout').respond('');
			if(Session.isOpen()) {
				Session.closeSession();
				$httpBackend.flush();
			}
			
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/refresh_token/regular', scope.credentials).respond(refreshToken);
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/access_token', {token:'refresh_token'}).respond(accessToken);
			scope.logIn();
			$httpBackend.flush();
	
			session = {
				userToken : Session.getUserToken(),
				userId : Session.getUserId(),
				userRole : Session.getUserRole(),
				userUrl : Session.getUserUrl(),
				access : Session.getAccessToken()
		  	};
		  
		  	expectedSession = {
				userToken:refreshToken.token,
				userId:refreshToken.user_id,
				userRole:'0',
				userUrl:refreshToken.user_url,
				access:accessToken.token
		  	};
		  
		  	expect(session).toEqualData(expectedSession);
		}));
	
		it('should correctly raise an alert when the login button is pressed and the credentials are incorrect', inject(function(Session) {
		  	var refreshToken = {token:'refresh_token',user_id:'userId',user_url:'/user/userId',role:"user"};
		  	var accessToken = {token:'access_token'}
		  	var refreshHeaders = {Accept:'application/json, text/plain, */*', Authorization:'refresh_token', 'Content-Type':'application/json;charset=utf-8'};
		  	var session;
		  	scope.credentials = {email: "email@email.email", password: "password"};
		  
		  	$httpBackend.when('POST','https://vopro7.ugent.be/api/refresh_token/logout').respond('');
			if(Session.isOpen()) {
				Session.closeSession();
				$httpBackend.flush();
			}
		  
		  	spyOn(window, 'alert');
		  
		  	$httpBackend.expect('POST','https://vopro7.ugent.be/api/refresh_token/regular', scope.credentials).respond(401);
	
		  	scope.logIn(scope.credentials);
		  	$httpBackend.flush();
		  
		  	expect(scope.notCompleteMessage).toEqual("Uw wachtwoord en/of email is verkeerd en/of bestaat niet.");
		}));
		
		
		it('should correctly raise an alert when the login button is pressed and the credentials are incomplete', inject(function($rootScope) {
			scope.credentials={password:'password'};
		  	scope.logIn();
		  	$rootScope.$apply();
		  	expect(scope.notCompleteMessage).toEqual("Gelieve een geldig wachtwoord en email in te geven");
		}));
		
		it('should correctly refresh the access token when access to the backend is refused, if there is an existing session', inject(function(Session, $http, $cookies) {
		  	var refreshToken = {token:'refresh_token',user_id:'userId',user_url:'https://vopro7.ugent.be/api/user/userId',role:"user"};
			var newAccessToken = {token:'new_access_token'};
		  	var originalHeaders = {Accept:'application/json, text/plain, */*'};
		  	var newHeaders = {Accept:'application/json, text/plain, */*', 'X-token':'new_access_token'};
			$cookies.put(Session.key_token,'refresh_token');
			$cookies.remove(Session.key_access_token);
			Session.state = Session.STATES.OPEN;
		  
		  	$httpBackend.expect('GET','https://vopro7.ugent.be/api/user/userId', undefined, originalHeaders).respond(403,''); //initial request
		  	$httpBackend.expect('POST','https://vopro7.ugent.be/api/access_token', {token:refreshToken.token}).respond(newAccessToken); //request to refresh access token
		  	$httpBackend.expect('GET','https://vopro7.ugent.be/api/user/userId', undefined, newHeaders).respond({id:'userId'}); //second request, using new access token
	
		  	$http.get('https://vopro7.ugent.be/api/user/userId');
		  	$httpBackend.flush();
		  
		  	expect(Session.getAccessToken()).toEqualData('new_access_token');
		}));
		
		it('should define a link as well as reset the validator when the validation error message is closed', function() { //re: the latter requirement: scope.alert() is called when the alert is closed
			scope.forgotPass();
			scope.alert();
		});
	  });
	  
	  describe('Logout', function() {
		
		it('should correctly log out if a session exists', inject(function($cookies, Session, $rootScope) {
		  	var refreshToken = {token:'refresh_token',user_id:'userId',user_url:'https://vopro7.ugent.be/api/user/userId',role:"user"};
			var newAccessToken = {token:'new_access_token'};
		  	var originalHeaders = {Accept:'application/json, text/plain, */*', 'Access-Control-Allow-Origin':'*'};
		  	var newHeaders = {Accept:'application/json, text/plain, */*', 'X-token':'new_access_token', 'Access-Control-Allow-Origin':'*'};
			var ret;
			
			$cookies.put(Session.key_token,'refresh_token');
			$cookies.put(Session.key_access_token,'access_token');
		  	$httpBackend.expect('POST','https://vopro7.ugent.be/api/refresh_token/logout').respond(refreshToken);
			
		  	Session.closeSession().then(function() {
				expect(true).toBe(true);;
			}, function() {
				expect(false).toBe(false);
			});
			
		  	$httpBackend.flush();
		}));
		
		it('should reject logout if no session exists',inject(function(Session) {
		  Session.closeSession().then(function()  {
			  done(new Error('Promise should be rejected, but was resolved.'));
		  }, function(reason) {
			  expect(reason).toEqual('No user is logged in!');
		  });
		}));
	  });
	});
	
	describe('Authorization', function() {
	  beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, Session, Authentication, Roles) {
		$httpBackend = _$httpBackend_;
  
		scope = $rootScope.$new();
	  }));
	  
	  it('should pass permission check when user is authorized', inject(function(Authentication, Authorization, Roles, $cookies, Session, $rootScope) {
		$cookies.put(Session.key_token,'refresh_token');
		Session.state = Session.STATES.OPEN;
	  	$cookies.put(Session.key_role, 0);
		Authorization.permissionCheck(Roles.getUser()).then(function(response) {
			expect(true).toBe(true);
		}, function(reason) {
			expect(false).toBe(true);
		}); 
		$rootScope.$apply();
	  }));
	  
	  it('should fail permission check when user is not authorized', inject(function(Authentication, Authorization, Roles, $cookies, Session, $rootScope) {
		$cookies.put(Session.key_token,'refresh_token');
		Session.state = Session.STATES.OPEN;
	  	$cookies.put(Session.key_role, 0);
		Authorization.permissionCheck(Roles.getOperator()).then(function(response) {
			expect(false).toBe(true);
		}, function(reason) {
			expect(true).toBe(true);
		}); 
		$rootScope.$apply();
	  }));
	  
	  it('should fail permission check when user is not logged in', inject(function(Authentication, Authorization, Roles, $cookies, Session, $rootScope) {
		$cookies.remove(Session.key_token);
		Session.state = Session.STATES.CLOSED;
	  	$cookies.put(Session.key_role, 0);
		Authorization.permissionCheck(Roles.getUser()).then(function(response) {
			expect(false).toBe(true);
		}, function(reason) {
			expect(true).toBe(true);
		}); 
		$rootScope.$apply();
	  }));
	});
  });