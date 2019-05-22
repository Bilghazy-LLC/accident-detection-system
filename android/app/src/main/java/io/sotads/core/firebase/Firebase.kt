package io.sotads.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.sotads.core.util.ACCIDENTS_REF
import io.sotads.core.util.Callback
import io.sotads.core.util.DRIVERS_REF
import io.sotads.core.util.EMT_REF
import io.sotads.data.Accident
import io.sotads.data.Driver
import io.sotads.data.EmtDataModel

class Firebase {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: Firebase? = null

        @JvmStatic
        fun get(): Firebase = instance ?: synchronized(this) {
            instance ?: Firebase().also { instance = it }
        }
    }

    fun getDrivers(callback: Callback<MutableList<Driver>>) {
        callback.onInit()
        firestore.collection(DRIVERS_REF)
            .limit(50)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val snapshot = it.result
                    if (snapshot != null) callback.onSuccess(snapshot.toObjects(Driver::class.java))
                    else callback.onError("Cannot get this collection of drivers")
                    callback.onComplete()
                    return@addOnCompleteListener
                } else {
                    callback.onError("Unable to retrieve all drivers")
                    callback.onComplete()
                }
            }.addOnFailureListener {
                callback.onError(it.localizedMessage)
                callback.onComplete()
            }
    }

    fun getAccidents(callback: Callback<MutableList<Accident>>) {
        callback.onInit()
        firestore.collection(ACCIDENTS_REF)
            .whereEqualTo("dispatched", true)
            .limit(50)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val snapshot = it.result
                    if (snapshot != null) callback.onSuccess(snapshot.toObjects(Accident::class.java))
                    else callback.onError("Cannot get this collection of accidents")
                    callback.onComplete()
                    return@addOnCompleteListener
                } else {
                    callback.onError("Unable to retrieve all accidents")
                    callback.onComplete()
                }
            }.addOnFailureListener {
                callback.onError(it.localizedMessage)
                callback.onComplete()
            }
    }

    fun getCurrentEmt(key: String, callback: Callback<EmtDataModel>) {
        callback.onInit()

        firestore.collection(EMT_REF)
            .document(key)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    callback.onSuccess(snapshot.toObject(EmtDataModel::class.java))
                    callback.onComplete()
                    return@addSnapshotListener
                }

                if (exception != null) {
                    callback.onError(exception.localizedMessage)
                    callback.onComplete()
                    return@addSnapshotListener
                }
            }
    }

    fun login()
}