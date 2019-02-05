/**
* Filtri angular utilizzati dal modulo navbar.fil3chain
*/


(function() {

  /**
  * Config
  */
  var moduleName = 'state.filters.navbar.fil3chain';
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

  module.filter('StateNavbar',StateNavbarFilter);


  StateNavbarFilter.$inject =[];
  function StateNavbarFilter(){
    return function(items, match){
      //console.log('StateNavbarFilter',items, match);
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


  module.filter('TabNavbar',TabNavbarFilter);

  TabNavbarFilter.$inject =[];
  function TabNavbarFilter(){
    return function(items, match){
      console.log('TabNavbarFilter',items, match);
      var matching = [];
      angular.forEach(items, function(item,index){
        console.log('TabNavbarFilter index',item, index);
        //angular.forEach(item.links, function(link){
          if(item.state===match){
            matching.push(index);
          }
        //})
      })
      return matching;
    };
  };
})();
