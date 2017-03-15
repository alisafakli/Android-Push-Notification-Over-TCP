package com.example.androidclientservice.service;

import com.example.androidclientservice.R;
import com.example.androidclientservice.comm.Client;
import com.example.androidclientservice.comm.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

public class MyServices extends Service {

	// private Client mClient;
	public static TextView status;
	private boolean isServiceStarted = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Toast.makeText(this, "Service is created", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "Service is started", Toast.LENGTH_LONG).show();
		// if(!isServiceStarted){
		// Toast.makeText(this, "Service is started", Toast.LENGTH_LONG).show();
		// isServiceStarted = true;
		// }else{
		// Toast.makeText(this, "Service is running", Toast.LENGTH_LONG).show();
		// }

		// String message;
		// message = intent.getStringExtra("message");
		// Toast.makeText(this, "Message: "+message, Toast.LENGTH_LONG).show();

		// Intent intent2 = new Intent(getBaseContext(),
		// com.example.androidclientservice.comm.Home.class);
		// Log.e("ServerIP", Client.SERVERIP);
		// startActivity(intent);

		new connectTask().execute("");

		// Intent sendIntent = new Intent("Myservice");
		// sendIntent.putExtra("ClientMessage", message);
		// sendBroadcast(sendIntent);

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Service is stopped", Toast.LENGTH_LONG).show();
		// isServiceStarted = false;
		super.onDestroy();
	}

	// *****************************************************************
	// ** //
	// ////////////////////////THREAD///////////////////////////////////
	public class connectTask extends AsyncTask<String, String, Client> {

		private MainActivity ma;

		private static final int NOTIFICATION_ID = 0;
		private NotificationManager mNM;

		private int mId;

		@Override
		protected Client doInBackground(String... message) {

			// we create a Client object and
			MainActivity.mClient = new Client(new Client.OnMessageReceived() {

				// here the messageReceived method is implemented

				@Override
				public void messageReceived(String message) {
					// TODO Auto-generated method stub
					 pushNotification(message);
					publishProgress(message);
				}
			});

			MainActivity.mClient.run();

			// status
			// status.setText();
			// status.setText(Client.connectionStatus);
			return null;
		}
		
		@SuppressWarnings("deprecation")
		public void pushNotification(String message) {
			
			/*
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyServices.this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle("Server Message")
			        .setContentText(message);
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(MyServices.this, ServerNotification.class);

			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyServices.this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(ServerNotification.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, mBuilder.build());
			*/
			mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification n = new Notification();
			n.icon = R.drawable.ic_launcher;
			n.tickerText = "Server Message";
			n.when = System.currentTimeMillis();

			// Intent notificationIntent = new Intent(this,
			// ServerNotification.class);
			Intent notificationIntent = new Intent(MyServices.this,
					ServerNotification.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK |
			        Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(
					MyServices.this, 0, notificationIntent, 0);

			CharSequence contentText = message;
			CharSequence contentTitle = "Server Message";
			n.setLatestEventInfo(MyServices.this, contentTitle, contentText,
					contentIntent);

			mNM.notify(NOTIFICATION_ID, n);
			

		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			// in the arrayList we add the messaged received from server
			MainActivity.arrayList.add(values[0]);

			// notify the adapter that the data set has changed. This means that
			// new message received
			// from server was added to the list
			MainActivity.mAdapter.notifyDataSetChanged();
		}
	}

}
