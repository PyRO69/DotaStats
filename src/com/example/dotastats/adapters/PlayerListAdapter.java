package com.example.dotastats.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dotastats.R;

public class PlayerListAdapter extends BaseAdapter {

	private Context context;
	private String[] playerNames;

	public PlayerListAdapter(Context context, String[] playerNames) {
		this.context =  context;
		this.playerNames = playerNames;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;

		if(convertView == null) {

			gridView = new View(context);
			gridView = inflater.inflate(R.layout.name_grids, null);

			TextView textView =  (TextView) gridView.findViewById(R.id.grid_item_label);
			textView.setText(playerNames[position]);

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}


	@Override
	public int getCount() {
		return playerNames.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
