var express = require('express')
const cors = require('cors')({origin: true});
const app = express();
const appV2 = express();
var bodyParser = require('body-parser')
var routes = require('./api/routes/routes')
var openRoute  = require('./api/routes/openRoute')
var constants = require('./constants')
const cookieParser = require('cookie-parser')()
var util = require('util')
const functions = require('firebase-functions');
var mFirebase = require('./configurations/firebase');

app.use(bodyParser.urlencoded({ extended: false }))
const validateFirebaseIdToken = (req, res, next) => {
    console.log('Check if request is authorized with Firebase ID token');
  
    if ((!req.headers.authorization || !req.headers.authorization.startsWith('Bearer ')) &&
        !req.cookies.__session) {
      console.error('No Firebase ID token was passed as a Bearer token in the Authorization header.',
          'Make sure you authorize your request by providing the following HTTP header:',
          'Authorization: Bearer <Firebase ID Token>',
          'or by passing a "__session" cookie.');
      res.status(403).send('Unauthorized');
      return;
    }
  
    let idToken;
    if (req.headers.authorization && req.headers.authorization.startsWith('Bearer ')) {
      console.log('Found "Authorization" header');
      // Read the ID Token from the Authorization header.
      idToken = req.headers.authorization.split('Bearer ')[1];
    } else {
      console.log('Found "__session" cookie');
      // Read the ID Token from cookie.
      idToken = req.cookies.__session;
    }
    mFirebase.firebaseAdmin.auth().verifyIdToken(idToken).then((decodedIdToken) => {
      console.log('ID Token correctly decoded', decodedIdToken);
      req.user = decodedIdToken;
      return next();
    }).catch((error) => {
      console.error('Error while verifying Firebase ID token:', error);
      res.status(403).send('Unauthorized');
    });
  };
 
  //Authenticated Endpoint
app.use(bodyParser.json())
app.use(cookieParser)
app.use(cors)
app.use(validateFirebaseIdToken);
routes(app)
exports.notifications = functions.https.onRequest(app);

//endpoint without Authentication 
appV2.use(bodyParser.json())
appV2.use(cookieParser)
appV2.use(cors)
openRoute(appV2)
exports.openEndpoints = functions.https.onRequest(appV2)
