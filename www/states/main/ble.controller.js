angular.module('ble.controllers', [])

.controller('BLEController', function($scope, $ionicPopup, Devices, BLE) {
	ble.isEnabled(
		function() { console.log("Bluetooth is enabled"); },
		function() { 
			$ionicPopup.alert({
				title: 'WARNING!',
				template: 'Bluetooth is not enabled'
			}); 
	});
	$scope.devices = Devices.all();

	var success = function() {
		if ($scope.devices.length < 1) { alert("Didn't find any BLE devices"); }
	};

	var failure = function(error) {
		alert(error);
	};

	$scope.onRefresh = function() {
		BLE.scan().then(
			success, failure
		).finally(
			function() {
				$scope.$broadcast('scroll.refreshComplete');
			}
		)
	}
	BLE.scan().then(success, failure);
})

.controller('BLEDetailController', function($scope, $stateParams, $log, $ionicPopup, BLE) {
	$scope.device = null;
	BLE.connect($stateParams.deviceId).then(
		function(peripheral) {
			$scope.device = peripheral;
	});

	writeToDevice = function(dest, val) {
		if ($scope.device == null) { return; }
		BLE.addQueue(BLE.stringToArrayBuffer(dest+val));
	};

	$scope.send = function() {
		//writeToDevice("1","1");
	}

	$scope.unSend = function() {
		//writeToDevice("1","0");
	}
});










