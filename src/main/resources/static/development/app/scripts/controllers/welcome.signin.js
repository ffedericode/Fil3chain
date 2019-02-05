'use strict';

/**
* @ngdoc function
* @name blockchain.controller:signinCtrl
* @description
* # signinCtrl
* Controller of the blockchain
*/
angular.module('blockchainApp')
.controller('signinCtrl',['$scope', '$http','$state', '$mdToast','$window',function($scope ,$http, $state, $mdToast, $window){
  $scope.signin = function(user){
      console.log('signinCtrl','user',user)
      $http({
          url:'/fil3chain/sign_in',
          method:'POST',
          data: user
      })
          .then(function(response){
              console.log('response success',response)
              $mdToast.show(
                $mdToast.simple()
                .textContent('Welcome '+response.data.username)
                .position('bottom')
                .hideDelay(5000)
              );
              $window.sessionStorage.setItem('user',JSON.stringify(response.data));
              $state.go('app.wallet.profile');
          },function(response){
              console.log('response error ',response)
              $mdToast.show(
                $mdToast.simple()
                .textContent(response.status+' '+response.data.error)
                .position('bottom')
                .hideDelay(5000)
              );
          });
  };
  $scope.delete = function(user){
    user={}
  }
}]);
