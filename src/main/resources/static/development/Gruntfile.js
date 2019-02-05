// Wire Bower dependencies to your source code.
var wiredep = require('wiredep');

module.exports = function(grunt){
  var package = grunt.file.readJSON('package.json');
  //This task build css from sass
  grunt.loadNpmTasks('grunt-contrib-sass');
  //This task write scripts and styles in the index.html
  grunt.loadNpmTasks('grunt-include-source');
  //This task copies files from a directory to another
  grunt.loadNpmTasks('grunt-contrib-copy');
  //grunt.loadNpmTasks('grunt-html-build');

  grunt.initConfig({
    pkg: package,
    sass:{
      dist:{
        files:{
          '<%= grunt.config("pkg.dev.basePath") %>/styles/css/main.css':'<%= grunt.config("pkg.dev.basePath") %>/styles/scss/main.scss',
          '<%= grunt.config("pkg.dev.basePath") %>/styles/css/welcome.css':'<%= grunt.config("pkg.dev.basePath") %>/styles/scss/welcome.scss',
          '<%= grunt.config("pkg.dev.basePath") %>/styles/css/application.css':'<%= grunt.config("pkg.dev.basePath") %>/styles/scss/application.scss',
          '<%= grunt.config("pkg.dev.basePath") %>/styles/css/wallet.transactions.css':'<%= grunt.config("pkg.dev.basePath") %>/styles/scss/wallet.transactions.scss'

        }
      }
    },
    includeSource: {
      options: {
        basePath: '<%= grunt.config("pkg.dev.basePath") %>',
        baseUrl: '',
        /*
        templates: {
        html: {
        js: '<script src="{filePath}"></script>',
        css: '<link rel="stylesheet" type="text/css" href="{filePath}" />',
      }
    }*/
  },
  dist: {
    files: {
      '<%= grunt.config("pkg.dev.main") %>': '<%= grunt.config("pkg.dev.main") %>'
    }
  }
  /*
  myTarget: {
  files: {
  'dist/index.html': 'development/index.html'
}
}*/
},
copy: {
  backup: {
    expand: true,
    nonull: true,
    src: '<%= grunt.config("pkg.dev.basePath") %>/**/*',
    dest: 'versions/<%= grunt.template.today("yyyy-mm-dd-HH:MM") %>/',
  },
  toProduction: {
    expand: true,
    nonull: true,
    cwd: '<%= grunt.config("pkg.dev.basePath") %>',
    src: '**',
    dest: '<%= grunt.config("pkg.prod.basePath") %>/',
  }
}
});




grunt.registerTask('wiredep','Write bower dependencies on the index.html',function(){
  //The path of index.html is defined in package.json's main field.
  var wiredepConfig = {src: grunt.config('pkg.dev.main')};
  wiredep( wiredepConfig );
});

grunt.registerTask('default', ['sass','wiredep','includeSource:dist']);
}
