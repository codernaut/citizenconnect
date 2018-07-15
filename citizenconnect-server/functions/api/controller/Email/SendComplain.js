var nodemailer = require('nodemailer');
var constants = require('../../../constants')
exports.sendComplain = function (req, res) {
    
    var message = req.body.message;
    var subject = req.body.subject;
    var dataSetName = req.body.dataSetName;
    var dataSetAddress = req.body.dataSetAddress;

    if( message!=null && subject!=null){
        var transporter = nodemailer.createTransport({
            service: 'Gmail',
            auth: {

                user: constants.EMAIL_SENDER,
                pass: constants.PASSWORD_SENDER

            }
        });
    
        var mailOptions = {
            from: constants.EMAIL_SENDER, // sender address
            to: constants.EMAIL_RECEIVER, // list of receivers
            subject: subject+ " Report", // Subject line
            text: 'i am Text body', // plaintext body
            html: '<b>Location Name: </b>'+dataSetName+'<br><b>Location Address: </b>'+dataSetAddress +'<br><b>Message : </b>' 
            + message
        };
        
        // send mail with defined transport object
        transporter.sendMail(mailOptions, function(error, info){
            if(error){
                res.json({
                    "Error":error
                })
                
            }
            res.json({
                "status":"succeess",
                "message":'Message sent: ' + info.response
            })    
        });
    }

}