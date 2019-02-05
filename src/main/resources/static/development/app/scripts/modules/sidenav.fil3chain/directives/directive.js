(function() {

  /**
  * Config
  */
  var moduleName = 'directive.sidenav.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'ui.router',
      'service.sidenav.fil3chain'
    ]);
  }
  module.directive('sidenav', SidenavDirective);

  SidenavDirective.$inject = ['$log','$compile','$http','$state','Sidenav'];
  function SidenavDirective($log, $compile, $http, $state, Sidenav) {
    var templateSrc = 'scripts/modules/sidenav.fil3chain/templates/sidenav.html';
    return{
      restrict:'E',
      scope:{},
      compile: function compile(tElement, tAttrs, transclude) {
        return {
          pre: function preLink(scope, iElement, iAttrs, controller) {
            //$log.debug('SidenavDirective','pre',Sidenav);
            scope.sidenav = Sidenav.get($state.current.name);
            scope.actionClick = function(action){
              $state.go(action.state)
              //$log.info('sidenav','action','click',action);
            }
          },
          post: function postLink(scope, iElement, iAttrs, controller) {
            $http.get(templateSrc)
            //$log.debug('SidenavDirective','post');
            .then(function(template){
              //$log.debug('SidenavDirective','post','template load success');
              var compiled = $compile(template.data)(scope);
              angular.element(iElement).replaceWith(compiled);

              function changeSuccessListener(event, toState, toParams, fromState, fromParams){
                //$log.info('SidenavDirective','$stateChangeSuccess',event, toState, toParams, fromState, fromParams);
                scope.sidenav = Sidenav.get($state.current.name);
              }
              scope.$on('$stateChangeSuccess', changeSuccessListener);

            },function(error){
              $log.error('SidenavDirective','post','template load error',error);
            })
          }
        }
      }
    };
  };
})();
