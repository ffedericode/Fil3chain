(function() {
	'use strict';
	/**
	 * Config
	 */
	var moduleName = 'config.speedDial.fil3chain';
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

	module.factory('SpeedDialConfig', SpeedDialServiceConfig);

	SpeedDialServiceConfig.$inject = [];
	function SpeedDialServiceConfig() {
		return[
		       {
		    	   states: [
		    	            'app.welcome',
		    	            'app.welcome.signin',
		    	            'app.welcome.signup'
		    	            ],
		    	            show:"true",
		    	            direction:"up",
		    	            class:"md-scale md-fab-bottom-right",
		    	            ngClass:"{ 'md-hover-full': hover }",
		    	            fabTrigger:{
		    	            	label:'Menu',
		    	            	icon:'icons/ic_menu_white_24px.svg',
		    	            	tooltip : {
		    	            		direction:'left',
		    	            		visible:'true',
		    	            		autohide:'false',
		    	            		label:'Menu'
		    	            	},
		    	            	action:{
			    	            	sref:"app.welcome",
		    	            		click:function(config){
		    	            			//console.log('FabController','in actionClicked this', this);
		    	            			//console.log('FabController','in actionClicked state', config);
		    	            			config.go(this.sref);
		    	            		}
		    	            	}
		    	            },
		    	            fabActions:[
		    	                        {
		    	                        	name: "Signin",
		    	                        	icon: "icons/ic_person_black_24px.svg",
		    	                        	direction: "left",
		    	                        	sref:"app.welcome.signin",
		    	                        	click:function(config){
		    	                        		//console.log('FabController','in actionClicked this', this);
		    	                        		//console.log('FabController','in actionClicked state', config);
		    	                        		config.go(this.sref);
		    	                        	}
		    	                        },
		    	                        {
		    	                        	name: "Signup",
		    	                        	icon: "icons/ic_person_add_black_24px.svg",
		    	                        	direction: "left",
		    	                        	sref:"app.welcome.signup",
		    	                        	click:function(config){
		    	                        		//log('FabController','in actionClicked this', this);
		    	                        		//console.log('FabController','in actionClicked state', config);
		    	                        		config.go(this.sref);
		    	                        	}
		    	                        }
		    	                        ]
		       },
		       {
		    	   states: [
		    	            'app.wallet.transactions',
		    	            ],
		    	            show:"true",
		    	            direction:"up",
		    	            class:"md-scale md-fab-bottom-right",
		    	            ngClass:"{ 'md-hover-full': hover }",
		    	            fabTrigger:{
		    	            	label:'Add Transaction',
		    	            	icon:'icons/ic_add_white_24px.svg',
		    	            	action:{
		    	            		click: function(state){
		    	            			//console.log('Action click',state);
		    	            			state.go('app.wallet.transactions.post')
		    	            		}
		    	            	}
		    	            },
		    	            fabActions:[]
		       },
		       {
		    	   states: ['default'],
		    	   show:'false',
		    	   direction:'',
		    	   class:'',
		    	   ngClass:'',
		    	   mouseenter:'',
		    	   mouseleave:'',
		    	   fabTrigger:{
		    		   label:'',
		    		   icon:'',
		    		   tooltip : {
		    			   direction:'',
		    			   visible:'',
		    			   autohide:'',
		    			   label:''
		    		   }
		    	   },
		    	   fabActions:[]
		       }
		       ];
	};


})();
