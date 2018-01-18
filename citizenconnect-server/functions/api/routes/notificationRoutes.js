var index = require('../../index')
module.exports = function(app){
    var notificationAPI = require('../controller/notifications/notification');

    app.post('/sendNotification',index.multer.single('file'),notificationAPI.sendNotification);
            
}