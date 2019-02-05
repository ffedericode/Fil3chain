'use strict';

/**
* @ngdoc overview
* @name testComponentApp
* @description
* # testComponentApp
*
* Main module of the application.
*/
angular
.module('blockchainApp')
.directive('upload',['$q','$mdToast','Sha256',function ($q,$mdToast,sha256Service) {
  var directive = {
    restrict: 'C',
    /*template: '<input id="fileInput" type="file" class="ng-hide">
    <md-button id="uploadButton" class="md-raised md-primary" aria-label="attach_file">
    Choose file
    </md-button>
    <md-input-container  md-no-float>
    <input id="textInput" ng-model="fileName" type="text" placeholder="No file chosen" ng-readonly="true">
    </md-input-container>',*/
    template: buildFormTemplate,
    link: apsUploadFileLink
  };
  return directive;

  function buildFormTemplate(){
    return(
      '<md-fab-speed-dial  class="md-fab-bottom-right">'+
      '<md-fab-trigger>'+
      '<input type="file" class="ng-hide">'+
      '<md-button id="uploadButton" class="md-fab" aria-label="Select Filed">'+
          '<md-icon md-svg-src="icons/ic_search_white_24px.svg"></md-icon>'+
        '</md-button>'+
      '</input>'+
      '</md-fab-trigger>'+
      '</md-fab-speed-dial>'
    );
  }
  function checkFileReader(){
    var deferred = $q.defer();
    // Check for the various File API support.
    if (window.File && window.FileReader && window.FileList && window.Blob) {
      //if supported resolve
      deferred.resolve();
    } else {
      deferred.reject('The File APIs are not fully supported by your browser.');
    }
    return deferred.promise;
  }

  function apsUploadFileLink(scope, element, attrs) {
    console.log('element ',element);
    var reader;
    var inputWrapper;
    var innerButton;

    scope.toggleProgress = function(){
      console.log('Toggle',scope.progressMode);
      if(!scope.progressMode || scope.progressMode===''){
        scope.progressMode='indeterminate';
        scope.$apply();
      }else {
        scope.progressMode='';
      }
    }

    checkFileReader()
    .then(function(){

      inputWrapper = element[0].children[0];
      innerButton = element[0].children[1];
      angular.element(innerButton).on('click',function(){
        inputWrapper.click();
      })
      element.on('click',function(){
        console.log('pddddddd',element.find('input')[0]);
        element.find('input')[0].click();
      })

      element.on('change',function(event){

        console.log('Input changed',event);
        //var files = event.target.files;
        var file = event.target.files[0];
        if(file){
          reader = new FileReader();
        scope.toggleProgress();
        console.log('File',file);

        // Closure to capture the file information.
        reader.onload = (function(theFile) {
          return function(e) {
            console.log('Reader OnLoad',e,theFile);
            //console.log('Blob', e.currentTarget.result);
            sha256Service.hash(e.currentTarget.result)
            .then(function(sha){
            	scope.citations = [];
              //console.log('Sha256', sha);
              scope.file = theFile;
              scope.file.sha256 = sha;
              //console.log('scope',scope);
              scope.toggleProgress();

              scope.fileToSend={
            		  hashTransBlock:'',
            		  filename:file.name,
                  indexInBlock: 0,
                  blockContainer: 0,
                  authorContainer:{}, //------------------------------------------TODO
            		  hashFile:sha,
            		  citations:[]
              }
              //scope.$apply();
            });

          }
        })(file);

        // Read in the image file as a data URL.
        reader.readAsDataURL(file);
      }
      });
    },function(message){
      $mdToast.show(
        $mdToast.simple()
        .textContent(message)
        .position('fil')
        //.position($scope.getToastPosition())
        .hideDelay(5000)
      );
    });
};

}])
