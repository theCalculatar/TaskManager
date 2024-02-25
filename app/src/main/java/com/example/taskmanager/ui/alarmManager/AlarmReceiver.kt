package com.example.taskmanager.ui.alarmManager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.ui.task.TaskDetailsActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val channelId = intent.getStringExtra("CHANEL_ID") ?: return
        val itemId = intent.getLongExtra("itemId",-1L)?:return

        val taskIntent = Intent(context,TaskDetailsActivity::class.java)
        taskIntent.putExtra("taskId",itemId)
        taskIntent.putExtra("fromReceiver",true)

        val pendingIntent = PendingIntent.getActivity(context,
            itemId.toInt(),taskIntent,PendingIntent.FLAG_IMMUTABLE)

        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (channelId == "TASK_ID") {
                val builder = NotificationCompat.Builder(ctx, channelId)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Task Due!")
                    .setContentText("Its almost time to finish $message")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                notificationManager.notify(itemId.toInt(), builder.build())
            }else{
                val builder = NotificationCompat.Builder(ctx, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true)
                    .setContentTitle("To do!")
                    .setContentText("Lets get started with $message")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                notificationManager.notify(itemId.toInt(), builder.build())
            }
        }
    }
}