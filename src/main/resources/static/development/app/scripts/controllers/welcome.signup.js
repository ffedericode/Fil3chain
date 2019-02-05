'use strict';

/**
* @ngdoc function
* @name blockchain.controller:signupCtrl
* @description
* # signupCtrl
* Controller of the blockchain
*/

angular.module('blockchainApp')
.controller('signupCtrl',['$scope','$http', '$state', '$mdToast',
function ($scope, $http, $state, $mdToast) {
    //console.log('signupCtrl');
    $scope.signup = function(user){
        //console.log(user)
        $http({
            url:'/fil3chain/sign_up',
            method:'POST',
            data: user
        })
            .then(function(response){
                //console.log('response success',response)
                $mdToast.show(
                  $mdToast.simple()
                  .textContent(response.data.response)
                  .position('bottom')
                  //.position($scope.getToastPosition())
                  .hideDelay(5000)
                );
                $state.go('app.welcome');
            },function(response){
                //console.log('response error ',response)
                $mdToast.show(
                  $mdToast.simple()
                  .textContent('Error '+response)
                  .position('bottom')
                  //.position($scope.getToastPosition())
                  .hideDelay(5000)
                );
            });
    };
    $scope.delete = function(user){
      console.log(user)
      user={};
    }
}])
