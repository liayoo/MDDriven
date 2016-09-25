angular.module('register',[])

.controller('RegisterController', function($scope, $state) {
	$scope.register = function() {
		console.log("registered!");
		$state.go('login');
	}
})

/*.controller('RegisterController', RegisterController);

RegisterController.$inject = ['$state','$rootScope'];
function RegisterController($state, $rootScope) {
	var vm = this;
	vm.register = register;
	function register() {
		vm.dataLoading = true;
		console.log("registered!");
		$state.go('login');

	}
}*/