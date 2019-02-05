(function () {
  'use strict';

  angular
  .module('blockchainApp')
  .filter('ExactMatch', ExactMatchFilter);

  ExactMatchFilter.$inject = ['$http'];
  function ExactMatchFilter($http) {
    return function(items, match){
      var matching = [], matches, falsely = true;

      // Return the items unchanged if all filtering attributes are falsy
      angular.forEach(match, function(value, key){

        falsely = falsely && !value;
        console.log(value, key);
      });
      if(falsely){
        return items;
      }

      angular.forEach(items, function(item){ // e.g. { title: "ball" }
      matches = true;
      angular.forEach(match, function(value, key){ // e.g. 'all', 'title'
      if(!!value){ // do not compare if value is empty
        matches = matches && (item[key] === value);
      }
    });
    if(matches){
      matching.push(item);
    }
  });
  return matching;
}
}
})();
