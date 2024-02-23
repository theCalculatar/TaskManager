package com.example.taskmanager.ui.alarmManager

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.taskmanager.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val channelId = intent.getStringExtra("CHANEL_ID") ?: return
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (channelId == "TASK_ID") {
                val builder = NotificationCompat.Builder(ctx, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Task Due!")
                    .setContentText("Its almost time to finish $message")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                notificationManager.notify(1, builder.build())
            }else{
                val builder = NotificationCompat.Builder(ctx, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("To do!")
                    .setContentText("Lets get started with $message")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                notificationManager.notify(1, builder.build())
            }
        }
    }
}