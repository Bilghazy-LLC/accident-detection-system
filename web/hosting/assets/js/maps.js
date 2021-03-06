// Maps
var mapOptions = {
    zoom: 19,
    center: new google.maps.LatLng(5.654614, -0.1839739),
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


var marker;
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
        shape: shape,
        animation: google.maps.Animation.DROP,
        zIndex: 5,
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
    $('#driver-details').hide();

    db.collection('accidents').orderBy('timestamp', 'desc').where('dispatched', '==', false)
        .get().then(snapshot => {
            toggleSpinner(false);
            snapshot.forEach(doc => {
                // Get data from snapshot
                var accident = doc.data();

                // Get driver key
                var driver = accident.driver.id;
                loadDriver(driver);
                loadAccident(accident);

                // Add marker to map
                addMarker({
                    lat: accident.location.latitude,
                    lng: accident.location.longitude
                }, 'Accident detected at this location');
            });

        }).catch(err => {
            if (err) {
                toggleSpinner(false);
                showNotification(`Unable to update this driver's information.\n${err.message}`);
                console.log(err.message);

            }
        });
});

var accidentId;
var emtId;
const loadAccident = (accident) => {
    accidentId = accident.key;

    var dropdown = $('#emt-dropdown');

    db.collection('emt').where('available', '==', true)
        .get().then(snapshot => {
            snapshot.forEach(doc => {
                var emt = doc.data();

                // Append EMT to dropdown
                dropdown.append(`<option data-href="${emt.key}">${emt.name}</option>`);
            });
        }).catch(err => {
            if (err) {
                showNotification(err.message);
            }
        });

    $(document).on('click', 'option[data-href]', function () {
        console.log(this.dataset.href);
        emtId = this.dataset.href;
    })

};

// Show driver's details
const loadDriver = (id) => {
    db.collection('drivers').doc(id).get().then(snapshot => {
        $('#driver-details').show();

        var username = $('#username');
        var email = $('#email');
        var contact = $('#contact');
        var carNumber = $('#car-number');

        var driver = snapshot.data();
        username.val(driver.name);
        email.val(driver.email);
        contact.val(driver.emergency_contact)
        carNumber.val(driver.car_number);
    }).catch(err => {
        if (err) {
            showNotification(err.message)
        }
    });
}

const sendDispatch = () => {
    if (accidentId) {
        toggleSpinner(true)
        db.collection('accidents').doc(accidentId)
            .update({
                available: false
            }).then(() => {
                toggleSpinner(false)
                showNotification('EMT dispatched successfully')
            }).catch(err => {
                if (err) {
                    toggleSpinner(false)
                    showNotification(err.message);
                }
            })
    } else {
        showNotification('Unable to dispatch EMT for this accident');
    }
};