var multer = require('multer');
var upload = multer({dest: './imports'});
var fs = require('fs');
var uid = require('uid-safe');

module.exports = function(app){
	app.get('/',function(req,res){
		var sess = req.session;
		if(!sess.id){
			console.log("first: "+sess.id);
			sess.id = uid.sync(18);

		}
		console.log("2nd: "+sess.id);
		res.render('index');
	});
	app.post('/', upload.array('upload_files'), function(req, res){
		var sess = req.session;
		var spawn = require('child_process').spawn;
		var cmd = "java" ;
		var args = ["-jar",__dirname+"/../applications/MM.jar",
		__dirname+"/../"+req.files[0].path /* + sess.id */];
		var options = {cwd: __dirname+"/../downloads"};
		var child = spawn(cmd,args,options);
		res.send(sess.id);
	});

};