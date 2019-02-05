'use strict';

/**
* @ngdoc function
* @name blockchain.controller:signinCtrl
* @description
* # signinCtrl
* Controller of the blockchain
*/
angular.module('blockchainApp')
.controller('walletTransactionCtrl',['$scope','$mdToast','transactions','transactionHeaders', function($scope, $mdToast, transactions, transactionHeaders){
  //console.log('walletTransactionCtrl',transactions,transactionHeaders);
  //Oggetto atto alla memorizzazione degli indici delle transazioni le cui citazioni sono aperte
  var openedCitations ={};
  //questa funzione si occupa di gestire i flag di apertura del campo citations//delle varie transazioni
  $scope.toggleCitation = function(index, transaction){
      //console.log('Toggle transaction',index, transaction);
      openedCitations[transaction.hashTransBlock] =! openedCitations[transaction.hashTransBlock];
      //console.log('Toggle transaction','result', openedCitations);
  }
  $scope.openedCitations = openedCitations;
  //Caso in cui non ricevo i service che mi aspetto
  if(!transactions || !transactionHeaders){
	  $mdToast.show(
		        $mdToast.simple()
		        .textContent('No transaction found')
		        .position('bottom')
		        .hideDelay(5000)
		      );
  }else{
	  $scope.transactions = transactions;
	  $scope.headers = transactionHeaders;
  }
  //Disabilitata la modalità selezione
  $scope.selectionMode = false;



    $scope.selectedTransactionIndex = undefined;
    $scope.selectUserIndex = function (index) {
      if ($scope.selectedTransactionIndex !== index) {
        $scope.selectedTransactionIndex = index;
      }
      else {
        $scope.selectedTransactionIndex = undefined;
      }
    };
}]);
