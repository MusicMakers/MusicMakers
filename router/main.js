var multer = require('multer');
var upload = multer({dest: './imports'});
var fs = require('fs');
module.exports = function(app){
	app.get('/',function(req,res){
		res.render('index');
	});
	app.get('/home',function(req,res){
		res.render('index');
	});
	app.post('/', upload.array('upload_files'), function(req, res){
		var spawn = require('child_process').spawn;
		var cmd = "java";
		var args = ["-jar",__dirname+"/../applications/MM.jar",
		__dirname+"/../"+req.files[0].path];
		var options = {cwd: __dirname+"/../downloads"};
		var child = spawn(cmd,args,options);
		
	});

};