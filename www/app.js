// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
angular.module('starter', ['ionic','login','register','measure','settings','sidemenu','services','ble.controllers'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);

      // Don't remove this line unless you know what you are doing. It stops the viewport
      // from snapping when text inputs are focused. Ionic handles this internally for
      // a much nicer keyboard experience.
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }

    ble.isEnabled(
      function() {
        alert("Bluetooth is enabled");
        console.log("Bluetooth is enabled");
      },
      function() {
        console.log("Bluetooth is not enabled");
        alert("Bluetooht is not enabled");
      }
    );

  });
})


.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state('login', {
      url: '/login',
      templateUrl: 'states/home/login.html'
    })
    .state('main', {
      url: '/main',
      abstract: true,
      templateUrl: 'states/main/main.html'
    })
    .state('sidemenu', {
      url: '/sidemenu',
      views: {
        'sidemenu': {
          templateUrl: 'shared/sidemenu/sidemenu.html'
        }
      }
    })
    .state('register', {
      url: '/register',
      templateUrl: 'states/home/register.html'
    })
    .state('measure', {
      url: '/measure',
      templateUrl: 'states/main/measure.html'
    })
    .state('settings', {
      url: '/settings',
      templateUrl: 'states/users/settings.html'
    });
    $urlRouterProvider.otherwise('main/login');
})

