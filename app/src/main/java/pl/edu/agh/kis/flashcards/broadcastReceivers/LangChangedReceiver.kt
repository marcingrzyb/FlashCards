package pl.edu.agh.kis.flashcards.broadcastReceivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.module.main.activity.MainActivity
import kotlin.random.Random

class LangChangedReceiver: BroadcastReceiver() {
    val CHANNEL_ID = "10001"
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.default_icon_foreground) //todo change icon
            .setContentTitle(context.getString(R.string.langChangedNotificationTitle))
            .setContentText(context.getString(R.string.langChangedNotificationText))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        createNotificationChannel(context,"FlashCardsChannel","baseChannel")
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(Random(997).nextInt(), builder.build())
        }
    }
    private fun createNotificationChannel(context: Context?,name: String,
                                          description: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                setDescription(description)
            }
            // Register the channel with the system
            val notificationManager = context!!.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}