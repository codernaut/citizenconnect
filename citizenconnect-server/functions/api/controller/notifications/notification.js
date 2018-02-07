var mFirebase = require('../../../configurations/firebase')
var HashMap = require('hashmap');
var fields_HashMap = new HashMap();
const _path = require('path');
const os = require('os');
const fs = require('fs');
const gcs = require('@google-cloud/storage')({ keyFilename: _path.join(__dirname,'ServiceKey.json') });
const Busboy = require('busboy');
var temp = require('fs-temp')
var constants = require('../../../constants')
var inspect = require('util').inspect;
var dateFormat = require('dateformat');
var now = new Date();
var filepath;
var fileMem;
var fileName;
var firebaseFileURL;
exports.sendNotification = function (req, res) {
  if (req.method === 'POST') {
    console.log("Value"+ req.body.name)
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
            saveFileURL(firebaseFileURL)
            res.json({
              "status": 200,
              "message": "File Uploaded"
            })
            
          })
          .catch(err => {
            console.error('ERROR:', err);
            res.json(err)
          });
      });
    });

    busboy.on('field', function (fieldname, val, fieldnameTruncated, valTruncated, encoding, mimetype) {
      console.log('Field [' + fieldname + ']: value: ' + inspect(val));
      fields_HashMap.set(fieldname,inspect(val))
    
    });

    busboy.end(req.rawBody);
  } else {

    res.status(405).end();
  }
};
function saveFileURL(fileUrl) {
  mFirebase.FirebaseDatabaseRef.ref('Notifications').push({
      filePath: fileUrl,
      date: dateFormat(now, "dddd, mmmm dS, yyyy, h:MM:ss TT"),
      description: fields_HashMap.get("description"),
      tag: fields_HashMap.get("tag")
  });
}