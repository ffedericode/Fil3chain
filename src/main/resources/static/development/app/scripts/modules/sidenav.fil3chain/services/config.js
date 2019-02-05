(function() {
  'use strict';
  /**
  * Config
  */
  var moduleName = 'config.sidenav.fil3chain';
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

  module.factory('SidenavConfig', SidenavServiceConfig);

  SidenavServiceConfig.$inject = [];
  function SidenavServiceConfig() {
    return[
      {
        states : [
          'app.wallet',
          'app.fil3chain',
          'app.fil3chain.dashboard',
          'app.fil3chain.statistics',
          'app.fil3chain.transactions',
          'app.wallet',
          'app.wallet.profile',
          'app.wallet.transactions',
          'app.wallet.transactions.post',
          'app.wallet.statistics',
          'app.miner'
        ],
        links:[{
          state : 'app.fil3chain.dashboard',
          label: 'Fil3chain',
          icon: 'icons/ic_dashboard_black_24px.svg'
        },
        {
          state : 'app.miner',
          label: 'Miner',
          icon: 'icons/mineCart_black.svg'
        },
        {
          state : 'app.wallet.profile',
          label: 'Wallet',
          icon: 'icons/user_black.svg'
        }]
      },
      {
        states : ['default'],
        links: []
      }
    ];
  };


})();
