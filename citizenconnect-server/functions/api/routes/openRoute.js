var index = require('../../index')
module.exports = function(app){

    var emailAPI = require('../controller/Email/SendEmail')
    app.post('/sendEmail',emailAPI.sendEmail);
            
}