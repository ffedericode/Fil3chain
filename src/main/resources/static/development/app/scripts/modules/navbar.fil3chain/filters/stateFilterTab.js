(function() {

  /**
  * Config
  */
  var moduleName = 'tab.filters.navbar.fil3chain';
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

  module.filter('TabNavbar',TabNavbarFilter);

  TabNavbarFilter.$inject =[];
  function TabNavbarFilter(){
    return function(items, match){
      ////console.log('Navbar State Filter',items, match);
      var matching = [];
      angular.forEach(items, function(item,index){
        //console.log('Navbar State Filter index',item, index);
        angular.forEach(item.links, function(link){
          if(link.state===match){
            matching.push(index);
          }
        })
      })
      return matching;
    };
  };
})();
