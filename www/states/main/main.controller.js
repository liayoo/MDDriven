angular.module('main',[])

.controller('MainController', function($scope, $ionicSideMenuDelegate) {
  $scope.toggleLeftSideMenu = function() {
    $ionicSideMenuDelegate.toggleLeft();
  };
  
  /* change to this?
  $scope.toggleLeft = function() {
    $ionicSideMenuDelegate.toggleLeft();
  };
  */
})