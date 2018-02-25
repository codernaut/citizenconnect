var express = require('express')
var cors = require('cors')
const app = express();
var bodyParser = require('body-parser')
var routes = require('./api/routes/routes')
var constants = require('./constants')
const functions = require('firebase-functions');

app.use(bodyParser.urlencoded({ extended: false }))
 
// parse application/json
app.use(bodyParser.json())

var util = require('util');
routes(app)

// Expose Express API as a single Cloud Function:
exports.notifications = functions.https.onRequest(app);