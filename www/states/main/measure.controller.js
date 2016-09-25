angular.module('measure',[])

.controller('MeasureController', function($scope, $ionicSideMenuDelegate) {
	$scope.toggleLeftSideMenu = function() {
    	$ionicSideMenuDelegate.toggleLeft();
  	}
	$scope.getMeasurement = function() {

	};
})
