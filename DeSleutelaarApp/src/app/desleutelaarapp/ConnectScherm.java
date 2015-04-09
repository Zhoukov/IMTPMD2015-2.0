package app.desleutelaarapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectScherm extends Activity implements OnClickListener {

	Button knop;
	EditText ipadres;
	public static String ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect_activity);

		knop = (Button) findViewById(R.id.knop);
		knop.setOnClickListener(this);

		ipadres = (EditText) findViewById(R.id.ipadres);
		ip = ipadres.getText().toString();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(ip);

		Intent i = new Intent(ConnectScherm.this, MainActivity.class);
		i.putExtra("ipadres", ip);
		startActivity(i);
	}

}
