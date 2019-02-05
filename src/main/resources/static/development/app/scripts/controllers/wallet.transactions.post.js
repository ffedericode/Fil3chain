'use strict';

/**
 * @ngdoc function
 * @name blockchain.controller:signinCtrl
 * @description
 * # signinCtrl
 * Controller of the blockchain
 */
angular.module('blockchainApp')
.controller('walletTransactionPostCtrl',[
  '$scope', '$mdDialog', '$mdMedia','$mdToast','$state','TransactionService','transactionHeaders','TransactionsCitationsAdapterFilter',
  function($scope,$mdDialog, $mdMedia,$mdToast,$state,TransactionService,  transactionHeaders, TransactionsCitationsAdapterFilter){
	//console.log('walletTransactionPostCtrl');
  //Variabile flag usata per mostrare eventuali citazioni da allegare alla transazione
	$scope.showCitationsList=false;
  //Oggetto usato per reperire l'icona del bottone che permette di visualizzare le citazioni
  //in base al suo stato
	$scope.citationsContainer ={
			show: false,
			false:'icons/ic_keyboard_arrow_down_black_24px.svg',
			true:'icons/ic_keyboard_arrow_up_black_24px.svg'
	}
  //Variabile che conterrà la transazione ad inviare al server
	$scope.fileToSend = {};
  //Aggiunta di un array di citazioni vuoto alla transazione
	$scope.fileToSend.citations = [];

  //Questa funzione si occupa di visualizzare un messaggio di dialogo
  //ne quale è possibile selezionare le citazioni da inserire
	function showAlert(ev, transactions, citations) {
		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) ;
		$mdDialog.show({
			controller: 'citationDialogCtrl',
			templateUrl: 'views/citation.dialog.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			locals: {
				transactions: transactions,
				transactionHeaders : transactionHeaders,
				citations : citations
			},
			clickOutsideToClose:true,
			fullscreen: true
		})

	};
  //Funzione che si occupa dell'invio della transazione
  // riceve in ingresso una variabile contenente tutte la transazione compilata
	function submitTransaction(file){
		//console.log('walletTransactionPostCtrl','submitTransaction',file);

    //Viene aggiornato l'array delle citazioni per far combaciare il tutto con hybernate
    file.citations = TransactionsCitationsAdapterFilter(file.hashFile, file.citations);
    //console.log('walletTransactionPostCtrl','submitTransaction','Adapted',file);

    //Chiamata al servizio che si occupa di gestire le transazioni
    //al termine si viene reindirizzati alla sezione transactions
		TransactionService.post(file)
		.then(function(response){
			$mdToast.show(
					$mdToast.simple()
					.textContent('Transaction submitted:\n '+response.response)
					.position('bottom')
					.hideDelay(5000)
			);
      $state.go('app.wallet.transactions')
		},function(error){
			$mdToast.show(
					$mdToast.simple()
					.textContent('Error Transaction submit: '+error)
					.position('bottom')
					.hideDelay(5000)
			);
      $state.go('app.wallet.transactions')
		})
	}
	$scope.submitTransaction = submitTransaction;

  //elimina dall'array la citazione di indice pari a quello preso in input
	function deleteCitation(index){
    $scope.fileToSend.citations.splice(index, 1)
	}
	$scope.deleteCitation = deleteCitation;


	function showCitation(ev){
		TransactionService.get()
		.then(function(response){
			//console.log('walletTransactionPostCtrl','success');
			$scope.transactions = response;
			showAlert(ev, $scope.transactions, $scope.fileToSend.citations);
		},function(response){
			//console.log('walletTransactionPostCtrl','error');

			$scope.transactions = [];
      $mdToast.show(
          $mdToast.simple()
          .textContent('Transaction error: '+ JSON.stringify(response))
          .position('bottom')
          //.position($scope.getToastPosition())
          .hideDelay(5000)
      );
			//showAlert(ev, $scope.transactions, $scope.fileToSend.citations);
		})

	}
	$scope.showCitation = showCitation;


  function deleteTransaction(){
    $mdToast.show(
        $mdToast.simple()
        .textContent('Transaction deleted')
        .position('bottom')
        //.position($scope.getToastPosition())
        .hideDelay(5000)
    );
    $state.go('app.wallet.transactions')
  }
  $scope.deleteTransaction=deleteTransaction;
}]);
