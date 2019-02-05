(function () {
  'use strict';

  angular
  .module('blockchainApp')
  .filter('FabState',FabStateFilter);

  FabStateFilter.$inject = ['$q'];
  function FabStateFilter($q) {
    return function(items, match){
      console.log('Fab State Filter',items, match);
      var matching = [];
      angular.forEach(items, function(item){
        if(item.states){
          angular.forEach(item.states, function(state){
            if(state===match){
              matching.push(item);
            }
          })
        }
      })
      return matching;
    };
}
})();
