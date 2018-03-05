var nodemailer = require('nodemailer');
exports.sendEmail = function (req, res) {
    var name = req.body.name;
    var contactNo = req.body.contactNo;
    var feedbackType = req.body.feedbackType;
    var message = req.body.message;


    if(name!=null && contactNo!=null && feedbackType!=null && message!=null){
        var transporter = nodemailer.createTransport({
            service: 'Gmail',
            auth: {

                user: 'xxxxxx',
                pass: 'xxxxxx'

            }
        });
        
        var mailOptions = {
            from: 'xxxxx', // sender address
            to: 'xxxxxx', // list of receivers
            subject: 'Hello ✔', // Subject line
            text: 'Hello world ✔', // plaintext body
            html: '<b>'+name+'</b>' // html body
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