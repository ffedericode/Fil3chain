(function() {

  /**
  * Config
  */
  var moduleName = 'user.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, ['service.user.fil3chain','validator.service.user.fil3chain']);
  }

})();
