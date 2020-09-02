package com.example.moviefilmroomdeferredtask;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.moviefilmroomdeferredtask.presentation.view.MainActivity;

public class Receiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;
    public static String NOTIFICATION_CHANNEL_ID = "Me channel";
    @Override
    public void onReceive(Context context, Intent intentRx) {
        String idMovie = intentRx.getAction();

        Log.d("log Receiver", "onReceive");
        Log.d("log Receiver", "action = " +  idMovie);


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("idMovie", idMovie);
        //intent.putExtra("picMovie", picMovie);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder( context, NOTIFICATION_CHANNEL_ID ) ;
        builder.setContentTitle( "Отложенные просмотры фильма") ;
        builder.setContentText("Пора смотреть кино!") ;
        builder.setSmallIcon(R.drawable.ic_watch_later_green_24dp ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.ic_watch_later_green_24dp, "Смотреть фильм", pendingIntent);
        builder.build() ;

        NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(context);
        notificationManager2.notify(0,builder.build());
    }
}
