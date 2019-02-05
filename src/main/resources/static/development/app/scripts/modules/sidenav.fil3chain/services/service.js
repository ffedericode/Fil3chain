(function() {
  'use strict';
  /**
  * Config
  */
  var moduleName = 'service.sidenav.fil3chain';
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
      'state.filters.sidenav.fil3chain'
    ]);
  }
  module.factory('Sidenav', SidenavService);

  SidenavService.$inject = ['$log','SidenavConfig','StateSidenavFilter'];
  function SidenavService($log, SidenavConfig, StateSidenavFilter) {
    var SidenavService = {};

    //$log.info('SidenavService',SidenavConfig, StateSidenavFilter)
    SidenavService.get = function(state){
      //$log.debug('SidenavService','get','state',state);
      var filtered;
      if(!state)state='default';
      filtered = StateSidenavFilter(SidenavConfig, state)[0];

      if(!filtered)filtered = StateSidenavFilter(SidenavConfig, 'default')[0];
      //console.log('uuuuuuuuuuu',filtered);
      return filtered;
    }
    return SidenavService;
  };

})();
