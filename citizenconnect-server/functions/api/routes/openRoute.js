var index = require('../../index')
module.exports = function(app){

    var feedbackAPI = require('../controller/Email/SendFeedback')
    var complainAPI = require('../controller/Email/SendComplain')
    app.post('/sendFeedback',feedbackAPI.sendFeedback);
    app.post('/sendComplain',complainAPI.sendComplain);
            
}