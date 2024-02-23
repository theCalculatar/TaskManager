//package com.example.taskmanager.ui.alarmManager
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import androidx.core.content.ContextCompat.getSystemService
//
//class NoficationChannels {
//    //To show the notification, we need to create a notification channel first.
//    val channelId = "TASK_ID"
//    val channelName = "Alarm"
//    val notificationManager = NotificationManager(Context.NOTIFICATION_SERVICE)
//    val channel = NotificationChannel(
//        channelId,
//        channelName,
//        NotificationManager.IMPORTANCE_HIGH
//    )
//    notificationManager.createNotificationChannel(channel)
//}