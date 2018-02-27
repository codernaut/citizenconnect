var app = angular.module('myApp');

app.controller('HomeController',
  function ($scope, $rootScope, $stateParams, $state, $http, LoginService, Idle) {

    LoginService.getUserToken()
      .then(function (idToken) {
        $scope.token = idToken;
      }).catch(function (error) {
        // Handle error
      });
    $scope.$on('IdleStart', function () {
      // the user appears to have gone idle
    });

    $scope.$on('IdleWarn', function (e, countdown) {
      window.alert("Session Expired! Please Login To Continue");
      Idle.watch();
      $scope.logout();
    });

    $scope.$on('IdleTimeout', function () {
      // the user has timed out (meaning idleDuration + timeout has passed without any activity)
      // this is where you'd log them

    });

    $scope.$on('IdleEnd', function () {
      // the user has come back from AFK and is doing stuff. if you are warning them, you can use this to hide the dialog
    });

    $scope.$on('Keepalive', function () {
      // do something to keep the user's session alive

    });
    $scope.user = $rootScope.userName;
    LoginService.isAuthenticated()
      .catch(function (user) {
        $state.transitionTo('login')
      });
    $scope.logout = function () {
      LoginService.logOut().then(function () {

        $state.transitionTo('login')
      }, function (error) {
        window.alert(error);
      });
    }

    $scope.sendNotification = function () {

      var token = 'Bearer ' + $scope.token;
      var data = new FormData();
      data.append('file', $scope.file)
      data.append('description', $scope.description)
      data.append('tag', $scope.tag)
      $http.post(
        "https://us-central1-citizenconnect-ed5fa.cloudfunctions.net/notifications/sendNotification",
        data,
        {
          transformRequest: angular.identity,
          headers: {
            'Content-Type': undefined,
            'Authorization': token
          }
        })
        .then(function successCallback(response) {
          console.log("Successfully POST-ed data");
          $scope.message = "Successfully POST-ed data";
        }, function errorCallback(response) {
          console.log("POST-ing of data failed");
          $scope.message = "POST-ing of data failed";
        });
    }
  })
app.config(['KeepaliveProvider', 'IdleProvider',
  function (KeepaliveProvider, IdleProvider) {
    IdleProvider.idle(60);
    IdleProvider.timeout(5);
    KeepaliveProvider.interval(5);
  }])
  .run(function (Idle) {
    Idle.watch();
  });;