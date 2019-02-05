'use strict';

/**
* @ngdoc function
* @name blockchain.controller:signinCtrl
* @description
* # signinCtrl
* Controller of the blockchain
*/
angular.module('blockchainApp')
.controller('userProfileCtrl',['$scope','$mdToast','$window','$state', function($scope, $mdToast, $window,$state){

    if(!$window.sessionStorage.getItem('user')){
      $mdToast.show(
        $mdToast.simple()
        .textContent('User Not founded')
        .position('bottom')
        .hideDelay(5000)
      );
      $state.go('app.welcome');
    }else{
      $scope.user = JSON.parse($window.sessionStorage.getItem('user'));

    }

}]);
