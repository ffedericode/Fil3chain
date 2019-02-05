(function() {
  'use strict';
  /**
  * Config
  */
  var moduleName = 'service.navbar.fil3chain';
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
      'state.filters.navbar.fil3chain'
    ]);
  }
  module.factory('Navbar', NavbarService);

  function NavbarService($log, NavbarConfig, StateNavbarFilter) {
    var SidenavService = {};

    //$log.info('NavbarService',NavbarConfig, StateNavbarFilter)
    NavbarService.get = function(state){
      //$log.debug('NavbarService','get','state',state);
      var filtered;
      if(!state)state='default';
      filtered = StateNavbarFilter(NavbarConfig, state)[0];

      if(!filtered)filtered = StateNavbarFilter(NavbarConfig, 'default')[0];
      return filtered;
    }
    return NavbarService;
  };

})();
