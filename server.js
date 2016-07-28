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
app.use(express.static(__dirname+'/node_modules'));
app.use(express.static(__dirname+'/node_modules'));

app.set('port', (process.env.PORT || 3000));

var server = app.listen(app.get('port'),function(){
	console.log("We have started our server on port " + app.get('port'))
});