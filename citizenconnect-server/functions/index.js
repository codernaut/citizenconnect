var express = require('express')
var cors = require('cors')
const app = express();
var bodyParser = require('body-parser')
var notificationAPI = require('./api/routes/notificationRoutes')
var constants = require('./constants')

const functions = require('firebase-functions');
var util = require('util');
notificationAPI(app)

// Expose Express API as a single Cloud Function:
exports.notifications = functions.https.onRequest(app);