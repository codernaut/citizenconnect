(function () {
    var app = angular.module('myApp', ['ui.router', 'ngIdle']);
    app.run(function ($rootScope, $location, $state, LoginService) {
        LoginService.isAuthenticated()
            .then(function (user) {
                if (user) {
                    $state.transitionTo('home')
                }
                else {
                    $state.transitionTo('login')
                }
            })
            .catch(function (user) {
                $state.transitionTo('login')
            });
    });
    app.config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $stateProvider
                .state('login', {
                    url: '/login',
                    templateUrl: 'login.html',
                    controller: 'LoginController'
                })
                .state('home', {
                    url: '/home',
                    templateUrl: 'home.html',
                    controller: 'HomeController',
                });
            $urlRouterProvider.otherwise('/');
        }])
})();