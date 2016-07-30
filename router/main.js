var multer = require('multer');
var upload = multer({dest: './imports'});
var fs = require('fs');
var uid = require('uid-safe');

module.exports = function(app){
	app.get('/',function(req,res){
		var sess = req.session;
		if(!sess.id){
			sess.id = uid.sync(18);

		}
		res.render('index');
	});
	app.post('/', upload.array('upload_files'), function(req, res){
		var sess = req.session;
		var spawn = require('child_process').spawn;
		var cmd = "java" ;
		var num_of_files = req.files.length;		
		var args = ["-jar",__dirname+"/../applications/MM improved.jar"];
		args.push(req.body.order, req.body.num_of_notes, req.body.tempo); //preferences: order, num_of_notes, tempo
		args.push(sess.id);
		args.push(num_of_files);
		for(var i = 0; i<num_of_files;i++){
			var loc = __dirname+"/../"+req.files[i].path
			args.push(loc);
		}
		var options = {cwd: __dirname+"/../downloads"};
		var child = spawn(cmd,args,options);
		console.log(child);
		res.send(sess.id);
	});
};