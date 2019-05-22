const functions = require('firebase-functions');
const admin = require('firebase-admin');

// Initialize Firebase Application
admin.initializeApp();

// Notification for dispatch
exports.dispatchEmt = functions.firestore.document('accidents/{key}').onUpdate((change, context) => {
    var key = context.params.key;

    return admin.messaging().sendToTopic('emt-dispatch', {
        data: {
            title: 'Dispatch request received',
            message: '',
            key,
            timestamp: `${new Date().toDateString()}`
        }
    }).then(() => {
        return console.log('Dispatch notification sent successfully');
    }).catch(err => {
        if (err) {
            return console.log(err.message);
        }
    });
});