var path = require("path");
const Storage = require('@google-cloud/storage')
const admin = require('firebase-admin')
var constants = require('../constants')

var serviceAccount = require('../configurations/ServiceKey.json')

const storage = new Storage();


admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  storageBucket: constants.STORAGE_BUCKET_URL,
  databaseURL: constants.DATABASE_URL,
});

exports.storageBucket = storage.bucket(constants.STORAGE_BUCKET_NOTIFICATION);

exports.FirebaseDatabaseRef = admin.database();

