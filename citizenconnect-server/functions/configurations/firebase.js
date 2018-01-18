const googleStorage = require('@google-cloud/storage');
const Multer = require('multer');
const admin = require('firebase-admin')
var constants = require('../constants')

const storage = googleStorage({
    projectId: constants.PROJECT_ID,
    keyFilename: constants.KEY_FILE_NAME
  });

  exports.storageBucket = storage.bucket(constants.STORAGE_BUCKET_URL); 

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: constants.DATABASE_URL
  });

  exports.FirebaseDatabaseRef = admin.database();