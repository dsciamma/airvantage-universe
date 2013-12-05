package net.airvantage.parse;

import static net.airvantage.parse.Constants.Extra.THING_NAME;
import static net.airvantage.parse.Constants.Extra.THING_UID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airvantage.parse.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.parse.ParseCloud;
import com.parse.ParseException;

public class ThingActivity extends Activity {

	private TextView name;

	private String thing_uid;
	private String thing_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.thing_details);

		name = (TextView) findViewById(R.id.thing_name);
		
		if (getIntent() != null && getIntent().getExtras() != null) {
			thing_uid = getIntent().getStringExtra(THING_UID);
			thing_name = getIntent().getStringExtra(THING_NAME);

			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("uid", thing_uid);
				params.put("data", "PAPP");

				ArrayList<HashMap<String, Integer>> result = (ArrayList<HashMap<String, Integer>>) ParseCloud.callFunction("getMaxHourlyData", params);

				name.setText(thing_name);

				GraphViewData[] data = new GraphViewData[result.size()];
				for (int i = 0; i < result.size(); i++) {
					data[i] = new GraphViewData(i, result.get(i).get("value"));
				}
				GraphViewSeries exampleSeries = new GraphViewSeries(data);

				GraphView graphView = new LineGraphView(this // context
						, "PAPP" // heading
				);
				graphView.addSeries(exampleSeries); // data

				LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
				layout.addView(graphView);
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
}
