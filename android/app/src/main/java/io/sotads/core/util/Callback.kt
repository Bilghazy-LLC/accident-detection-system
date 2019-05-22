package io.sotads.core.util

/**
 * Callback interface for all async processes
 */
interface Callback<Type> {

    fun onInit() {}

    fun onComplete() {}

    fun onError(error: String?)

    fun onSuccess(response: Type?)
}