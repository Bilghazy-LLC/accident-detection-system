// Initialize Firebase SDKs Here
let auth = firebase.auth();
let db = firebase.firestore();
let bucket = firebase.storage().ref();
let messaging = firebase.messaging();

$(document).ready(function () {
    console.log('Setting up SDKs...');

    // Email and password
    var email = window.localStorage.getItem('sot-email');
    var password = window.localStorage.getItem('sot-password');

    auth.setPersistence(firebase.auth.Auth.Persistence.SESSION)
        .then(function () {
            // Existing and future Auth states are now persisted in the current
            // session only. Closing the window would clear any existing state even
            // if a user forgets to sign out.
            // New sign-in will be persisted with session persistence.
            if (email && password) {
                console.log('Logging in presistently');

                return auth.signInWithEmailAndPassword(email, password);
            } else {
                return null;
            }
        }).catch(function (error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;

            showNotification(errorMessage);
        });


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
const login = () => {
    var email = $('#email').val();
    var password = $('#password').val();

    if (validator.isEmpty(email)) {
        showNotification('Please enter a valid email address');
        return;
    } else if (validator.isEmpty(password) || password.length < 6) {
        showNotification('Please enter a valid password. It should be a t least six characters long')
        return;
    }

    toggleSpinner(true);
    // Sign in with new email and password
    auth.signInWithEmailAndPassword(email, password).catch(function (error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;

        if (errorCode === 'auth/user-not-found') {
            showNotification('User not found. Creating a new instance. Please wait...');
            // Create account if user does not exist
            auth.createUserWithEmailAndPassword(email, password).catch(function (error) {
                // Handle Errors here.
                var errorCode = error.code;
                var errorMessage = error.message;
                showNotification(errorMessage);
                return;

            }).then(userInfo => {
                if (userInfo && userInfo !== undefined) {
                    var user = userInfo.user;
                    db.collection('emt').doc(user.uid)
                        .set({
                            name: user.displayName,
                            email: user.email,
                            createdAt: new Date().getTime(),
                            key: user.uid
                        }).then(() => {
                            toggleSpinner(false);
                            window.localStorage.setItem('sot-email', email);
                            window.localStorage.setItem('sot-password', password);
                            window.localStorage.setItem('sot-user-id', user.uid);
                            window.location.href = 'dashboard.html';
                        })
                }
            });

        } else {
            toggleSpinner(false);
            showNotification(errorMessage);
        }
    }).then(userInfo => {
        var user = userInfo.user;
        if (user) {
            toggleSpinner(false);
            window.localStorage.setItem('sot-email', email);
            window.localStorage.setItem('sot-password', password);
            window.localStorage.setItem('sot-user-id', user.uid);
            window.location.href = 'dashboard.html';
        } else {
            showNotification('Cannot find user information');
        }
    });
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

const toggleSpinner = (state) => {
    var spinner = $('#overlay');
    if (state) {
        spinner.show();
    } else {
        spinner.hide();
    }
};