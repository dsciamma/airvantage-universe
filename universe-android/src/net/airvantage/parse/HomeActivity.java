package net.airvantage.parse;

import static net.airvantage.parse.Constants.Extra.THING_NAME;
import static net.airvantage.parse.Constants.Extra.THING_UID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.airvantage.parse.adapters.ListAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airvantage.parse.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HomeActivity extends Activity {

	private ListView list;
	private ListAdapter adapter;
	private Button addButton;
	private Button scanButton;
	private TextView serialTxt;
	private TextView nameTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home);

		addButton = (Button) findViewById(R.id.addThingBt);
		scanButton = (Button) findViewById(R.id.scanBt);
		serialTxt = (TextView) findViewById(R.id.serialTxt);
		nameTxt = (TextView) findViewById(R.id.nameTxt);
		
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onAddButtonClicked();
			}
		});

		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onScanButtonClicked();
			}
		});

		refreshList();
	}

	private void refreshList() {
		try {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Thing");
			query.whereEqualTo("owner", ParseUser.getCurrentUser());
			final List<ParseObject> things = query.find();

			list = (ListView) findViewById(R.id.list);

			// Getting adapter by passing xml data ArrayList
			adapter = new ListAdapter(this, things);
			list.setAdapter(adapter);

			// Click event for single list row
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ParseObject thing = things.get(position);
					showThingActivity(thing);
				}
			});
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		// check we have a valid result
		if (scanningResult != null) {
			// get content from Intent Result
			String scanContent = scanningResult.getContents();
			// get format name of data scanned
			//String scanFormat = scanningResult.getFormatName();
			serialTxt.setText(scanContent);
			nameTxt.requestFocus();
		} else {
			// invalid scan data or scan canceled
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	protected void onScanButtonClicked() {
		// instantiate ZXing integration class
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		// start scanning
		scanIntegrator.initiateScan();
	}

	protected void onAddButtonClicked() {


		if (serialTxt.getText().length() == 0) {
		} else {
			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("serial", serialTxt.getText().toString());
				if (nameTxt.getText().length() > 0) {
					params.put("name", nameTxt.getText().toString());
				}
				
				Object result = ParseCloud.callFunction("addThing", params);

				Log.d("net.airvantage", result.toString());

				refreshList();

				Context context = getApplicationContext();
				CharSequence text = "Thing added";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void showThingActivity(ParseObject thing) {
		Intent intent = new Intent(this, ThingActivity.class);
		intent.putExtra(THING_UID, thing.getString("uid"));
		intent.putExtra(THING_NAME, thing.getString("name"));
		startActivity(intent);
	}
}
