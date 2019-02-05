(function() {

  /**
  * Config
  */
  var moduleName = 'directive.speedDial.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, ['ui.router','service.speedDial.fil3chain']);
  }
  module.directive('speedDial', SpeedDialDirective);

  SpeedDialDirective.$inject = ['$log','$compile','$http','$state','SpeedDial'];
  function SpeedDialDirective($log, $compile, $http, $state, SpeedDial) {
    var templateSrc = 'scripts/modules/speedDial.fil3chain/templates/speedDial.html';
    return{
      restrict:'E',
      scope:{},
      compile: function compile(tElement, tAttrs, transclude) {
        return {
          pre: function preLink(scope, iElement, iAttrs, controller) {
            //$log.debug('SpeedDialDirective','pre');
            scope.speedDial = SpeedDial.setConfig($state.current.name);
            scope.actionClick = function(action){
              //$log.info('speedDial','action','click',action);
              action.click($state);
            }
          },
          post: function postLink(scope, iElement, iAttrs, controller) {
            //$log.debug('SpeedDialDirective','post');
            $http.get(templateSrc)
            .then(function(template){
              //$log.debug('SpeedDialDirective','post','template load success');
              var compiled = $compile(template.data)(scope);
              angular.element(iElement).replaceWith(compiled);

              function changeSuccessListener(event, toState, toParams, fromState, fromParams){
                //$log.info('speedDial','$stateChangeSuccess',event, toState, toParams, fromState, fromParams);
                scope.speedDial = SpeedDial.setConfig($state.current.name);

                //$log.info('speedDial','$viewContentLoading','after',scope.speedDial);
              }
              scope.$on('$stateChangeSuccess', changeSuccessListener);

            },function(error){
              $log.error('SpeedDialDirective','post','template load error',error);
            })
          }
        }
      }
    };
  };
})();
