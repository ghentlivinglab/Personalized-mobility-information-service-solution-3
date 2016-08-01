describe('eventListTest', function () {
  var scope;
  beforeEach(module('myApp'));

  beforeEach(inject(function($controller, $rootScope){
    scope = $rootScope.$new();
    $controller('eventListController', { $scope: scope});
  }));

  describe('sum', function () {
        it('1 + 1 should equal 2', inject(function ($controller) {
            scope.x = 1;
            scope.y = 2;
            scope.sum();
            expect(scope.z).toBe(3);
        }));
    });

});
