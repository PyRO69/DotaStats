package com.example.dotastats.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dotastats.R;
import com.example.dotastats.helperclasses.MatchResultObject;

public class ListViewAdapterForMatches extends ArrayAdapter<MatchResultObject> {

	private Context context;
	private int layoutID;
	private MatchResultObject[] myMatchResults;

	public ListViewAdapterForMatches(Context context, int textViewResourceId,
			MatchResultObject[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.layoutID = textViewResourceId;
		this.myMatchResults = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View myView = convertView;
		ResultsHolder holder = null;

		if(myView == null) {

			LayoutInflater myInflater =  ((Activity) context).getLayoutInflater();
			myView = myInflater.inflate(layoutID, parent, false);
			holder = new ResultsHolder();
			holder.heroName = (TextView) myView.findViewById(R.id.heroname);
			holder.img = (ImageView) myView.findViewById(R.id.heroimage);
			holder.matchID = (TextView) myView.findViewById(R.id.matchid);

			myView.setTag(holder);

		} else {

			holder = (ResultsHolder) myView.getTag();

		}

		MatchResultObject thisResult = myMatchResults[position];

		holder.heroName.setText("Played as: " + thisResult.getHeroName());
		holder.img.setImageBitmap(thisResult.getHeroImage());
		holder.matchID.setText("Match ID: " + thisResult.getMatchID());

		return myView;
	}


	private class ResultsHolder {
		public ImageView img;
		public TextView heroName;
		public TextView matchID;
	}
}
