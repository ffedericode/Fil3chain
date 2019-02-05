(function () {
	'use strict';

	angular
	.module('blockchainApp')
	.factory('TransactionService', TransactionServiceImpl);

	TransactionServiceImpl.$inject = ['$http','$q'];
	function TransactionServiceImpl($http,  $q) {
		var service = {};
		var TRANSACTION_ENDPOINT_CITATIONS = 'fil3chain/citations';
		var TRANSACTION_ENDPOINT_TRANSACTION = 'fil3chain/sendTransaction';
		var TRANSACTION_HEADERS =[{
			name: 'Id Transazione',
			field: 'hashTransBlock'
		},{
			name: 'Hash File',
			field: 'hashFile'
		},{
			name:'File name',
			field: 'filename'
		},{
			name: 'Index in Block',
			field: 'index_in_block'
		},{
			name: 'Block Container',
			field: 'blockContainer'
		}
		,{
			name: 'Citations',
			field: 'citations'
		}
		];
		var transactionsMock=[{
			hashFile:'oihsoaidhsodhsad',
			filename:'filename',
			index_in_block:'null',
			blockContainer:'null',
			authorContainer:'author',
			citationContainer:[

			                   ]
		},{
			hashFile:'hash2',
			filename:'filename',
			index_in_block:'null',
			blockContainer:'null',
			authorContainer:'author',
			citationContainer:[

			                   ]
		}
		]
		service.get = GetTransaction;
		service.getTransactionHeader = GetTransactionHeader;
		service.post = PostTransaction;
		/*
    service.put = PutTransaction;
    service.delete = DeleteTransaction;
		 */
		return service;
		function GetTransactionHeader() {
			return TRANSACTION_HEADERS;
		}
		function GetTransaction() {
			var deferred = $q.defer();
			$http({
				method: 'GET',
				url: TRANSACTION_ENDPOINT_CITATIONS
			}).success(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				//alert('Transazione inviata con successo');
				console.log(response);
				deferred.resolve(response);
				// deferred.resolve(transactionsMock); //disattivare
			}).error(function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
				//alert('Errore durante l\'invio della transazione');
				console.log(response);
				deferred.reject(response);    //riattivare
				//deferred.resolve(transactionsMock); //disattivare
			});
			return deferred.promise;
		}

		function PostTransaction(transaction){
			var deferred = $q.defer();
			$http({
				method: 'POST',
				url: TRANSACTION_ENDPOINT_TRANSACTION,
				data: transaction
			}).success(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				//alert('Transazione inviata con successo');
				console.log(response);
				deferred.resolve(response);
			}).error(function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
				//alert('Errore durante l\'invio della transazione');
				console.log(response);
				deferred.reject(response);
			});
			return deferred.promise;
		}
	}

})();
