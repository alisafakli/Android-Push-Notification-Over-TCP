package com.example.androidclientservice.comm;

import java.net.InetAddress;
import java.util.ArrayList;

import com.example.androidclientservice.R;
import com.example.androidclientservice.service.MyServices;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView mList;
	public static ArrayList<String> arrayList;
	public static MyCustomAdapter mAdapter;
	public static Client mClient;
	private Button connect;
	private EditText ipAdress;
	public static TextView status;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// home
		ipAdress = (EditText) findViewById(R.id.editTextIP);
		connect = (Button) findViewById(R.id.btnBack);
		status = (TextView) findViewById(R.id.textViewStatus);
		ipAdress.setText("192.168.56.1");
		//status.setText("Disconnected");
		connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ip = ipAdress.getText().toString();
				Client.SERVERIP = ip;
				// Intent intent = new Intent(getBaseContext(),
				// MainActivity.class);
				// Log.e("ServerIP", Client.SERVERIP);
				// startActivity(intent);
				
				//**TASK
				//task = new connectTask();
				//task.execute("");
				startMethod(v);
				
				
				
				
				status.setText(Client.connectionStatus);

				//if (mClient != null && mClient.getConnectionStatus()) {
					//status.setText("Connected");
				//}
			}
		});
		// home-end

		arrayList = new ArrayList<String>();

		final EditText editTextRequest = (EditText) findViewById(R.id.editTextRequest);
		Button send = (Button) findViewById(R.id.send_button);

		// relate the listView from java to the one created in xml
		mList = (ListView) findViewById(R.id.list);
		mAdapter = new MyCustomAdapter(this, arrayList);
		mList.setAdapter(mAdapter);

		// connect to the server

		// editTextIP.setText("192.168.56.1");

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String message = editTextRequest.getText().toString();

				// add the text in the arrayList
				// arrayList.add("c: " + message);

				// sends the message to the server
				if (mClient != null) {
					mClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editTextRequest.setText("");
			}
		});

	}
	public void startMethod(View v){
		
		Intent i = new Intent(this, MyServices.class);
		i.putExtra("message", "this is test message");
		startService(i);
	}
	
	public void stopMethod(View v){
		Intent i = new Intent(this, MyServices.class);
		stopService(i);
	}

	
	/* Servisin icine gonderilen class
	public class connectTask extends AsyncTask<String, String, Client> {

		private MainActivity ma;

		@Override
		protected Client doInBackground(String... message) {

			// we create a Client object and
			mClient = new Client(new Client.OnMessageReceived() {
				@Override
				// here the messageReceived method is implemented
				public void messageReceived(String message) {
					// this method calls the onProgressUpdate
					publishProgress(message);
				}
			});
			mClient.run();


			// status
			// status.setText();
			//status.setText(Client.connectionStatus);
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			// in the arrayList we add the messaged received from server
			arrayList.add(values[0]);
			// notify the adapter that the data set has changed. This means that
			// new message received
			// from server was added to the list
			mAdapter.notifyDataSetChanged();
		}
	} */

}
