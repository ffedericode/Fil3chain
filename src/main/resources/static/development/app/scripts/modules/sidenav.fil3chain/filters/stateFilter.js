(function() {

  /**
  * Config
  */
  var moduleName = 'state.filters.sidenav.fil3chain';
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

  module.filter('StateSidenav',StateSidenavFilter);

    StateSidenavFilter.$inject =[];
    function StateSidenavFilter(){
      return function(items, match){
        //console.log('Fab State Filter',items, match);
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
    };
})();
