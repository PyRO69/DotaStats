package com.example.dotastats.adapters;

import com.example.dotastats.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * List view adapter to populate all the users Records.
 * 
 * @author swaroop
 */
public class ListViewAdapterForRecords extends ArrayAdapter<String> {

	private Context context;
	private int layoutID;
	private String[] recordData;

	public ListViewAdapterForRecords(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.layoutID = textViewResourceId;
		this.recordData = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View myView = convertView;
		TextView holder = null;

		if(myView == null) {
			LayoutInflater myInflater = ((Activity) this.context).getLayoutInflater();
			myView = myInflater.inflate(this.layoutID, parent, false);
			holder = (TextView) myView.findViewById(R.id.record);
			myView.setTag(holder);
		} else {
			holder = (TextView) myView.getTag();
		}

		holder.setText(this.recordData[position]);

		return myView;
	}

}
