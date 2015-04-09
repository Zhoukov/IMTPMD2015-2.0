package app.desleutelaarapp;

import java.util.concurrent.ExecutionException; 

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SlotenScherm extends Activity implements OnClickListener {

	Button bevestigen;
	Button annuleren;
	String gekozenslot;
	TextView titel;
	TextView info;
	private String detailInfo;
	public static String ip = MainActivity.ip;
	public static int port = MainActivity.port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sloten_activity);

		bevestigen = (Button) findViewById(R.id.bevestigen);
		bevestigen.setOnClickListener(this);

		annuleren = (Button) findViewById(R.id.annuleren);
		annuleren.setOnClickListener(this);

		Intent hoofdscherm = getIntent();
		gekozenslot = hoofdscherm.getStringExtra("naam");

		TextView titel = (TextView) findViewById(R.id.textView2);
		this.titel = titel;
		titel.setText(gekozenslot);

		// System.out.println(gekozenservice);

		JSONObject beknoptjObject = new JSONObject();
		try {
			beknoptjObject.put("informatie", gekozenslot);
			try {
				try {
					detailInfo = new ServerCommunicator(ip, port,
							beknoptjObject.toString()).execute().get();

				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			String infoFix = detailInfo.replace("null", "");
			JSONObject fixedjObject = new JSONObject(infoFix);
			detailInfo = fixedjObject.getString("informatie");

			Log.i("informatie", infoFix);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		TextView info = (TextView) findViewById(R.id.textView1);
		this.info = info;
		info.setText(detailInfo);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bevestigen:

			Intent i = new Intent(this, AanvraagScherm.class);
			i.putExtra("gekozen", (String) gekozenslot);
			startActivity(i);

			finish();
			break;
		case R.id.annuleren:
			Intent j = new Intent(this, MainActivity.class);
			startActivity(j);

			finish();
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			onBackPressed();
			System.out.println("Back pressed");
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			// System.out.println("UP Pressed");

			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);

			finish();

			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

}
