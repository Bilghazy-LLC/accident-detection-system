package io.sotads.core.firebase

import android.app.Activity
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.sotads.core.ADSApplication
import io.sotads.core.room.ADSDao
import io.sotads.core.theme.BaseActivity
import io.sotads.core.util.*
import io.sotads.data.Accident
import io.sotads.data.Driver
import io.sotads.data.EmtDataModel
import io.sotads.view.HomeActivity
import io.sotads.view.MainActivity
import kotlinx.coroutines.launch

class Firebase private constructor(private val app: ADSApplication) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val messaging: FirebaseMessaging = FirebaseMessaging.getInstance()

    companion object {
        @Volatile
        private var instance: Firebase? = null

        @JvmStatic
        fun get(app: ADSApplication): Firebase = instance ?: synchronized(this) {
            instance ?: Firebase(app).also { instance = it }
        }
    }

    /**
     * Get all [Driver]s
     */
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

    /**
     * Get all [Accident]s
     */
    fun getAccidents(host: Activity, callback: Callback<MutableList<Accident>>) {
        callback.onInit()
        firestore.collection(ACCIDENTS_REF)
            .whereEqualTo("available", true)
            .limit(50)
            .addSnapshotListener(host) { snapshot, exception ->
                if (exception != null) {
                    callback.onError(exception.localizedMessage)
                    callback.onComplete()
                    return@addSnapshotListener
                }

                if (snapshot != null) callback.onSuccess(snapshot.toObjects(Accident::class.java))
                else callback.onError("Cannot get this collection of accidents")
                callback.onComplete()
            }
    }

    fun getDriver(key: String, callback: Callback<Driver>) {
        callback.onInit()

        firestore.collection(DRIVERS_REF).document(key).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccess(it.result?.toObject(Driver::class.java))
                } else {
                    callback.onError("Could not retrieve this driver\'s information")
                }
                callback.onComplete()
            }.addOnFailureListener {
                callback.onError(it.localizedMessage)
                callback.onComplete()
            }
    }

    fun getAccident(key: String, callback: Callback<Accident>) {
        callback.onInit()

        firestore.collection(ACCIDENTS_REF).document(key).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccess(it.result?.toObject(Accident::class.java))
                } else {
                    callback.onError("Could not retrieve this accident\'s information")
                }
                callback.onComplete()
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

    /**
     * Sign in
     */
    fun login(context: BaseActivity, dao: ADSDao, callback: Callback<EmtDataModel>) {
        callback.onInit()

        auth.signInAnonymously().addOnFailureListener {
            callback.onError(it.localizedMessage)
            callback.onComplete()
        }.addOnCompleteListener {
            val authResult = it.result
            val user = authResult?.user
            if (user != null) {
                // Create model
                val model = EmtDataModel(user.uid)

                firestore.collection(EMT_REF).document(model.key)
                    .set(model).addOnCompleteListener {
                        // Store model in shared prefs
                        app.prefs.edit {
                            putString(USER_KEY, model.key)
                            apply()
                        }

                        app.ioScope.launch {
                            dao.createEMT(model)
                            app.uiScope.launch {
                                callback.onSuccess(model)
                                callback.onComplete()

                                context.intentTo(HomeActivity::class.java, true)
                            }
                        }
                    }.addOnFailureListener { exception ->
                        callback.onError(exception.localizedMessage)
                        callback.onComplete()
                    }

            }
        }.addOnFailureListener {
            callback.onError(it.localizedMessage)
            callback.onComplete()
        }
    }

    /**
     * Sign out
     */
    fun logout(context: BaseActivity, key: String) {
        auth.signOut()
        app.prefs.edit {
            putString(USER_KEY, null)
            apply()
        }
        context.intentTo(MainActivity::class.java, true)
    }

    fun subscribeToTopic() {
        try {
            messaging.subscribeToTopic(TOPIC_NAME).addOnCompleteListener {
                if (it.isSuccessful) debugLog("Subscribed to topic successfully")
                else debugLog("Unable to subscribe to emt topic")
            }.addOnFailureListener {
                debugLog(it.localizedMessage)
            }
        } catch (e: Exception) {
            debugLog(e.localizedMessage)
        }
    }

    fun acceptDispatch(key: String?, callback: Callback<Void>) {
        callback.onInit()

        if (!key.isNullOrEmpty()) {
            try {
                firestore.collection(ACCIDENTS_REF).document(key).update(
                    mapOf<String, Any?>(
                        "available" to false
                    )
                ).addOnFailureListener {
                    callback.onError(it.localizedMessage)
                    callback.onComplete()
                }.addOnCompleteListener {
                    callback.onSuccess(null)
                    callback.onComplete()
                }
            } catch (ex: Exception) {
                callback.onError(ex.localizedMessage)
                callback.onComplete()
            }
        }
    }
}