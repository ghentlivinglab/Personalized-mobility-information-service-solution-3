/*describe('directives', function() {
    var scope, template;

    beforeEach(module('routeDirectives'));
    
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

    beforeEach(inject(function($rootScope, $controller) {
      scope = $rootScope.$new();
      scope.a = "a";
      scope.b = "b";
      scope.c = "c";
    }));

    it('should correctly add selected elements to the given array', inject(function($controller, $compile) {
      var element = angular.element(
        '<form name="testForm">' +
          '<input type="checkbox" ng-checked="true" name="abox" ng-model="dummy" checkboxes array="array" el="a"/>' +
          '<input type="checkbox" name="bbox" ng-model="dummy" checkboxes array="array" el="b"/>' +
          '<input type="checkbox" name="cbox" ng-model="dummy" checkboxes array="array" el="c"/>' +
        '</form>'
      );
      scope.array=[];
      template = $compile(element)(scope);
      template.find("input").triggerHandler("click");
      scope.$digest();
      expect(scope.array).toEqualData(["a"]);
    }));

    it('should correctly remove elements when their respective checkboxes are deselected', inject(function($controller, $compile) {
      var element = angular.element(
        '<form name="testForm">' +
          '<input type="checkbox" name="abox" ng-model="dummy" checkboxes array="array" el="a"/>' +
          '<input type="checkbox" name="bbox" ng-model="dummy" checkboxes array="array" el="b"/>' +
          '<input type="checkbox" name="cbox" ng-model="dummy" checkboxes array="array" el="c"/>' +
        '</form>'
      );
      template = $compile(element)(scope);
      scope.array=["a"];
      template.find("input").attr("ng-checked","false");
      template.find("input").triggerHandler("click");
      scope.$digest();
      expect(scope.array).toEqualData([]);
    }));
  });*/