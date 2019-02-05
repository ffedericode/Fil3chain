'use strict';

/**
 * @ngdoc function
 * @name blockchain.controller:signinCtrl
 * @description
 * # signinCtrl
 * Controller of the blockchain
 */
angular.module('blockchainApp')
.controller('minerCtrl',function($scope, $mdDialog, $mdMedia,MinerService, ips, miningCheck){
	//miningCheck conterrà lo stato attuale del mining
	//////console.log('minerCtrl',miningCheck);
	//Variabile contenente l'ip selezionato dall'utente
	//lo start_time ed end_time di mining ritornati dal server a seguito dello start
	$scope.miner={};

	var buttonMinerStart={
			icon:'icons/ic_play_arrow_white_24px.svg',
			label:'Start miner'
	};
	var buttonMinerStop={
			icon:'icons/ic_stop_white_24px.svg',
			label:'Stop miner'
	};

	var buttonConfig={
			state: miningCheck,
			false: buttonMinerStart,
			true: buttonMinerStop
	}
	//Assegnazione bottone per la gestione del servizio di mining
	$scope.buttonMiner = buttonConfig[buttonConfig.state];
	//funzione che si occupa di fare lo switch del bottone
	//settandolo al valore opposto del suo stato corrente
	function switchButton(){
		buttonConfig.state = !buttonConfig.state
		$scope.buttonMiner = buttonConfig[buttonConfig.state];
	}


	function minerButtonClick(event){
		////console.log('minerCtrl','minerButtonClick');
		//Se lo stato del bottone è false allora devo avviare il mining
		if(!buttonConfig.state) {
			//mostro un dialog per far selezionare l'ip all'utente
			showAlert(event)
			.then(function(selected_ip) {
				//console.log('You said the information was "' + selected_ip + '".');
				$scope.miner.selected_ip = selected_ip;
				$scope.miner.startTime = Date.now();
				//Una volta selezionato l'ip chiamo lo start passandogli l'ip scelto   TODO
				MinerService.start()
				.then(function(miner) {
					switchButton();
					////console.log('Mining Start success.',miner);
				},function() {
					console.log('Mining Start error.');
				})
			}, function() {
				//console.log('You cancelled the dialog.');
			});
		}
		else {
			MinerService.stop()
			.then(function(selected_ip) {
				switchButton();
				//console.log('Mining Stop success.',buttonConfig.state);
			},function(selected_ip) {
				console.log('Mining Stop error.');
			})
		}
		return;
	}
	$scope.minerButtonClick = minerButtonClick;



	//funzione atta alla visualizzazione di un dialog nel quale l'utente
	//ha la possibilità di selezionare uno degli ip disponibili dalla sua scheda di rete
	function showAlert(ev) {

		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) ;// && $scope.customFullscreen;

		return $mdDialog.show({
			controller: 'minerDialogCtrl',
			templateUrl: 'views/miner.dialog.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose:true,
			fullscreen: useFullScreen,
			locals: {
				ips: ips
			}
		})

	};


});
