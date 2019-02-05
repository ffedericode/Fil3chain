/**
* sidenav.fil3chain modulo per la gestione di una sidenav
*/


(function() {

  /**
  * Config
  */
  var moduleName = 'sidenav.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'config.sidenav.fil3chain',
      'service.sidenav.fil3chain',
      'directive.sidenav.fil3chain',
      'state.filters.sidenav.fil3chain'
    ]);
  }

})();
