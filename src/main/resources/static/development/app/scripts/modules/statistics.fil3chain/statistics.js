/**
* statistics.fil3chain modulo per la gestione delle statistiche e widget
*/


(function() {

  /**
  * Config
  */
  var moduleName = 'statistics.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'config.statistics.fil3chain',
      'service.statistics.fil3chain',
      'directive.statistics.fil3chain'
    ]);
  }

})();
