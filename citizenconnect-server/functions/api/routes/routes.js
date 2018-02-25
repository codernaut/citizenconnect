var index = require('../../index')
module.exports = function(app){
    var notificationAPI = require('../controller/notifications/notification');
    var emailAPI = require('../controller/Email/SendEmail')

    app.post('/sendNotification',notificationAPI.sendNotification);
    app.post('/sendEmail',emailAPI.sendEmail);
            
}