
(function() {

  /**
  * Config
  */
  var moduleName = 'validator.service.user.fil3chain';
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


  module.factory('UserValidatorService', UserValidatorService);

  UserValidatorService.$inject = ['$timeout', '$filter', '$q'];
  function UserValidatorService($timeout, $filter, $q) {

    var service = {};

    service.validateUserSignup = ValidateUserSignup;
    service.validateUserSignin = ValidateUserSignin;


    return service;

    function ValidateUserSignup(user) {
      var deferred = $q.defer();
      console.log('ValidateUserSignup',!user);
      if(!user){
        deferred.reject('User is empty')
      }else{
        if(!user.name)deferred.reject('User name is empty')
        if(!user.surname)deferred.reject('User surname is empty')
        if(!user.email)deferred.reject('User email is empty')
        if(!user.username)deferred.reject('User username is empty')
        if(!user.password)deferred.reject('User password is empty')
      }
      deferred.resolve(user);
      return deferred.promise;
    }

    function ValidateUserSignin(user) {
      var deferred = $q.defer();
      console.log('ValidateUserSignin',!user);
      if(!user){
        deferred.reject('User is empty')
      }else{
        if(!user.username)deferred.reject('User username is empty')
        if(!user.password)deferred.reject('User password is empty')
      }
      deferred.resolve(user);
      return deferred.promise;
    }

    function validateName(name){
      var deferred = $q.defer();
      if(!name) deferred.reject('Name is undefined')
      deferred.resolve(name);
      return deferred.promise;
    }
    function validateSurname(surname){

    }
    function validateEmail(email){

    }
    function validateUsername(username){

    }
    function validatePassword(password){

    }
  }

})();
