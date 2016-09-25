package brandon.trytry;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Bluetooth extends Activity {
	Button open;
	TextView label;
	ProgressBar pg;
	public EditText edit;
	Handler h;
	Handler han;
	String s;
	boolean go;
	boolean next;
	boolean est;
	final int MESSAGE_READ = 1;
	float[] tmp;
	private BluetoothServerSocket mmServerSocket;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;
	Toast toast;
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blue);
		open = (Button) findViewById(R.id.open);
		label = (TextView) findViewById(R.id.label);
		edit = (EditText) findViewById(R.id.editText1);
		pg = (ProgressBar) findViewById(R.id.progressBar1);
		s = "Connected";
		h = new Handler();
		han = new Handler();
		go = false;
		next = false;
		Context context = getApplicationContext();
		CharSequence text = "Hit";
		int duration = Toast.LENGTH_SHORT;

		toast = Toast.makeText(context, text, duration);

		
		open.setOnClickListener(new OnClickListener() {// /Initialize Bluetooth
														// Processes
			public void onClick(View v) {
				pg.setVisibility(1);
				new Thread(new Runnable() {
					public void run() {
						mBluetoothAdapter = BluetoothAdapter
								.getDefaultAdapter();
						if (mBluetoothAdapter == null) { // Determines if
															// Bluetooth is on
															// Device
							label.setText("BT Not Supported");
							pg.setVisibility(0);
						}
						if (!mBluetoothAdapter.isEnabled()) { // Enables
																// Bluetooth if
																// it was not
																// already
							Intent enableBtIntent = new Intent(
									BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivityForResult(enableBtIntent, 1);
						}

						Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
								.getBondedDevices();
						// If there are paired devices

						if (pairedDevices.size() > 0) {
							// Loop through paired devices

							for (BluetoothDevice device : pairedDevices) {
								if (device.getName().equals("KISS-001")) {// KISS-001
									go = true;
									mmDevice = device;
									break;
								}
							}
						}
						h.post(new Runnable() {
							public void run() {
								if (go) {
									(new Thread(new ConnectThread(mmDevice)))
											.start();
								}

							}
						});

					}// end of RUN
				}).start(); // starts thread and is "end" of thread block
			}
		}); // end of button click block

	}

	public void display(String st) {
		edit.setText(st);
	}

	public void cancel() {
		try {
			final Handler h = new Handler();
			if (est) {
				mmSocket.close();
				new Thread(new Runnable() {
					public void run() {
						h.post(new Runnable() {
							public void run() {
								toast.show();
							}
						});
					}
				}).start();
			}
		} catch (IOException e) {
		}
	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocketm;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;

			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);

			} catch (IOException e) {
			}
			mmSocketm = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				if (mmSocketm == null) {
					label.setText("Null Problem");
				} else {
					mmSocketm.connect();
					next = true;
				}

			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocketm.close();
				} catch (IOException closeException) {
				}
				return;
			}
			h.post(new Runnable() {
				public void run() {
					if (next) {
						label.setText(s);
						edit.setText("Connected");
						pg.setVisibility(View.INVISIBLE);
					}
				}
			});
			if (mmSocketm.isConnected()) {
				manageConnectedSocket(mmSocketm);
			}
			// Do work to manage the connection (in a separate thread)

		}

		public void manageConnectedSocket(BluetoothSocket mmSocket2) {
			ConnectedThread ct = new ConnectedThread(mmSocket2);
			ct.start();

		}

		private final Handler mHandler = new Handler() {
			int x = 1;
			String hold = "";
			ArrayList<Float> al = new ArrayList<Float>();
			ArrayList<String> sl = new ArrayList<String>();

			@Override
			public void handleMessage(Message msg) {
				int check = 0;
				boolean jo = false;

				switch (msg.what) {

				case MESSAGE_READ:
					// String readMessage = (String) msg.obj;
					Log.d("BUGGALOO", "MESSAGE_READ");
					String rm = (String) msg.obj;
					byte[] readBuf = rm.getBytes(Charset.forName("UTF-8"));
					// construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, msg.arg1);
					Log.e("BUGGALOO", "message received = " + readMessage);
					ArrayList<String> allAvail = new ArrayList<String>();

					switch (readMessage) {
					case "D":
						for (int h = 0; h < allAvail.size(); h++) {
							al.add(Float.parseFloat(allAvail.get(h)));
						}
						// sl.add(hold);
						float[] tmp = new float[al.size()];
						// String[] tmpr = new String[sl.size()];
						for (int h = 0; h < al.size(); h++) {
							tmp[h] = (float) al.get(h);
						}
						Bundle b = new Bundle();

						b.putFloatArray("pass", tmp);

						// ////Write to File
						FileOutputStream fos;
						String filename="DATES";
						String send="WORKS";
						try {
							 FileOutputStream fileout=openFileOutput(filename, MODE_APPEND);
							 OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
							 outputWriter.write(send);
							 outputWriter.close();
							 
							 //display file saved message
							 Toast.makeText(getBaseContext(), "well done",
							 Toast.LENGTH_SHORT).show();
							 
							 } catch (Exception e) {
							 e.printStackTrace();
							 } 
						//Intent intentFile = new Intent(Bluetooth.this, FileActivity.class);
						//startActivity(intentFile);

						// ////
						Intent intentGraph = new Intent(Bluetooth.this,
								GraphActivity.class);
						intentGraph.putExtras(b);
						cancel();
						startActivity(intentGraph);
						break;
					default:
						allAvail.add(readMessage);
						for (int y = 0; y < allAvail.size(); y++) {
							edit.append(allAvail.get(y));
						}
						break;
					}

					// al.add(Float.parseFloat(hold));

					// sl.add(hold);
					/*
					 * float[] tmp = new float[al.size()]; // String[] tmpr =
					 * new String[sl.size()]; for (int h = 0; h < al.size();
					 * h++) { tmp[h] = (float) al.get(h); } Bundle b = new
					 * Bundle();
					 * 
					 * b.putFloatArray("pass", tmp);
					 * 
					 * Intent intentGraph = new Intent(Bluetooth.this,
					 * GraphActivity.class); intentGraph.putExtras(b); cancel();
					 * startActivity(intentGraph);
					 */

					/*
					 * byte[] readBuf = (byte[]) msg.obj; // construct a string
					 * from the valid bytes in the buffer String readMessage =
					 * new String(readBuf, 0, msg.arg1);
					 * edit.setText(readMessage);
					 */
					break;
				}
			}

		};

		public boolean isNumeric(String str) {
			return str.matches("-?\\d+(\\.\\d+)?"); // match a number with
													// optional '-' and decimal.
		}

		private class ConnectedThread extends Thread {
			private final BluetoothSocket mmSocketmm;
			private final InputStream mmInStream;
			private final OutputStream mmOutStream;

			boolean stopWorker = false;
			int readBufferPosition = 0;

			// ArrayList al;

			public ConnectedThread(BluetoothSocket socket) {

				mmSocketmm = socket;
				InputStream tmpIn = null;
				OutputStream tmpOut = null;
				// al=new ArrayList();
				// Get the input and output streams, using temp objects because
				// member streams are final
				try {

					tmpIn = mmSocketmm.getInputStream();
					tmpOut = mmSocketmm.getOutputStream();// ////Previous crash
															// // block

				} catch (IOException e) {

				}

				mmInStream = tmpIn;
				mmOutStream = tmpOut;

			}

			public void run() {

				boolean t = true;
				while (!stopWorker) {
					byte[] buffer = new byte[1024];
					int bytes = 0;
					try {
						// Read from the InputStream
						bytes = mmInStream.read(buffer);
						// String readMessage = new String(buffer, 0, bytes);
						// Send the obtained bytes to the UI Activity
						mHandler.obtainMessage(MESSAGE_READ, bytes, -1,
								new String(buffer, 0, bytes)).sendToTarget();

						write("#Connected".getBytes());

						// ConnectedThread.sleep(20);

					} catch (IOException e) {
						stopWorker = true;
					}

				}

			}

			public void write(byte[] bytes) {
				try {
					mmOutStream.write(bytes);
				} catch (IOException e) {
				}
			}

			/* Call this from the main activity to shutdown the connection */
			public void cancel() {
				try {
					if (mmSocket.isConnected())
						mmSocket.close();
					if (mmSocketm.isConnected())
						mmSocketm.close();
					if (mmSocketmm.isConnected())
						mmSocketmm.close();
					pg.setVisibility(View.INVISIBLE);
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		Intent intentBack = new Intent(Bluetooth.this, MiddleMan.class);
		startActivity(intentBack);
		return;
	}
}
