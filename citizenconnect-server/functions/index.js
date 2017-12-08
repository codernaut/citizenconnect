const functions = require('firebase-functions');
var FCM = require('fcm-push');
var serverKey = 'AAAAb6tZ1_Y:APA91bGU7sDMrtgMF1y_OIOWqVqMPMc_0RT25UmvUkq-RHdz9LWrd6nX4Lbjpn4RKKefu1cqO_2Cb8l9a-U5x5DMMsu6WQZ7IdYj6Mb9y8h0DsOTzUILSckywWlCFfHanESLq_rnIe0H';
var fcm = new FCM(serverKey);
exports.sanitizePost = functions.database
.ref('/posts/{pushId}')
.onWrite(event => {
    const post = event.data.val()
    if (post.sanitized) {
            return
    }
    console.log("Sanitizing new post "+ event.params.pushId)
    console.log(post)
    
    post.body = "Notification Sent to Mobile"
    sendNotification(post.body)
     return event.data.ref.set(post)
})
function sendNotification(msg){
    var message = {
        to: '/topics/news', // required fill with device token or topics
        data: {
            serveMessage: msg
        },
        notification: {
            title: 'Firebase',
            body: 'Database Change Alert'
        }
    };

    fcm.send(message, function(err, response){
        if (err) {
            console.log("Error in Sending notification :"+err);
        } else {
            console.log("Successfully sent with response: ", response);
        }
    });
}

