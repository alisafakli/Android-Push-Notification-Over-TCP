package com.example.androidclientservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action =intent.getAction();
		if(action.equals("Myservice")){
			Toast.makeText(context, intent.getStringExtra("ClientMessage"), Toast.LENGTH_LONG).show();
		}
	}

}
