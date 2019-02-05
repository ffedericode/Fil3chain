/**
* speedDial.fil3chain modulo per la gestione di sppedDial button
**/


(function() {

  /**
  * Config
  */
  var moduleName = 'speedDial.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'config.speedDial.fil3chain',
      'service.speedDial.fil3chain',
      'directive.speedDial.fil3chain',
      'state.filters.speedDial.fil3chain'
    ]);
  }

})();
