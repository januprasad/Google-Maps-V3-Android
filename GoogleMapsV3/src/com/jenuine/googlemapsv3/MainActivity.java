package com.jenuine.googlemapsv3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MainActivity mContext;
	private ProgressDialog progressDialog;
	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WebView webView = (WebView) findViewById(R.id.webView1);
		String old = readTxt(getApplicationContext());
		final GPSTracker tracker = new GPSTracker(this);
		if (tracker.canGetLocation()) {
			if (tracker.getLatitude() == 0.0 && tracker.getLongitude() == 0.0) {
//				Toast.makeText(getApplicationContext(),
//						"Location Not Available", Toast.LENGTH_SHORT).show();
				alertDialogforfinish("Location Not Available", this);

			} else {

				mContext = this;
				progressDialog = new ProgressDialog(mContext);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("Loading....");
				progressDialog.show();

				mThread = new Thread() {
					@Override
					public void run() {
						try {
							synchronized (this) {
								// Wait given period of time or exit on touch
								wait(5000);

							}
						} catch (InterruptedException ex) {
						}

						// finish();
						progressDialog.dismiss();

					}
				};
				mThread.start();

				// Toast.makeText(getApplicationContext(),
				// tracker.getLatitude() + " " + tracker.getLongitude(),
				// Toast.LENGTH_SHORT).show();

				String getChangeLocation = old.replace("lati",
						tracker.getLatitude() + ""); // startlat,startlong
				String getScond = getChangeLocation.replace("longi",
						tracker.getLongitude() + ""); // endlat,endlong

				webView.getSettings().setJavaScriptEnabled(true);
				// webView.loadUrl("file:///android_asset/index.html");
				webView.loadDataWithBaseURL(null, getScond, "text/html",
						"utf-8", null);
			}
		} else {
			tracker.showSettingsAlert();
		}

	}
	public static void alertDialogforfinish(String msg, final Activity activity) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		// Add the buttons
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				activity.finish();

			}
		});

		AlertDialog dialog = builder.create();
		dialog.setTitle(msg);
		dialog.show();
	}
	public String readTxt(Context con) {

		InputStream inputStream = null;
		;
		try {
			AssetManager am = con.getAssets();
			inputStream = am.open("index.html");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int i;
		try {
			i = inputStream.read();
			while (i != -1) {
				byteArrayOutputStream.write(i);
				i = inputStream.read();
			}
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteArrayOutputStream.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
