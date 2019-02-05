(function() {

  /**
  * Config
  */
  var moduleName = 'directive.statistics.fil3chain';
  /**
  * Module
  */
  var module;
  try {
    module = angular.module(moduleName);
  } catch(err) {
    // named module does not exist, so create one
    module = angular.module(moduleName, [
      'service.statistics.fil3chain',
      'config.statistics.fil3chain'
    ]);
  }
  module.directive('widget', GraphDirective);
  module.directive('filechain', Fil3chainDirective);
  Fil3chainDirective.$inject = [
    '$log',
    '$compile',
    '$http',
    '$state',
    'Statistics',
    'StatisticsConfig'
  ];
  function Fil3chainDirective($log, $compile, $http, $state, Statistics, StatisticsConfig) {
    return{
      restrict:'A',
      link: function(scope, element, attrs){
        //$log.debug('Fil3chainDirective',scope, element, attrs)
        scope.block = null;
        scope.headers=[{
    			name: 'Chain Level',
    			field: 'chainLevel'
    		},{
    			name: 'Creation Time',
    			field: 'creationTime'
    		},{
    			name:'Hash Block',
    			field: 'hashBlock'
    		},{
    			name: 'Merkle Root',
    			field: 'merkleRoot'
    		},{
    			name: 'Miner Publick Key',
    			field: 'minerPublicKey'
    		},{
    			name: 'Nonce',
    			field: 'nonce'
    		},{
    			name: 'Signature',
    			field: 'signature'
    		},{
          name: 'User',
          field: 'userContainer',
          innerFields:[{
              name: 'Username',
              field: 'username'
            },{
            name: 'Public Key Hash',
            field: 'publicKeyHash'
          }]
        }];
        /*
        ,{
          name: 'Transaction',
          field: 'transactionsContainer',
          innerFields:[{
              name: 'File name',
              field: 'filename'
            },{
            name: 'Hash File',
            field: 'hashFile'
          }]
        }
        */
        //$log.debug('Fil3chainDirective','element chart:', element[0])
        angular.element(element[0]).css('width','100%');
        angular.element(element[0]).css('height','100%');

        var node_click = function(d){
          //console.log('CLIIIIIIIIIIIIIIII',d);
          //if(d.name===0)return;
          $http({
            method:'POST',
            url:'fil3chain/blockDetail',
            data:{
              hashBlock:d.name
            }
          }).then(function(response){
            //console.log('Fil3chainDirective','Get Block','success',response);
            scope.block = response.data;
          },function(response){
            //console.log('Fil3chainDirective','Get Block','error',response);

          })
        };
        attrs.$observe('data',function(){
          if(attrs.data){
            //console.log( 'Fil3chainDirective:', attrs.data );
            //var json = JSON.parse(newValue);
            angular.element(element[0]).empty();
            initTree(element[0], JSON.parse(attrs.data) ,node_click);
            scope.block = null;
          }
      });
      //Oggetto atto alla memorizzazione delle indici dei blocchi le cui transazioni sono aperte
      var openedFields ={};
      //questa funzione si occupa di gestire i flag di apertura del campo citations//delle varie transazioni
      scope.toggleInnerField = function(index, block, field){
          //console.log('Toggle transaction',index, block, field);
          if(!openedFields[block.hashBlock]){
            openedFields[block.hashBlock]={};
          }
          if(!openedFields[block.hashBlock][field])openedFields[block.hashBlock][field] = true;
          else openedFields[block.hashBlock][field] = !openedFields[block.hashBlock][field]
          //console.log('Toggle transaction','result', openedFields, openedFields[block.hashBlock]);
      }
      scope.openedFields = openedFields;


      function deleteDetails(){
        scope.block=null;
      }
      scope.deleteDetails=deleteDetails;
        var ConfigWidget = function(type, name, page){
          return {
            type:type,
            name:name,
            page:page
          }
        }
        scope.paginationPrev = function(page){
          //console.log('Page to send',parseInt(page)-1);
          Statistics.get(ConfigWidget(scope.type,scope.name,parseInt(page)-1))
          .then(function(response){
            //$log.debug('Fil3chainDirective','success',response)
            scope.data = response.data
          },function(response){
            //$log.debug('Fil3chainDirective','error',response)

          })
        };
        scope.paginationSucc = function(page){
          //console.log('Page to send',parseInt(page)+1);
          Statistics.get(ConfigWidget(scope.type, scope.name, parseInt(page)+1))
          .then(function(response){
            //$log.debug('Fil3chainDirective','success',response)
            scope.data = response.data
          },function(response){
            //$log.debug('Fil3chainDirective','error',response)

          })
        }
      }
    }
  }
  GraphDirective.$inject = [
    '$log',
    '$compile',
    '$http',
    '$state',
    'Statistics',
    'StatisticsConfig'
  ];
  function GraphDirective($log, $compile, $http, $state, Statistics, StatisticsConfig) {

    return{
      restrict:'E',
      scope:{
        type:'@',
        name:'@'
      },
      compile: function compile(tElement, tAttrs, transclude) {
        return {
          pre: function preLink(scope, iElement, iAttrs, controller) {
            //$log.debug('GraphDirective','pre',$state.current.name);
            //$log.debug('GraphDirective','pre',Statistics, StatisticsConfig);//),Navbar.get($state.current.name));
            var ConfigWidget = function(type, name){
              return {
                type:type,
                name:name,
                page:1
              }
            }
            scope.refreshWidget = function(){
              Statistics.get(ConfigWidget(scope.type,scope.name,scope.page))
              .then(function(response){
                //$log.debug('GraphDirective','success',response)
                scope.data = response.data
              },function(response){
                //$log.debug('GraphDirective','error',response)

              })
            };
            scope.refreshWidget();
            scope.pointClick = function (points, evt) {
              console.log(points, evt);

            };
            var openWidgetMenu = function($mdOpenMenu, ev){
              //console.log('Open Widget menu');
              //originatorEv = ev;
              $mdOpenMenu(ev);
            }
            scope.openWidgetMenu = openWidgetMenu;

          },
          post: function postLink(scope, iElement, iAttrs, controller) {
            var templateSrc = 'scripts/modules/statistics.fil3chain/templates/'+
            scope.type+'/'+scope.name+'.html';
            //$log.debug('GraphDirective','post');
            //console.log(scope,  templateSrc);
            $http.get(templateSrc)
            .then(function(template){
              //$log.debug('GraphDirective','post','template load success');
              var compiled = $compile(template.data)(scope);
              if(scope.type==='number'){
                compiled.css('max-height','8em');
              }
              angular.element(iElement).replaceWith(compiled);
            },function(error){
              $log.error('GraphDirective','post','template load error',error);
            })
          }
        }
      }
    };
  };
})();
