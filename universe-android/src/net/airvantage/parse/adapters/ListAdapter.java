package net.airvantage.parse.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airvantage.parse.R;
import com.parse.ParseObject;

public class ListAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
    private List<ParseObject> things;

	public ListAdapter(Activity a, List<ParseObject> data) {
		activity = a;
		things = data;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return things.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item, null);
        
        TextView text = (TextView)vi.findViewById(R.id.text); 
 
        ParseObject thing = things.get(position);
 
        text.setText(thing.getString("name"));
        
        return vi;
    }
}