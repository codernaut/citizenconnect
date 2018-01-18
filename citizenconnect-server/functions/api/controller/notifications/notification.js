var mFirebase = require('../../../configurations/firebase')
exports.sendNotification = function(req,res){
    console.log("Request :"+req.file + " and "+req.files)
    let file = req.file;
    if (file) {
        console.log("Calling -> uploadImageToStorage ")
        uploadImageToStorage(file).then((success) => {
          res.status(200).send({
            status: 200,
            "Message": success
          });
        },(fail)=>{
            console.log("Request Failed")
            res.status(500).send({
                status: 500,
                "Message": "Uploading Failed"
            })
        }).catch((error) => {
          console.error(error);
        });
      }
      else{
        console.log("Request Received,File not found")
          res.status(500).send({
              status: 500,
              "Message": "File not found"
          })
      }
};

/**
 * Upload the image file to Google Storage
 * @param {File} file object that will be uploaded to Google Storage
 */
const uploadImageToStorage = (file) => {
    let prom = new Promise((resolve, reject) => {
        console.log("Uploading File")
      if (!file) {
        console.log("File not found")
        reject('No image file');
      }
      let newFileName = `${file.originalname}_${Date.now()}`;
  
      let fileUpload = mFirebase.storageBucket.file(newFileName);
  
      const blobStream = fileUpload.createWriteStream({
        metadata: {
          contentType: file.mimetype
        }
      });
  
      blobStream.on('error', (error) => {
        console.log("Something is wrong! Unable to upload at the moment.")
        reject('Something is wrong! Unable to upload at the moment.');
      });
  
      blobStream.on('finish', () => {
        const url = format(`https://storage.googleapis.com/${mFirebase.storageBucket.name}/${fileUpload.name}`);
        resolve(url);
      });
  
      blobStream.end(file.buffer);
    });
    return prom;
  }