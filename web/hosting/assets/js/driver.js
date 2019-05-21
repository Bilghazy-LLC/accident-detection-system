let currentDriver = {};
var isNewDriver = false;
var marker;
var address;


// Maps
var mapOptions = {
    zoom: 19,
    center: {
        lat: 5.654614,
        lng: -0.1839739
    },
    scrollwheel: false, //we disable de scroll over the map, it is a really annoing when you scroll through page
    styles: [{
            "elementType": "geometry",
            "stylers": [{
                "color": "#1d2c4d"
            }]
        },
        {
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#8ec3b9"
            }]
        },
        {
            "elementType": "labels.text.stroke",
            "stylers": [{
                "color": "#1a3646"
            }]
        },
        {
            "featureType": "administrative.country",
            "elementType": "geometry.stroke",
            "stylers": [{
                "color": "#4b6878"
            }]
        },
        {
            "featureType": "administrative.land_parcel",
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#64779e"
            }]
        },
        {
            "featureType": "administrative.province",
            "elementType": "geometry.stroke",
            "stylers": [{
                "color": "#4b6878"
            }]
        },
        {
            "featureType": "landscape.man_made",
            "elementType": "geometry.stroke",
            "stylers": [{
                "color": "#334e87"
            }]
        },
        {
            "featureType": "landscape.natural",
            "elementType": "geometry",
            "stylers": [{
                "color": "#023e58"
            }]
        },
        {
            "featureType": "poi",
            "elementType": "geometry",
            "stylers": [{
                "color": "#283d6a"
            }]
        },
        {
            "featureType": "poi",
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#6f9ba5"
            }]
        },
        {
            "featureType": "poi",
            "elementType": "labels.text.stroke",
            "stylers": [{
                "color": "#1d2c4d"
            }]
        },
        {
            "featureType": "poi.park",
            "elementType": "geometry.fill",
            "stylers": [{
                "color": "#023e58"
            }]
        },
        {
            "featureType": "poi.park",
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#3C7680"
            }]
        },
        {
            "featureType": "road",
            "elementType": "geometry",
            "stylers": [{
                "color": "#304a7d"
            }]
        },
        {
            "featureType": "road",
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#98a5be"
            }]
        },
        {
            "featureType": "road",
            "elementType": "labels.text.stroke",
            "stylers": [{
                "color": "#1d2c4d"
            }]
        },
        {
            "featureType": "road.highway",
            "elementType": "geometry",
            "stylers": [{
                "color": "#2c6675"
            }]
        },
        {
            "featureType": "road.highway",
            "elementType": "geometry.fill",
            "stylers": [{
                "color": "#9d2a80"
            }]
        },
        {
            "featureType": "road.highway",
            "elementType": "geometry.stroke",
            "stylers": [{
                "color": "#9d2a80"
            }]
        },
        {
            "featureType": "road.highway",
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#b0d5ce"
            }]
        },
        {
            "featureType": "road.highway",
            "elementType": "labels.text.stroke",
            "stylers": [{
                "color": "#023e58"
            }]
        },
        {
            "featureType": "transit",
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#98a5be"
            }]
        },
        {
            "featureType": "transit",
            "elementType": "labels.text.stroke",
            "stylers": [{
                "color": "#1d2c4d"
            }]
        },
        {
            "featureType": "transit.line",
            "elementType": "geometry.fill",
            "stylers": [{
                "color": "#283d6a"
            }]
        },
        {
            "featureType": "transit.station",
            "elementType": "geometry",
            "stylers": [{
                "color": "#3a4762"
            }]
        },
        {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [{
                "color": "#0e1626"
            }]
        },
        {
            "featureType": "water",
            "elementType": "labels.text.fill",
            "stylers": [{
                "color": "#4e6d70"
            }]
        }
    ]
};
let map = new google.maps.Map(document.getElementById("map"), mapOptions);


// Add a new marker to the maps
const addMarker = (locationAddress, titleSnippet) => {
    var image = {
        url: 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png',
        // This marker is 20 pixels wide by 32 pixels high.
        size: new google.maps.Size(20, 32),
        // The origin for this image is (0, 0).
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at (0, 32).
        anchor: new google.maps.Point(0, 32)
    };

    // Shapes define the clickable region of the icon. The type defines an HTML
    // <area> element 'poly' which traces out a polygon as a series of X,Y points.
    // The final coordinate closes the poly by connecting to the first coordinate.
    var shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: 'poly'
    };


    marker = new google.maps.Marker({
        position: locationAddress,
        map: map,
        icon: image,
        shape,
        title: titleSnippet
    });
    marker.addListener('click', toggleBounce)
};

const toggleBounce = () => {
    if (marker.getAnimation() !== null) {
        marker.setAnimation(null);
    } else {
        marker.setAnimation(google.maps.Animation.BOUNCE);
    }
};

$(document).ready(function () {
    toggleSpinner();

    var username = $('#username');
    var email = $('#email');
    var contact = $('#contact');
    var carNumber = $('#car-number');

    var driverKey = window.localStorage.getItem('sot-driver');

    if (driverKey) {
        // Setup user information
        db.collection('drivers').doc(driverKey)
            .get().then(snapshot => {
                if (snapshot.exists) {
                    var driver = snapshot.data();
                    username.val(driver.name);
                    email.val(driver.email);
                    contact.val(driver.emergency_contact)
                    carNumber.val(driver.car_number);

                    if (driver.address) {
                        addMarker(new google.maps.LatLng(driver.address.latitude, driver.address.longitude), driver.name);
                    }
                } else {
                    showNotification('This driver does not exist');
                }
            }).catch(err => {
                if (err) {
                    showNotification(err.message);
                }
            });
    }
});

// Save driver information
const saveProfile = () => {
    var username = $('#username').val();
    var email = $('#email').val();
    var contact = $('#contact').val();
    var carNumber = $('#car-number').val();

    // Check the validity of all fields
    var isValid = !validator.isEmpty(username) && !validator.isEmpty(email) && !validator.isEmpty(contact) && !validator.isEmpty(carNumber);

    // Post data if valid
    if (isValid) {
        toggleSpinner(true);
        var driversRef = db.collection('drivers').doc(window.localStorage.getItem('sot-driver'))
        if (isNewDriver) {
            driversRef.update({
                name: username,
                email,
                emergency_contact: contact,
                car_number: carNumber
            }).then(() => {
                toggleSpinner(false);
                showNotification("Driver's profile updated successfully")
            }).catch(err => {
                if (err) {
                    toggleSpinner(false);
                    showNotification(`Unable to update this driver's information.\n${err.message}`);
                }
            });
        } else {
            driversRef.set({
                name: username,
                email,
                emergency_contact: contact,
                car_number: carNumber
            }).then(() => {
                toggleSpinner(false);
                showNotification("Driver's profile updated successfully");
                isNewDriver = false;
            }).catch(err => {
                if (err) {
                    toggleSpinner(false);
                    showNotification(`Unable to update this driver's information.\n${err.message}`);
                }
            });
        }

    } else {
        showNotification("Please enter this driver's information completely. Make sure to enter the full name, emergency contact, address etc before you proceed!")
    }
};

const addNewDriver = () => {
    isNewDriver = true;

    // Clear all fields
    $('#username').val('');
    $('#email').val('');
    $('#contact').val('');
    $('#car-number').val('');

    window.localStorage.setItem('sot-driver', null);
}