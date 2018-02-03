var path = require("path");
const googleStorage = require('@google-cloud/storage')
const admin = require('firebase-admin')
var constants = require('../constants')

var serviceAccount = require('../configurations/ServiceKey.json')

const storage = googleStorage({
  projectId: constants.PROJECT_ID
});


admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  storageBucket: constants.STORAGE_BUCKET_URL,
  databaseURL: constants.DATABASE_URL,
});

exports.storageBucket = storage.bucket(constants.STORAGE_BUCKET_URL);

exports.FirebaseDatabaseRef = admin.database();

