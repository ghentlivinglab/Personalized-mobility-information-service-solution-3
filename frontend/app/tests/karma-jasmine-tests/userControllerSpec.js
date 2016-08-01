describe('User Controllers', function () {
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

  describe('userListController', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, user1, user2, user3, user4;

    user1 = {"id":"userId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/1"}],"password":null,"email":"Liam.Vermeir1@hotmail.com","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32498047339","mute_notifications":true,"points_of_interest":null};
    user2 = {"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2"}],"password":null,"email":"Liam.Vermeir2@skynet.be","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32478951761","mute_notifications":false,"points_of_interest":null};
    user3 = {"id":"3","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/3"}],"password":null,"email":"Liam.Vermeir3@ugent.be","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32471575473","mute_notifications":true,"points_of_interest":null};
    user4 = {"id":"4","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/4"}],"password":null,"email":"Liam.Vermeir4@telenet.be","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32478637353","mute_notifications":false,"points_of_interest":null};

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
      $httpBackend = _$httpBackend_;
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user').respond([user1, user2, user3, user4]);

      $routeParams.userId = 'userId';
      scope = $rootScope.$new();
      ctrl = $controller('userListController', {$scope: scope, $route:{reload: function() {}}});
    }));

    it('should create "userList" model with 1 user for each of the users received from the backend', inject(function($controller) {
      $httpBackend.flush();
      expect(scope.users).toEqualData([user1, user2, user3, user4]);
    }));

    it('should correctly delete users by ID', inject(function($controller, _$httpBackend_) {
      $httpBackend.flush();
      _$httpBackend_.expect('DELETE','https://vopro7.ugent.be/api/user/userId').respond(scope.users[0]);
      scope.deleteUserFunction(scope.users[0]);
	  scope.yes();
	  _$httpBackend_.flush();
    }));
  });

  describe('userShowController', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, user;

    user = {"id":"userId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/1"}],"password":null,"email":"Liam.Vermeir1@hotmail.com","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32498047339","mute_notifications":true,"points_of_interest":null};

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
      $httpBackend = _$httpBackend_;
      $httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId').respond(user);

      $routeParams.id = 'userId';
      scope = $rootScope.$new();
      ctrl = $controller('userShowController', {$scope: scope});
    }));

    it('should create "user" model using the user received from the backend', inject(function($controller) {
      $httpBackend.flush();
      expect(scope.user).toEqualData(user);
    }));
	
	it('should download a user dump when an admin requests it', inject(function($controller) {
		//Not sure as to what expectations I should set here
    	$httpBackend.expect('POST','https://vopro7.ugent.be/api/admin/data_dump/').respond([{test:'test'}]);
		scope.downloadDatadump();
     	$httpBackend.flush();
     	expect(scope.user).toEqualData(user);
    }));
  });
  
	describe('userEditController', function() {
		var scope, ctrl, $httpBackend, RESTdata, modelData, user;
	
		user = {"id":"userId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/1"}],"password":null,"email":"Liam.Vermeir1@hotmail.com","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32498047339","mute_notifications":true,"points_of_interest":null};
	
		beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
			$httpBackend = _$httpBackend_;
			$httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId').respond(user);
	  
			$routeParams.id = 'userId';
			scope = $rootScope.$new();
			ctrl = $controller('userEditController', {$scope: scope});
		}));
	
		it('should create "user" model using the user received from the backend', inject(function($controller) {
			$httpBackend.flush();
			expect(scope.user).toEqualData(user);
		}));
		
		it("should correctly make a PUT request to the user endpoint when the edit button is clicked and the input is valid", inject(function($controller) {
			  $httpBackend.flush();
			  scope.user.password='password';
			  scope.user.confirmPassword=scope.user.password;
			  $httpBackend.expect('PUT','https://vopro7.ugent.be/api/user/userId').respond(RESTdata);
			  scope.editUserFunction();
			  $httpBackend.flush();
		}));
		
		it("should display an error message when the edit button is pressed and either name contains invalid characters", inject(function($controller) {
			  $httpBackend.flush();
			  scope.user.first_name='*¨%£µù*%';
			  scope.editUserFunction();
			  expect(scope.notCompleteMessage).toEqual("De volgende tekens mogen niet in een voor- of achternaam voorkomen: ^ + # , * \" < > ; = [ ] : ? / | ' % / \\ $ ¨ ^ 0 1 2 3 4 5 6 7 8 9 ( ) § ! { } ° _ £ € ² ³ ~ ");
		}));
		
		it("should display an error message when the edit button is pressed and the given cellphone number is invalid", inject(function($controller) {
			  $httpBackend.flush();
			  scope.user.cell_number='*¨%£µù*%';
			  scope.editUserFunction();
			  expect(scope.notCompleteMessage).toEqual("Gelieve een geldig gsm nummer op te geven.");
			  scope.alert();
		}));
	});
  
  	describe('userChangePassController', function() {
    	var scope, ctrl, user;

    	beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, HOSTNAME) {
    		user = {"id":"userId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/userId"}],"password":null,"email":"Liam.Vermeir1@hotmail.com","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32498047339","mute_notifications":true,"points_of_interest":null};
      		$httpBackend = _$httpBackend_;
      		$httpBackend.when('GET','https://vopro7.ugent.be/api/user/userId').respond(user);

			$routeParams.id = 'userId';

      		scope = $rootScope.$new();
      		ctrl = $controller('userChangePassController', {$scope: scope});
    	}));
	
		it("should make a POST request to the change_password endpoint when the input is valid", inject(function($controller, HOSTNAME) {
			scope.userPassword.new_password = 'password'
			scope.confirmPassword = 'password'
			$httpBackend.expect('POST', HOSTNAME.URL+'user/userId/modify_password').respond(200);
			
      		scope.save();
			$httpBackend.flush();
    	}));
	
		it("should make a POST request to the change_password endpoint when the input is valid, then display an error message if the old password is incorrect", inject(function($controller, HOSTNAME) {
			scope.userPassword.new_password = 'password'
			scope.confirmPassword = 'password'
			$httpBackend.expect('POST', HOSTNAME.URL+'user/userId/modify_password').respond(777);
			
      		scope.save();
			$httpBackend.flush();
			expect(scope.notCompleteMessage).toEqual('Uw oude wachtwoord is niet correct.');
			scope.alert(); //Tells the controller that the alert no longer needs to be displayed
    	}));
	
		it("should display an error message when the edit button is pressed and the passwords aren't equal", inject(function($controller) {
			scope.userPassword.new_password = 'password'
			scope.confirmPassword = 'notPassword'
		
      		scope.save();
			expect(scope.notCompleteMessage).toEqual('Gelieve 2 identieke wachtwoorden in te geven.');
    	}));
	
		it('should define a link', function() {
			scope.goBack();
		});
  });
  
  describe('userForgotPasswordController', function() {
    var scope, ctrl;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, HOSTNAME) {
      $httpBackend = _$httpBackend_;

      scope = $rootScope.$new();
      ctrl = $controller('userForgotPasswordController', {$scope: scope});
    }));
	
	it("should make a POST request to the forgot_password endpoint when the input is valid", inject(function($controller, HOSTNAME) {
		$httpBackend.expect('POST', HOSTNAME.URL+'/user/forgot_password_request?save=%7B%22method%22:%22POST%22%7D').respond(200);
		
		scope.userForgotPassword.email='email@email.email';

		scope.save();
		$httpBackend.flush();
		expect(scope.successMessage).toEqual('Uw verzoek is verwerkt, gelieve uw mail te bekijken voor de heractiveringscode.');
	}));
	
	it("should display an error message when the input is invalid", inject(function($controller, HOSTNAME) {
		
		scope.userForgotPassword.email='565465464';

		scope.save();
		expect(scope.notCompleteMessage).toEqual('Gelieve een geldig email op te geven.');
	}));
  });
  
  describe('userForgotPasswordRecoverController', function() {
    var scope, ctrl, refreshToken, accessToken;

	refreshToken = {token:'refresh_token',user_id:'userId',user_url:'/user/userId',role:"ROLE_USER"};
	accessToken = {token:'access_token'};
	
    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, HOSTNAME) {
      	$httpBackend = _$httpBackend_;
		
		$routeParams.mail='email';
		$routeParams.code='code'
		
      	scope = $rootScope.$new();
      	ctrl = $controller('userForgotPasswordRecoverController', {$scope: scope});
    }));
	
	it("should make a POST request to the forgot_password endpoint (when the given passwords are equal), then log in", inject(function($controller, HOSTNAME) {
		$httpBackend.expect('POST',HOSTNAME.URL+"user/forgot_password?password_token=code", {password:'password', email:'email'}).respond(200);
		$httpBackend.expect('POST','https://vopro7.ugent.be/api/refresh_token/regular').respond(refreshToken);
		$httpBackend.when('POST','https://vopro7.ugent.be/api/access_token', {token:'refresh_token'}).respond(accessToken);
		
		scope.password='password';
		scope.confirmPassword='password';
		
		scope.recover();
		$httpBackend.flush();
	}));
	
	it("should display an error message when password recovery fails because the server returns an error", inject(function($controller, HOSTNAME) {
		$httpBackend.expect('POST',HOSTNAME.URL+"user/forgot_password?password_token=code", {password:'password', email:'email'}).respond(401);
		
		scope.password='password';
		scope.confirmPassword='password';
		
		scope.recover();
		$httpBackend.flush();
		expect(scope.notCompleteMessage).toEqual('Iets is fout gegaan tijdens de verwerking van uw verzoek.');
		scope.alert();
	}));
	
	it("should display an error message when the passwords are different", inject(function($controller, HOSTNAME) {
		scope.password='password';
		scope.confirmPassword='notPassword';
		
		scope.recover();
		expect(scope.notCompleteMessage).toEqual('Gelieve, 2 identieke wachtwoorden op te geven.');
		scope.alert();
	}));
  });


  describe('userAddController', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, user;

    user = {"id":"userId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/1"}],"password":null,"email":"Liam.Vermeir1@hotmail.com","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32498047339","mute_notifications":true,"points_of_interest":null};
    
    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile) {
      scope = $rootScope.$new();
      $httpBackend = _$httpBackend_;

      ctrl = $controller('userAddController', {$scope: scope});
    }));
	
	describe('successful validation', function() {
		var refreshToken, accessToken;
		
		beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, $compile) {
			refreshToken = {token:'refresh_token',user_id:'userId',user_url:'/user/userId',role:"ROLE_USER"};
			accessToken = {token:'access_token'};
			scope.user.first_name='test';
			scope.user.last_name='test';
			scope.user.cell_number='0000000000';
			scope.user.email='email@email.email';
			scope.user.password='password';
			scope.confirmPassword='password';
			scope.notChecked = true;
		}));
	
		it('should validate, send a POST request and log in when passwords are equal and all fields are correctly filled in', inject(function($controller) {
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user').respond(scope.user);
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/refresh_token/regular').respond(refreshToken);
			$httpBackend.when('POST','https://vopro7.ugent.be/api/access_token', {token:'refresh_token'}).respond(accessToken);
			scope.addUserFunction();
			$httpBackend.flush();
			expect(scope.notCompleteMessage).toEqual('');
		}));
	
		it('should validate, send a POST request and display an error message when passwords are equal and all fields are correctly filled in but login fails', inject(function($controller) {
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user').respond(scope.user);
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/refresh_token/regular').respond(777);
			scope.addUserFunction();
			$httpBackend.flush();
			expect(scope.notCompleteMessage).toEqual('Iets is verkeerd gegaan tijdens het automatisch inloggen met uw profiel. Gelieve later proberen in te loggen met uw nieuw profiel.');
		}));
	
		it('should validate and display an error message when the fields are correctly filled in but the terms of service haven\'t been agreed to', inject(function($controller) {
			scope.notChecked = false;
			scope.addUserFunction();
			expect(scope.notCompleteMessage).toEqual('Zonder u akkoord te verklaren met onze terms of service, kunnen wij u onze diensten niet aanbieden.');
		}));
	
		it('should validate, send a POST request when passwords are equal and all fields are correctly filled in but the email is already in use or invalid', inject(function($controller) {
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user').respond(409);
			scope.addUserFunction();
			$httpBackend.flush();
			expect(scope.notCompleteMessage).toEqual('Dit email-account is al in gebruik of is niet geldig.');
		}));
	
		it('should validate, send a POST request when passwords are equal and all fields are correctly filled in but an error occurs while processing the POST request', inject(function($controller) {
			$httpBackend.expect('POST','https://vopro7.ugent.be/api/user').respond(777);
			scope.addUserFunction();
			$httpBackend.flush();
			expect(scope.notCompleteMessage).toEqual('Iets is verkeerd gegaan tijdens de registratie van uw account. De server kon niet bereikt worden. Gelieve uw internetverbinding nog eens na te kijken.');
		}));
	});
	
	describe('unsuccessful validation', function() {
		it("shouldn't validate when passwords are different (but all fields are filled in) and display the appropriate message", inject(function($controller) {
			scope.user.email='email@email.email';
			scope.user.password='password';
			scope.user.confirmPassword='notPassword';
			scope.notChecked = false;
			scope.addUserFunction();
			expect(scope.notCompleteMessage).toEqual('Gelieve 2 identieke wachtwoorden in te geven.');
		}));
		
		it("shouldn't validate not all fields are filled in", inject(function($controller) {
			scope.user.password='password';
			scope.confirmPassword='password';
			scope.notchecked=false;
			scope.addUserFunction();
			expect(scope.notCompleteMessage).toEqual('Gelieve een geldig email op te geven.');
		}));
		
		it("should display an error message when the add button is pressed and the given cellphone number is invalid", inject(function($controller) {
			scope.user.first_name='test';
			scope.user.last_name='test';
			scope.user.cell_number='*¨%£µù*%';
			scope.addUserFunction();
			expect(scope.notCompleteMessage).toEqual("Gelieve een geldig gsm nummer op te geven.");
			scope.alert();
		}));
		
		it("should display an error message when the add button is pressed and the first or last name contains invalid characters", inject(function($controller) {
			scope.user.first_name='test';
			scope.user.last_name='*¨%£µù*%';
			scope.addUserFunction();
			expect(scope.notCompleteMessage).toEqual("De volgende tekens mogen niet in een voor- of achternaam voorkomen: ^ + # , * \" < > ; = [ ] : ? / | ' % / \\ $ ¨ ^ 0 1 2 3 4 5 6 7 8 9 ( ) § ! { } ° _ £ € ² ³ ~ ");
			
			scope.user.last_name='test';
			scope.user.first_name='*¨%£µù*%';
			scope.addUserFunction();
			expect(scope.notCompleteMessage).toEqual("De volgende tekens mogen niet in een voor- of achternaam voorkomen: ^ + # , * \" < > ; = [ ] : ? / | ' % / \\ $ ¨ ^ 0 1 2 3 4 5 6 7 8 9 ( ) § ! { } ° _ £ € ² ³ ~ ");
			scope.alert();
		}));
	});
  });
  
  describe('userEventsShowController', function() {
	var scope, ctrl, event1, event2, event3;

    event1 = {"id":"eventId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/eventId"}],"coordinates":{"lat":51.061436,"lon":3.680263},"active":true,"description":"Dit is een test event.","jams":[{"speed":26,"delay":2569,"start_node":{"lat":51.048344,"lon":3.758815},"end_node":{"lat":51.047207,"lon":3.773301}}],"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"1","type":"ROAD_CLOSED","subtype":"ROAD_CLOSED_EVENT","relevant_for_transportation_types":["bus","car","bike","streetcar"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
    event2 = {"id":"1","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/1"}],"coordinates":{"lat":51.079049,"lon":3.691705},"active":true,"description":"Dit is een test event.","jams":[{"speed":17,"delay":3015,"start_node":{"lat":51.031899,"lon":3.671271},"end_node":{"lat":51.027925,"lon":3.753934}}],"source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"3","type":"JAM","subtype":"JAM_MODERATE_TRAFFIC","relevant_for_transportation_types":["bus","car"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
    event3 = {"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/event/2"}],"coordinates":{"lat":51.024944,"lon":3.751568},"active":false,"description":"Dit is een test event.","source":{"name":"TestEvent","icon_url":"www.example.org/random_icon_url.png"},"type":{"id":"6","type":"DEFAULT","subtype":null,"relevant_for_transportation_types":["bus","car","bike","streetcar","train"]},"publication_time":"2016-03-07 10:11:29.077501","last_edit_time":"2016-03-07 10:11:29.077501"};
	
  
	beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, HOSTNAME, $templateCache, $compile) {
		$httpBackend = _$httpBackend_;
  
		$routeParams.userId='userId';
		
		scope = $rootScope.$new();
		ctrl = $controller('userEventsShowController', {$scope: scope});
		$httpBackend.expect('GET', HOSTNAME.URL+"event/?user_id=userId").respond([event1, event2, event3]);
		
		var hlep = $templateCache.get('app/views/users/userEventsShow.html');
		/*var hlep = angular.element(
			'<div name="testDiv" ng-include="views/travels/travelShow.html"></div>'
		);*/
		template = $compile(hlep)(scope);
		scope.$digest();
		
		$httpBackend.flush();
		
		var results = [{"address_components":[{"long_name":"38","short_name":"38","types":["street_number"]},{"long_name":"Bonifantenstraat","short_name":"Bonifantenstraat","types":["route"]},{"long_name":"Gent","short_name":"Gent","types":["locality","political"]},{"long_name":"Oost-Vlaanderen","short_name":"OV","types":["administrative_area_level_2","political"]},{"long_name":"Vlaanderen","short_name":"Vlaanderen","types":["administrative_area_level_1","political"]},{"long_name":"Belgium","short_name":"BE","types":["country","political"]},{"long_name":"9000","short_name":"9000","types":["postal_code"]}],"formatted_address":"Bonifantenstraat 38, 9000 Gent, Belgium","geometry":{"location":{"lat":51.0564067,"lng":3.7173033999999916},"location_type":"ROOFTOP","viewport":{"south":51.05505771970849,"west":3.7159544197085097,"north":51.05775568029149,"east":3.7186523802914735}},"place_id":"ChIJqXeFQkBxw0cR2LMD-MHK1hY","types":["street_address"]}];
		var status="NOT OK";
		
		var geocode = function(location, callback) {
				callback(results, status);
		};
		
		spyOn(scope.geocoder, 'geocode').and.callFake(geocode);
	}));
	
	it("should retrieve the user's events from the backends and create a model for them", inject(function($controller, HOSTNAME) {
		expect(scope.userEvents).toEqualData([event1, event2, event3]);
	}));
  });

  describe('userListAdminController', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, user1, user2, user3, user4;

    user1 = {"id":"userId","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/1"}],"password":null,"email":"Liam.Vermeir1@hotmail.com","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32498047339","mute_notifications":true,"points_of_interest":null};
    user2 = {"id":"2","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/2"}],"password":null,"email":"Liam.Vermeir2@skynet.be","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32478951761","mute_notifications":false,"points_of_interest":null};
    user3 = {"id":"3","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/3"}],"password":null,"email":"Liam.Vermeir3@ugent.be","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32471575473","mute_notifications":true,"points_of_interest":null};
    user4 = {"id":"4","links":[{"rel":"self","href":"https://vopro7.ugent.be/api/user/4"}],"password":null,"email":"Liam.Vermeir4@telenet.be","travels":null,"first_name":"Liam","last_name":"Vermeir","cell_number":"+32478637353","mute_notifications":false,"points_of_interest":null};

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, HOSTNAME) {
      $httpBackend = _$httpBackend_;
      $httpBackend.expect('GET',HOSTNAME.URL+'/admin/user').respond([user3, user4]);
      $httpBackend.expect('GET',HOSTNAME.URL+'user').respond([user1, user2]);
      $httpBackend.expect('GET',HOSTNAME.URL+'role').respond(['ROLE_USER','ROLE_ADMIN']);

      $routeParams.userId = 'userId';
      scope = $rootScope.$new();
      ctrl = $controller('userListAdminController', {$scope: scope, $route:{reload: function() {}}});
    }));

    it('should create "userList" model with 1 user for each of the users received from the backend', inject(function($controller) {
      $httpBackend.flush();
      expect(scope.users).toEqualData([user3, user4]);
    }));

    it('should correctly delete users by ID', inject(function($controller, _$httpBackend_, HOSTNAME) {
      $httpBackend.flush();
      $httpBackend.expect('DELETE',HOSTNAME.URL + 'user/userId').respond(scope.users[0]);
      scope.deleteUserFunction('userId');
	  scope.deleteUser();
	  $httpBackend.flush();
    }));

    it('should correctly change users\' roles', inject(function($controller, _$httpBackend_, HOSTNAME) {
      $httpBackend.flush();
      $httpBackend.expect('POST',HOSTNAME.URL + 'admin/permission/userId','"ROLE_ADMIN"').respond(200);
      scope.selectForRoleChange(user1);
	  scope.changeRole();
	  $httpBackend.flush();
    }));
  });

  describe('userMailValidationController', function() {
    var scope, ctrl, $httpBackend, RESTdata, modelData, user;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
      $httpBackend = _$httpBackend_;

      $routeParams.mail = 'email';
      $routeParams.code = 'code';
	  
      scope = $rootScope.$new();
      ctrl = $controller('userMailValidationController', {$scope: scope});
    }));

    it('should correctly complete email verification when the verification link is clicked', inject(function($controller, HOSTNAME) {
		$httpBackend.expect('POST',HOSTNAME.URL+"user/verify" , {email:'email', email_verification_pin:'code'}).respond(200);
      	$httpBackend.flush();
    }));

    it('should correctly display an error when the email verification link is clicked and the server returns an error', inject(function($controller, HOSTNAME) {
		$httpBackend.expect('POST',HOSTNAME.URL+"user/verify" , {email:'email', email_verification_pin:'code'}).respond(405);
      	$httpBackend.flush();
		expect(scope.notCompleteMessage).toEqual("De gebruikte link is vervallen of is al eens gebruikt.");
		expect(scope.notValid).toBe(true);
    }));
  });
});