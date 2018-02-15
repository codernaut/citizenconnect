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
                user: 'shahzaib.shahid414@gmail.com',
                pass: 'Speed1822414'
            }
        });
        
        var mailOptions = {
            from: 'shahzaib.shahid414@gmail.com', // sender address
            to: 'shahzaib.shahid414@gmail.com', // list of receivers
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