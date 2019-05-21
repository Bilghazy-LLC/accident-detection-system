$(document).ready(function () {

    // Load all drivers
    var drivers = loadDrivers();
    console.log(drivers);

});


const loadDrivers = () => {
    var drivers = [];
    db.collection('drivers').get().then(snapshots => {
        snapshots.forEach(doc => {
            // Check for nullity & existence
            if (doc && doc.exists) {
                // Add driver to the drivers list
                drivers.push(doc.data());
            }
        });
    }).catch(err => {
        if (err) {
            return console.log(err.message);
        }
    });
    return drivers;
};