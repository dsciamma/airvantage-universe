package net.airvantage.parse;

import android.app.Application;

import com.airvantage.parse.R;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class AirVantageApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, getString(R.string.parse_app_id),
				getString(R.string.parse_client_key));

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));

	}

}
