package app.desleutelaarapp;

import java.util.concurrent.ExecutionException; 

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.desleutelaarapp.Preferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AanvraagScherm extends Activity implements OnClickListener {

	TextView gekozen;
	String gekozenservice;
	EditText naamVeld;
	EditText emailVeld;
	EditText telefoonVeld;
	EditText adresVeld;
    private static String naam;
    private static String adres;
    private static String telefoon;
    private static String email;
	Button bevestigen;
	Button annuleren;
	String ip = ConnectScherm.ip; /*MainActivity.ip;*/
	int port = MainActivity.port;
	String responseFix;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aanvraag_activity);

		bevestigen = (Button) findViewById(R.id.bevestigen);
		bevestigen.setOnClickListener(this);

		annuleren = (Button) findViewById(R.id.annuleren);
		annuleren.setOnClickListener(this);

		Intent aanvraagscherm = getIntent();
		gekozenservice = aanvraagscherm.getStringExtra("gekozen");
		System.out.println(gekozen);

		TextView gekozen = (TextView) findViewById(R.id.textView1);
		this.gekozen = gekozen;
		gekozen.setText("U heeft gekozen voor de service "
				+ gekozenservice
				+ ", gelieve hier uw naam, adres, telefoonnummer en email in te vullen");

		naamVeld = (EditText) findViewById(R.id.naam);
		// naam.setHint("Naam");

		adresVeld = (EditText) findViewById(R.id.adres);
		// adres.setHint("Adres");

		telefoonVeld = (EditText) findViewById(R.id.telefoon);
		// telefoon.setHint("0611111111");

		emailVeld = (EditText) findViewById(R.id.email);
		// telefoon.setHint("E-mail");

		String[] prefs = Preferences.getInstance(this)
				.getCustomerInfoPreferences();
		if (prefs[0] != null)
			this.naamVeld.setText(prefs[0]);
		if (prefs[1] != null)
			this.adresVeld.setText(prefs[1]);
		if (prefs[2] != null)
			this.telefoonVeld.setText(prefs[2]);
		if (prefs[3] != null)
			this.emailVeld.setText(prefs[3]);
	}

	@Override
	public void onClick(View v) {

		String[] newprefs = { this.naamVeld.getText().toString(),
				this.adresVeld.getText().toString(),
				this.telefoonVeld.getText().toString(),
				this.emailVeld.getText().toString() };
		Preferences.getInstance(this).updateCustomerInfoPreferences(newprefs);

		switch (v.getId()) {
		case R.id.bevestigen:
			
			plaatsBestelling();

			break;
		case R.id.annuleren:
			Intent j = new Intent(this, MainActivity.class);
			j.putExtra("ipadres", ip);
			startActivity(j);

			finish();
			break;
		}
	}

	 private void plaatsBestelling() {
	        final TextView koperNaam = (TextView) findViewById(R.id.naam);
	        final TextView koperAdres = (TextView) findViewById(R.id.adres);
	        final TextView koperTelefoon = (TextView) findViewById(R.id.telefoon);
	        final TextView koperEmail = (TextView) findViewById(R.id.email);

	        naam = naamVeld.getText().toString();
	        adres = adresVeld.getText().toString();
	        telefoon = telefoonVeld.getText().toString();
	        email = emailVeld.getText().toString();

	        JSONObject bestelling = new JSONObject();
	        JSONObject service = new JSONObject();
	        JSONObject gegevens = new JSONObject();
	        JSONArray bestelArray = new JSONArray();

	        try {
	            service.put("slotnaam", gekozenservice.toString());
	            gegevens.put("kopernaam", naam);
	            gegevens.put("koperadres", adres);
	            gegevens.put("kopertelnr", telefoon);
	            gegevens.put("koperemail", email);

	            bestelArray.put(service);
	            bestelArray.put(gegevens);

	            bestelling.put("aanvraag", bestelArray);

	        } catch (JSONException e) {

	        }
	        String response = null;

	        try {
	            try {
	                // Dit IP adres moet IP adres van server zijn.
	                response = new ServerCommunicator(ip,
	                        port, bestelling.toString()).execute().get();
	            } catch (ExecutionException e) {
	                e.printStackTrace();
	            }
	        } catch (InterruptedException e1) {
	            e1.printStackTrace();
	        }
	        if(response == null)
	        {
	            Toast.makeText(this, "Server is momenteel niet bereikbaar", Toast.LENGTH_LONG).show();
	        }
	        else{
	            responseFix = response.replace("null", "");

	            Toast.makeText(this, responseFix, Toast.LENGTH_LONG).show();
	            bevestigen.setVisibility(View.GONE);

	        }
	        naam = koperNaam.getText().toString();
	        adres = koperAdres.getText().toString();
	        telefoon = koperTelefoon.getText().toString();
	        email = koperEmail.getText().toString();

	        annuleren.setText("Terugkeren naar Hoofdscherm");

	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	public void onBackPressed() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();

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
