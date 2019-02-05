/*
*/

(function() {
  'use strict';
  /**
  * Config
  */
  var moduleName = 'config.statistics.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, []);
  }

  module.factory('StatisticsConfig', StatisticsServiceConfig);

  StatisticsServiceConfig.$inject = ['$log'];
  function StatisticsServiceConfig($log) {

    var config = {
      'ips':'fil3chain/statistics/ips',
      'blocks':'fil3chain/statistics/blocks',
      'fil3chain':'fil3chain/statistics/fil3chain',
      'power':'fil3chain/statistics/power',
      'mlevel':'fil3chain/statistics/mlevel'
    };
    var StatisticsServiceConfig={};
    //Prende in ingresso il nome del widget e ritorna il link dell'endpoint
    StatisticsServiceConfig.get = function(name){
      //$log.debug('StatisticsServiceConfig','get','name',name);
      return config[name];
    }

    return StatisticsServiceConfig;
  };



})();
