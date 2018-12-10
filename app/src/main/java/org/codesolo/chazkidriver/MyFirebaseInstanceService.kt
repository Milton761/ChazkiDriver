package org.codesolo.chazkidriver

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseInstanceService : FirebaseMessagingService() {

    val tag = this.javaClass.name

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.i(tag, "From: " + remoteMessage!!.from!!)
        Log.i(tag, "Notification Message Body: " + remoteMessage.notification!!.body!!)
    }






}
