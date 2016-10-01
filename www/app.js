// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
angular.module('starter', ['ionic','login','register','measure','settings','services','ble.controllers'])

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
    .state('register', {
      url: '/register',
      templateUrl: 'states/home/register.html'
    })
    .state('sidemenu.main', {
      url: '/main',
      templateUrl: 'states/main/main.html'
    })
    .state('sidemenu', {  //remove sidemenuController?
      url: '/sidemenu',
      abstract: true,
      templateUrl: 'shared/sidemenu/sidemenu.html'
    })
    .state('measure', {
      url: '/measure',
      views: {
        'menuContent' :{
          templateUrl: 'states/main/measure.html',
          controller: "MeasureController"
        }
      }
    })
    .state('sidemenu.settings', {
      url: '/settings',
      views: {
        'menuContent': {
          templateUrl: 'states/users/settings.html',
          controller: "SettingsController"
        }
      }
    });
    $urlRouterProvider.otherwise('main/login');
})

