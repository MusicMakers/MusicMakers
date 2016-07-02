var multer = require('multer');
var upload = multer({dest: './imports'});
module.exports = function(app){
	app.get('/',function(req,res){
		res.render('index');
	});
	app.get('/home',function(req,res){
		res.render('index');
	});
	app.post('/', upload.array('upload_files'), function(req, res){
		console.dir(req.files);
		console.log("uploaded");
	});

};