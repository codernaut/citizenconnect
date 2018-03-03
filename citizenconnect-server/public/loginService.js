var app = angular.module('myApp');

app.factory('LoginService', function () {
  var isAuthenticated = false;

  return {
    login: function (username, password) {
      firebase.auth().setPersistence(firebase.auth.Auth.Persistence.SESSION)
        .then(function () {
          return firebase.auth().signInWithEmailAndPassword(username, password);
        })
        .catch(function (error) {

        });
      return firebase.auth().signInWithEmailAndPassword(username, password)
    },
    isAuthenticated: function () {
      return new Promise(
        function (resolve, reject) {
          firebase.auth().onAuthStateChanged(function (user) {
            if (user) {
              resolve(true);
            }
            else {
              reject(false);
            }
          });
        }
      );
    },
    getUserToken: function () {
      return firebase.auth().currentUser.getIdToken(/* forceRefresh */ true);
    },
    logOut: function () {
      return firebase.auth().signOut();
    }
  };
});