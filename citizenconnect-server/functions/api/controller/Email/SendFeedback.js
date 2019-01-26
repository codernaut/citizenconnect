var nodemailer = require('nodemailer');
var constants = require('../../../constants')
exports.sendFeedback = function (req, res) {
    var name = req.body.name;
    var contactNo = req.body.contactNo;
    var feedbackType = req.body.feedbackType;
    var message = req.body.message;
    var subject = req.body.subject;

    if(name!=null && contactNo!=null  && message!=null && subject!=null){
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
            subject: 'Feedback: '+subject, // Subject line
            text: 'i am Text body', // plaintext body
            html: '<b>Name: </b><br><br>'
            + name + '<br>Contact No:<b>'
            +contactNo +  '<br>Message : </b>' 
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