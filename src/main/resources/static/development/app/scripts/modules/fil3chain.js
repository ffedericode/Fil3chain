(function() {
  'use strict';
  /**
  * Config
  */
  var moduleName = 'fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'statistics.fil3chain',
      'sha256.fil3chain',
      'navbar.fil3chain',
      'sidenav.fil3chain',
      'speedDial.fil3chain'
    ]);
  }
})();
