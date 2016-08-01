module.exports = function(config){
  config.set({

    basePath : '../../',

    files : [ //Requires all used js files from the app itself aswell as angular modules (angular-resource.js etc.)
	'https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js',
      	'../assets/javascript/modules/angular.min.js',
      	'../assets/javascript/modules/*.js',
      	'tests/karma-jasmine-tests/angular-mocks.js',
	'../assets/javascript/*.js',
	'https://maps.googleapis.com/maps/api/js?libraries=places&sensor=false&key=AIzaSyA1ouRfRDPJsTHjpUd1IDzCJBCCjQxlu5M',
      	'app.js',
        'services/notificationServices/*.js',
      	'controllers/defaults/*.js',
      	'controllers/events/*.js',
      	'controllers/routes/*.js',
      	'controllers/travels/*.js',
      	'controllers/users/*.js',
      	'controllers/pois/*.js',
      	'translation_tables/*.js',
      	'directives/*.js',
      	'views/travels/*.html',
      	'views/routes/*.html',
      	'views/pois/*.html',
      	'views/events/*.html',
      	'views/users/*.html',
      	'services/resourceServices/_resources.js',
      	'services/resourceServices/*.js',
      	'services/authenticationServices/*.js',
      	'services/commonServices/*.js',
      	'tests/karma-jasmine-tests/*Spec.js',
    ],

    autoWatch : true,

    frameworks: ['jasmine'],
	
    reporters: [/*'progress',*/'mocha','coverage','html'],
	
	client: {
		mocha: {
			reporter:'reporter-file',
			ui:'tdd'	
		}
	},

	coverageReporter : {
 		 dir : 'tests/karma-jasmine-tests/coverage/',
		reporters: [
			{type: 'html', subdir: 'report-html'},
			{type:'lcovonly', subdir:'.'}]
	},
	
	htmlReporter: {
		outputDir:'karma-jasmine-tests',
		reportName:'karma_html'
	},

	preprocessors: {
		'views/travels/*.html':'ng-html2js',
		'views/routes/*.html':'ng-html2js',
		'views/pois/*.html':'ng-html2js',
		'views/users/*.html':'ng-html2js',
		'views/events/*.html':'ng-html2js',
      'controllers/defaults/*.js': 'coverage',
      'controllers/events/*.js': 'coverage',
      'controllers/routes/*.js': 'coverage',
      'controllers/travels/*.js': 'coverage',
      'controllers/users/*.js': 'coverage',
      'directives/*.js': 'coverage',
      'controllers/pois/*.js': 'coverage',
      'translation_tables/*.js': 'coverage',
      'services/authenticationServices/*.js': 'coverage',
      'services/resourceServices/resources.js': 'coverage',
      'services/resourceServices/*.js': 'coverage',
      'services/commonServices/*.js': 'coverage'
	},

	ngHtml2JsPreprocessor: {
		prependPrefix: 'app/',
    		moduleName: 'templates'
	},
	
	/*phantomjsLauncher: {
		exitOnResourceError:true
	},*/

    browsers : [/*'PhantomJS',*/'Chrome'],

    plugins : [
		'karma-ng-html2js-preprocessor',
            	'karma-chrome-launcher',
				'karma-phantomjs-launcher',
            	'karma-jasmine',
            	'karma-mocha-reporter',
		'karma-html-reporter',
		'karma-coverage'/*,
		'karma-phantomjs-launcher'*/
            ]

  });
};