module.exports = function(app){
	app.get('/',function(req,res){
	res.render('index');
	});
	app.get('/home',function(req,res){
	res.render('index');
	});
	app.get('/composer',function(req,res){
	res.render('composer');
	});
};