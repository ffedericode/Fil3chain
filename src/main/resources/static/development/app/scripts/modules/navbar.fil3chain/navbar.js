/**
navbar.fil3chain modulo che consente la gestione
della navbar
*/


(function() {

  /**
  * Config
  */
  var moduleName = 'navbar.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'config.navbar.fil3chain',
      'service.navbar.fil3chain',
      'directive.navbar.fil3chain',
      'state.filters.navbar.fil3chain'
    ]);
  }

})();
