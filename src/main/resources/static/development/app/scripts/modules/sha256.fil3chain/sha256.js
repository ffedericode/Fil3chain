/**
* sha256.fil3chain modulo per la gestione di Sha256
*/


(function() {

  /**
  * Config
  */
  var moduleName = 'sha256.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'service.sha256.fil3chain'
    ]);
  }

})();
