'use strict';

/**
* @ngdoc overview
* @name blockchain
* @description
* # blockchain
*
* Main module of the application.
*/
angular
.module('blockchainApp', [
  'chart.js',
  'ngAnimate',
  'ngAria',
  'ngCookies',
  'ngMaterial',
  'ngMessages',
  'ngResource',
  'ngResource',
  'ngRoute',
  'ui.router',
  'fil3chain'
])
.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
  .state('app', {
    abstract: true,
    views: {
      "master": {
        templateUrl: 'views/application.html',
      }
    }
  })
  .state('app.welcome', {
    url: '/welcome',
    views: {
      "content@app": {
        templateUrl: 'views/welcome.html'
      }
    }
  })
  .state('app.welcome.signin', {
    url: "/signin",
    views: {
      "content@app.welcome": {
        templateUrl: 'views/welcome.signin.html',
        controller: 'signinCtrl'
      }
    }
  })
  .state('app.welcome.signup', {
    url: "/signup",
    views: {
      "content@app.welcome": {
        templateUrl: 'views/welcome.signup.html',
        controller:'signupCtrl'
      }
    }
  })

  .state('app.wallet', {
    url: "/wallet",
    abstract:true
  })
  .state('app.wallet.profile', {
    url: "/profile",
    views: {
      "content@app": {
        templateUrl: 'views/wallet.profile.html',
        controller:'userProfileCtrl'
      }
    }
  })
  .state('app.wallet.transactions', {
    url: "/transactions",
    views: {
      "content@app": {
        templateUrl: 'views/wallet.transactions.html',
        controller:'walletTransactionCtrl'
      }
    },
    resolve:{
    	transactions:['$state','$mdToast','$q','TransactionService',function($state, $mdToast,$q, TransactionService){
    		   return TransactionService.get()
    		   .then(function(transactions){
    			   return transactions;
    		   },function(error){
    			  return undefined;
    		   });
    	}],
    	transactionHeaders:['TransactionService',function(TransactionService){
    		return TransactionService.getTransactionHeader();
    	}]
    }
  })
  .state('app.wallet.transactions.post', {
    url: "/post",
    views: {
      "content@app": {
        templateUrl: 'views/wallet.transactions.post.html',
        controller:'walletTransactionPostCtrl'
      }
    },
    resolve:{
    	transactionHeaders:['TransactionService',function(TransactionService){
    		return TransactionService.getTransactionHeader();
    	}]
    }
  })
  .state('app.wallet.statistics', {
    url: "/statistics",
    views: {
      "content@app": {
        templateUrl: 'views/wallet.statistics.html',
        controller:'walletStatisticsCtrl'
      }
    }
  })
  .state('app.miner', {
    url: "/miner",
    views: {
      "content@app": {
        templateUrl: 'views/miner.html',
        controller:'minerCtrl'
      }
    },
    resolve:{
    	ips:['MinerService',function(MinerService){
    		   return MinerService.getIps()
    		   .then(function(ips){
    			   return ips;
    		   },function(error){
    			  return undefined;
    		   });
    	}],
    	miningCheck:['MinerService',function( MinerService ){
	 		   return MinerService.check()
	 		   .then(function(ips){
	 			   return ips;
	 		   },function(error){
	 			  return undefined;
	 		   });
	 	}]
    }
  })

  .state('app.fil3chain', {
    url: "/fil3chain",
    abstract: true
  })
  .state('app.fil3chain.dashboard', {
    url: "/dashboard",
    views: {
      "content@app": {
        templateUrl: 'views/fil3chain.dashboard.html',
        controller:'walletStatisticsCtrl'
      }
    }
  })
  .state('app.fil3chain.statistics', {
    url: "/statistics",
    views: {
      "content@app": {
        templateUrl: 'views/fil3chain.statistics.html',
        controller:'walletStatisticsCtrl'
      }
    }
  })
  .state('app.fil3chain.transactions', {
    url: "/transactions",
    views: {
      "content@app": {
        templateUrl: 'views/fil3chain.transactions.html',
        controller:'walletTransactionCtrl'
      }
    },
    resolve:{
      transactions:['$state','$mdToast','$q','TransactionService',function($state, $mdToast,$q, TransactionService){
           return TransactionService.get()
           .then(function(transactions){
             return transactions;
           },function(error){
            return undefined;
           });
      }],
      transactionHeaders:['TransactionService',function(TransactionService){
        return TransactionService.getTransactionHeader();
      }]
    }
  })


  /*
  .state('app', {
  abstract: true,
  url: '/app/:userId',
  resolve:{
  userId: ['$stateParams',function($stateParams){
  return $stateParams.userId;
}]
},
views: {
"master": {
templateUrl: 'views/app.master/application.html',
controller: 'applicationCtrl'
},
"sidenav@app": {
templateUrl: 'views/app.sidenav/sidenav.html',
controller: 'SidenavAppCtrl'
},
"navbar@app": {
templateUrl: 'views/app.navbar/navbar.html',
controller: 'NavbarAppCtrl'
}
}
})
.state('app.wallet', {
url: '/wallet',
views: {
'navbar@app':{
templateUrl: 'views/app.wallet.filters/filters.html',
controller: 'filtersWalletCtrl'
}
}
})
.state('app.dashboard', {
url: '/dashboard',
views: {
"content@app": {
templateUrl: 'views/app.dashboard/dashboard.html'
},
"filters@app.dashboard": {
templateUrl: 'views/app.dashboard.filters/filters.tpl.html',
controller: 'filtersAppDashboardCtrl'
}
},
label:'Dashboard'
})
.state('app.copyright', {
url: '/copyright',
templateUrl: 'views/copyright/copyright.html',
controller: 'copyrightCtrl',
label: 'Copyright',
data: {
displayName: 'Copyright'
}
})
/*
.state('app.wallet', {
url: '/wallet',
templateUrl: 'views/wallet/wallet.html',
controller: 'copyrightCtrl'
})
*/

