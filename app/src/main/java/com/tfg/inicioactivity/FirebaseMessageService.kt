package com.tfg.inicioactivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context;
import android.os.Build
import androidx.core.app.NotificationCompat

class FirebaseMessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("fcm", "Refreshed token: $token")
        FirebaseMessaging.getInstance().subscribeToTopic("ALERTS")
    }

    // Notificación que se verá en el móvil
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Prueba_Mensaje", "El mensaje 1 --> ${remoteMessage.data}")
        }
        remoteMessage.notification?.let {
            Log.d("Prueba_Mensaje", "El mensaje 2 --> ${it.body}")

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "id_canal"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Mensajeria_FCM",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(remoteMessage.notification!!.title) // Título del mensaje FCM
                .setContentText(remoteMessage.notification!!.body) // Cuerpo del mensaje FCM
                .setVibrate(longArrayOf(0, 1000, 500, 1000))
                .setAutoCancel(false)

            notificationManager.notify(1, notificationBuilder.build())
        }
    }
}