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

// Login as a driver
const loginDriver = () => {
    var email = $('#email').val();
    var password = $('#password').val();

    // Create account if user does not exist
    auth.createUserWithEmailAndPassword(email, password).catch(function (error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        showNotification(errorMessage);
    });
};

// Login as an Admin/EMT
const loginAdmin = () => {

};


const showNotification = function (message) {
    color = Math.floor((Math.random() * 4) + 1);

    $.notify({
        icon: "tim-icons icon-bell-55",
        message: message
    }, {
        type: type[color],
        timer: 8000,
        placement: {
            from: 'top',
            align: 'left'
        }
    });
};