$urlRouterProvider.otherwise("/welcome");

})
.run(['$rootScope',function ($rootScope) {

}]);
/*
.run(['$rootScope','UserService','contextService','sidenavService',function($rootScope,UserService,contextService,sidenavService){
console.log('Run function');
console.log('UserService',UserService);
UserService.Create({name:'ivan18',surname:'ivan18',email:'ivan18@ivan18.it',username:'ivan18',password:'ivan18'});
console.log('ContextService',contextService);
contextService.post('welcomeFab',{
actions:[
{ name: "Signin", icon: "icons/ic_person_black_24px.svg", direction: "left",sref:"welcome.signin" },
{ name: "Signup", icon: "icons/ic_person_add_black_24px.svg", direction: "left",sref:"welcome.signup" }
]
});
contextService.post('sidenav',[{
state : 'app.dashboard',
label: 'Dashboard',
icon: 'icons/ic_dashboard_black_24px.svg'
},
{
state : 'app.copyright',
label: 'Copyright',
icon: 'icons/ic_copyright_black_24px.svg'
},
{
state : 'app.statistics',
label: 'Statistics',
icon: 'icons/graph_black.svg'
},
{
state : 'app.wallet',
label: 'Wallet',
icon: 'icons/user_black.svg'
}
]);
contextService.post('navbar',{
title:'',
links:[{
state : 'app.dashboard',
label: 'Dashboard',
icon: 'icons/ic_dashboard_black_24px.svg'
},
{
state : 'app.copyright',
label: 'Copyright',
icon: 'icons/ic_copyright_black_24px.svg'
},
{
state : 'app.statistics',
label: 'Statistics',
icon: 'icons/graph_black.svg'
},
{
state : 'app.wallet',
label: 'Wallet',
icon: 'icons/user_black.svg'
}],
actions:[{
label: 'Search',
icon: 'icons/ic_search_white_24px.svg',
click:function(ev){console.log(ev)}
}]
});
console.log('Context',contextService.get());
console.log($rootScope)
}])
.factory('context', ['$rootScope',function($rootScope) {
$rootScope.context = {};
return   $rootScope.context;
}])
.service('contextService',['context', function(context) {

this.get = function(name){
if(name) return context[name];
return context;
};
this.post=function(name, model){
return context[name]=model;
};
}])
.directive('myTabs', function() {
return {
restrict: 'E',
transclude: true,
scope: {},
controller: ['$scope', function($scope) {
var panes = $scope.panes = [];

$scope.select = function(pane) {
angular.forEach(panes, function(pane) {
pane.selected = false;
});
pane.selected = true;
};

this.addPane = function(pane) {
if (panes.length === 0) {
$scope.select(pane);
}
panes.push(pane);
};
}],
templateUrl: 'views/my-tabs.html'
};
})
.directive('myPane', function() {
return {
require: '^^myTabs',
restrict: 'E',
transclude: true,
scope: {
title: '@'
},
link: function(scope, element, attrs, tabsCtrl) {
tabsCtrl.addPane(scope);
},
templateUrl: 'views/my-pane.html'
};
});
*/
