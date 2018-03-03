var app = angular.module('myApp');
app.controller('LoginController', function ($scope, $rootScope, $stateParams, $state, LoginService) {
  $rootScope.title = "ICT App - Login";

  $scope.formSubmit = function () {
      LoginService.login($scope.username, $scope.password)
        .then(function (firebaseResponse) {
          $rootScope.userName = $scope.username;
          $scope.error = '';
          $scope.username = '';
          $scope.password = '';
          $state.transitionTo('home');
        })
        .catch(function (error) {
          window.alert("Login Attempt Failed");
          $scope.error = "Incorrect username/password !";
        });
  };
});