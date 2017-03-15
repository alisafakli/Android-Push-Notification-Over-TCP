package com.example.androidclientservice.comm;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import com.example.androidclientservice.R;

public class Client {

	private String serverMessage;
	public static String SERVERIP; // your computer IP
									// address
	public static final int SERVERPORT = 2222;
	private OnMessageReceived mMessageListener = null;
	private boolean mRun = false;
	protected MainActivity context;
	PrintWriter out;
	BufferedReader in;
	public static  String connectionStatus = "";
	
	
	//This is for update connection status
	public Client(Context context){
		this.context = (MainActivity) context;
	}

	public boolean getConnectionStatus() {
		return this.mRun;
	}
	
	public void UpdateConnectionStatus(final String status){

		context.status.setText(connectionStatus);
        
   }

	/**
	 * Constructor of the class. OnMessagedReceived listens for the messages
	 * received from server
	 */
	public Client(OnMessageReceived listener) {
		mMessageListener = listener;

	}

	/**
	 * Sends the message entered by client to the server
	 * 
	 * @param message
	 *            text entered by client
	 */
	public void sendMessage(String message) {
		if (out != null && !out.checkError()) {
			out.println(message);
			out.flush();
		}
	}

	public void stopClient() {
		mRun = false;
	}

	public void run() {

		try {

			// here you must put your computer's IP address.
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			Log.e("serverAddr", serverAddr.toString());
			Log.e("TCP Client", "C: Connecting...");
			connectionStatus = "Connecting...";
			//UpdateConnectionStatus(connectionStatus);
			// create a socket to make the connection with the server
			Socket socket = new Socket(serverAddr, SERVERPORT);
			Log.e("TCP Server IP", SERVERIP);
			try {

				// send the message to the server
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);

				Log.e("TCP Client", "C: Sent.");

				Log.e("TCP Client", "C: Done.");
				connectionStatus = "Connected";
				//UpdateConnectionStatus(connectionStatus);
				//context.status.setText("Connected");
				//MainActivity.status.setText((new Client(this.context)).);
				mRun = true;
				

				// receive the message which the server sends back
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));

				// in this while the client listens for the messages sent by the
				// server
				while (mRun) {
					serverMessage = in.readLine();
					//ma.status.setText("Connected");
					if (serverMessage != null && mMessageListener != null) {
						// call the method messageReceived from MyActivity class
						mMessageListener.messageReceived(serverMessage);
					}
					serverMessage = null;

				}

				Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
						+ serverMessage + "'");

			} catch (Exception e) {

				Log.e("TCP", "S: Error", e);
				//context.status.setText("Error! : " + e);
				connectionStatus = "Server Error : " + e.toString();
				

			} finally {
				// the socket must be closed. It is not possible to reconnect to
				// this socket
				// after it is closed, which means a new socket instance has to
				// be created.
				UpdateConnectionStatus(connectionStatus);
				socket.close();
			}

		} catch (Exception e) {

			Log.e("TCP", "C: Error", e);
			connectionStatus = "Connection Error : " + e.toString();
			//UpdateConnectionStatus(connectionStatus);

		}

	}

	// Declare the interface. The method messageReceived(String message) will
	// must be implemented in the MyActivity
	// class at on asynckTask doInBackground
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}
}