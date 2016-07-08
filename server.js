var express = require('express');
var session = require('express-session');
var cookieSession = require('cookie-session')
var bodyParser  = require('body-parser');

var app = express();

app.use(cookieSession({
	name: "MMcookie",
	secret: '$#%!@#@@#SSDASASDVV@@@@', 
	maxAge: 600000
}));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	extended: true
}));

app.set('views', __dirname+'/views');
app.set('view engine','ejs');
require('./router/main')(app);

// Serving these file up to server
app.use(express.static(__dirname + '/assets'));
app.use(express.static(__dirname + '/downloads'));
app.use(express.static(__dirname+'/node_modules/jquery/dist'));
app.use(express.static(__dirname+'/node_modules/jquery-file-download/src/scripts'));

var server = app.listen(3000,function(){
	console.log("We have started our server on port 3000: accessed it by localhost:3000")
});