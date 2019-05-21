// Initialize Firebase SDKs Here
let auth = firebase.auth();
let db = firebase.firestore();
let bucket = firebase.storage().ref();
let messaging = firebase.messaging();

$(document).ready(function () {
    console.log('Setting up SDKs...');


});


// Sign out any logged in user
const logout = () => {
    if (confirm('Do you wish to logout?')) {
        auth.signOut().then(() => {
            // Clear local storage for logged in user's UID
            window.localStorage.setItem('sot-user-id', null);
            // Navigate to the index page
            window.location = 'index.html';
        }).catch(err => {
            if (err) {
                console.log(err.message);
            }
        })
    }
};