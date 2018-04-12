var mFirebase = require('../../../configurations/firebase')
var HashMap = require('hashmap');
var fields_HashMap = new HashMap();
const _path = require('path');
const os = require('os');
const fs = require('fs');
const gcs = require('@google-cloud/storage')({ keyFilename: _path.join(__dirname, 'ServiceKey.json') });
const Busboy = require('busboy');
var temp = require('fs-temp')
var constants = require('../../../constants')
var inspect = require('util').inspect;
var dateFormat = require('dateformat');
var FCM = require('fcm-push');
var serverKey = constants.SERVER_KEY;
var fcm = new FCM(serverKey);
var now = new Date();
var filepath;
var fileMem;
var fileName;
var firebaseFileURL;
exports.sendNotification = function (req, res) {
  if (req.method === 'POST') {
    console.log("Value" + req.body.name)
    const busboy = new Busboy({ headers: req.headers });
    const uploads = {}
    busboy.on('file', (fieldname, file, filename, encoding, mimetype) => {
      console.log(`File [${fieldname}] filename: ${filename}, encoding: ${encoding}, mimetype: ${mimetype}`);

      filepath = _path.join(os.tmpdir(), filename);
      uploads[fieldname] = { file: filepath }
      fileName = filename;
      file.pipe(fs.createWriteStream(filepath));

      console.log('Path: ' + filepath)

      busboy.on('finish', function () {

        mFirebase.storageBucket
          .upload(filepath)
          .then(() => {
            console.log(`File Uploaded`);
            const bucket = gcs.bucket(constants.STORAGE_BUCKET_NOTIFICATION);
            const file = bucket.file(fileName);
            return file.getSignedUrl({
              action: 'read',
              expires: '03-09-2491'
            })
          })
          .then(signedUrls => {
            firebaseFileURL = signedUrls[0]
            return Promise.all([
              saveFileURL(firebaseFileURL)
            ])
          })
          .then(() => {
           sendNotification(firebaseFileURL, res)
          })
          .catch(err => {
            console.error('ERROR:', err);
            res.json({
              "status": 500,
              "Error": err
            })
          });
      });
    });

    busboy.on('field', function (fieldname, val, fieldnameTruncated, valTruncated, encoding, mimetype) {
      console.log('Field [' + fieldname + ']: value: ' + inspect(val));
      fields_HashMap.set(fieldname, inspect(val))
    });

    busboy.end(req.rawBody);
  } else {

    res.status(405).end();
  }
};
function saveFileURL(fileUrl) {
  return new Promise((resolve, reject) => {
    mFirebase.FirebaseDatabaseRef.ref('Notifications').push({
      filePath: fileUrl,
      date: dateFormat(now, "dddd, mmmm dS, yyyy, h:MM:ss TT"),
      description: fields_HashMap.get("description"),
      tag: fields_HashMap.get("tag")
    }).then((response) => {
      resolve(response)
    }).catch(err => {
      reject(err)
    })
  })
}

function sendNotification(msg, res) {
  var message = {
    to: '/topics/notification',
    priority: "high",
    data: {
      serveMessage: msg
    },
  };

  fcm.send(message, function (err, response) {
    if (err) {
      res.json({
        "status": 403,
        "Error": err
      })
    }
    else {
      sendNotificationToIphone(msg, res)
    }
  });
}
function sendNotificationToIphone(msg,res ){
  var message = {
    to: '/topics/iphoneNotification',
    priority: "high",
    content_available: true,
    "notification":{
      "title":"ICT Citizen Connect",
      "body":"New update added click to view"
    }
  };

  fcm.send(message, function (err, response) {
    if (err) {
      res.json({
        "status": 403,
        "Error": err
      })
    }
    else {
      console.log('Message Sent')
      res.json({
        "status": 200,
        "Response": response,
        "Message":"Notification Sent!"
      })
    }
  });
}