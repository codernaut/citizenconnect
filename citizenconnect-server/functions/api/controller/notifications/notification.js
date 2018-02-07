var mFirebase = require('../../../configurations/firebase')
const _path = require('path');
const os = require('os');
const fs = require('fs');
const Busboy = require('busboy');
var temp = require('fs-temp')
var constants = require('../../../constants')
var inspect = require('util').inspect;
var filepath;
var fileMem;
var fileName;
exports.sendNotification = function (req, res) {
  if (req.method === 'POST') {
    const busboy = new Busboy({ headers: req.headers });
    // This object will accumulate all the uploaded files, keyed by their name
    const uploads = {}

    // This callback will be invoked for each file uploaded
    busboy.on('file', (fieldname, file, filename, encoding, mimetype) => {
      console.log(`File [${fieldname}] filename: ${filename}, encoding: ${encoding}, mimetype: ${mimetype}`);

      filepath = _path.join(os.tmpdir(), filename);
      uploads[fieldname] = { file: filepath }
     // console.log(`Saving '${fieldname}' to ${filepath}`);
      var path = temp.writeFileSync(file);
      var fstream = fs.createWriteStream(filepath);
      console.log('Path: '+filepath)
      fstream.on('finish', function () {

        const thumbFilePath = _path.join(_path.dirname("gs://citizenconnect-ed5fa.appspot.com/Notifications"), filename);
          mFirebase.storageBucket.upload(filepath, { destination: thumbFilePath });
      });
      file.pipe(fstream);
    });


    busboy.on('field', function (fieldname, val, fieldnameTruncated, valTruncated, encoding, mimetype) {
      console.log('Field [' + fieldname + ']: value: ' + inspect(val));
    });

    busboy.end(req.rawBody);
  } else {
    // Client error - only support POST
    res.status(405).end();
  }
};