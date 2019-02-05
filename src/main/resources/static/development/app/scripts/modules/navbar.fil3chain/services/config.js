(function() {
  'use strict';
  /**
  * Config
  */
  var moduleName = 'config.navbar.fil3chain';
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

  module.factory('NavbarConfig', NavbarServiceConfig);

  NavbarServiceConfig.$inject = [];
  function NavbarServiceConfig() {
    function changeState(state, name) {
      state.go(name)
    }
    return[
      {
        states : [
          'app.wallet',
          'app.wallet.profile',
          'app.wallet.transactions',
          'app.wallet.statistics'
        ],
        links:[{
          state : 'app.wallet.profile',
          label: 'Profile',
          action: changeState
        },
        {
          state : 'app.wallet.transactions',
          label: 'Transactions',
          icon: 'icons/ic_copyright_black_24px.svg'
        },
        {
          state : 'app.wallet.statistics',
          label: 'Statistics',
          icon: 'icons/graph_black.svg'
        }]
      },
      {
        states:['app.wallet.transactions.post'],
        links:[]
      },
      {
        states : [
          'app.fil3chain',
          'app.fil3chain.dashboard',
           'app.fil3chain.statistics',
           'app.fil3chain.transactions'
        ],
        links:[{
          state : 'app.fil3chain.dashboard',
          label: 'Dashboard'
        },
        {
          state : 'app.fil3chain.transactions',
          label: 'Transactions',
          icon: 'icons/ic_copyright_black_24px.svg'
        },
        {
          state : 'app.fil3chain.statistics',
          label: 'Statistics',
          icon: 'icons/graph_black.svg'
        }]
      },
      {
        states : ['default','app.miner'],
        links: []
      }
    ];
  };


})();
