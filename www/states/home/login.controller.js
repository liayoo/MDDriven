angular.module('login',[])

.controller('LoginController', function($scope, $state) {
  $scope.signin = function(id, pw, stayLoggedIn) {
    if(id && pw) {
      $state.go('main');
    }
  }
  $scope.register = function(firstname, lastname, email) {
    if(firstname && lastname && email) {
      $state.go('register');
    }
  }
})