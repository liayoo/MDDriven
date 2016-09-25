angular.module('sidemenu',[])

.controller('SidemenuController', function($scope, $ionicSideMenuDelegate) {
  $scope.toggleLeftSideMenu = function() {
    $ionicSideMenuDelegate.toggleLeft();
  };

})