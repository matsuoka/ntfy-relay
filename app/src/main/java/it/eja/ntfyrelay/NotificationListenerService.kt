// Copyright (C) 2024 by Ubaldo Porcheddu <ubaldo@eja.it>

package it.eja.ntfyrelay

import android.os.Build;
import android.app.Notification
import android.app.NotificationManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListenerService : NotificationListenerService() {
    companion object {
        private const val SUPPRESSION_INTERVAL = 1_000
        private const val PACKAGENAME_NTFY = "io.heckel.ntfy"
    }

    private var lastNotificationKey: String? = null
    private var lastNotificationTime: Long = 0
    private val sender = NotificationSender()

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        val packageName = sbn.packageName
        if (packageName == PACKAGENAME_NTFY) {
            return
        }
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE, "")
        val text = extras.getString(Notification.EXTRA_TEXT, "")
        Log.d("NotificationListenerService", "Title: $title, Text: $text")

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.channelId ?: ""
        } else {
            ""
        }
        if (channelId.isNotEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val channel = manager?.getNotificationChannel(channelId)
            if (channel != null &&
                (channel.importance == NotificationManager.IMPORTANCE_LOW ||
                        channel.importance == NotificationManager.IMPORTANCE_MIN)
            ) {
                return
            }
        }
        //val tag = sbn.tag ?: ""
        //val notificationKey = "$packageName|$channelId|$tag|$title|$text"
        val notificationKey = "$packageName|$channelId|$title|$text"
        val now = System.currentTimeMillis()

        synchronized(this) {
            if (notificationKey == lastNotificationKey && (now - lastNotificationTime < SUPPRESSION_INTERVAL)) {
                return  // 抑制
            }
            lastNotificationKey = notificationKey
            lastNotificationTime = now
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val url = sharedPreferences.getString("URL", "")
        var active: Boolean = sharedPreferences.getBoolean("ACTIVE", false)

        if (active && url != null && url.toString() != "") {
            val thread = Thread {
                try {
                    sender.sendNotification(url, title, text)
                } catch (e: Exception) {
                    Log.e("NotificationListenerService", "Error sending notification: ${e.message}")
                }
            }
            thread.start()
        }
    }
}