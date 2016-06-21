var express = require('express');
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var session = require('express-session');
var app = express();

require('./router/main')(app);
app.set('views', __dirname+'/views');
app.set('view engine','ejs');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded());
app.use(cookieParser());
app.use(session({ secret: '$#%!@#@@#SSDASASDVV@@@@', key: 'sid'}));

// Serving the these file up to server
app.use(express.static(__dirname + '/assets'));
app.use(express.static(__dirname+'/node_modules/jquery/dist'));




var server = app.listen(3000,function(){
	console.log("We have started our server on port 3000: accessed it by localhost:3000")
});