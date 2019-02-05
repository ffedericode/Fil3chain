(function () {
	'use strict';

	angular
	.module('blockchainApp')
	.factory('MinerService', MinerServiceImpl);

	MinerServiceImpl.$inject = ['$http','$q'];
	function MinerServiceImpl($http,  $q) {
		var service = {};
		var MINER_ENDPOINT_IP = 'fil3chain/ips';
		var MINER_ENDPOINT_CHECK = 'fil3chain/checkMining';
		var MINER_ENDPOINT_START = 'fil3chain/starMining';
		var MINER_ENDPOINT_STOP = 'fil3chain/stopMining';
		
		
		service.getIps = GetIps;
		service.postIp = PostIps;

		service.check = GetCheck;
		service.start = GetStartMining;
		service.stop = GetStopMining;
		return service;

		function GetIps() {
			var deferred = $q.defer();
			$http({
				method: 'GET',
				url: MINER_ENDPOINT_IP
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
				//deferred.reject(response);    //riattivare
				deferred.resolve(transactionsMock); //disattivare 
			});
			return deferred.promise;
		}

		function PostIps(ip){
			var deferred = $q.defer();
			$http({
				method: 'POST',
				url: MINER_ENDPOINT_IP,
				date: ip
			}).success(function successCallback(response) {
				// this callback will be called asynchronously
				// when the response is available
				console.log(response);
				referred.resolve(response);
			}).error(function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
				console.log(response);
				deferred.reject(response);
			});
			return deferred.promise;
		}
		
		function GetCheck(){
			var deferred = $q.defer();
			$http({
				method: 'GET',
				url: MINER_ENDPOINT_CHECK
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
				//deferred.reject(response);    //riattivare
				deferred.resolve(transactionsMock); //disattivare 
			});
			return deferred.promise;
		}
		
		function GetStartMining() {
			var deferred = $q.defer();
			$http({
				method: 'GET',
				url: MINER_ENDPOINT_START
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
				//deferred.reject(response);    //riattivare
				deferred.resolve(transactionsMock); //disattivare 
			});
			return deferred.promise;
		}
		function GetStopMining() {
			var deferred = $q.defer();
			$http({
				method: 'GET',
				url: MINER_ENDPOINT_STOP
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
				//deferred.reject(response);    //riattivare
				deferred.resolve(transactionsMock); //disattivare 
			});
			return deferred.promise;
		}
	}

})();
