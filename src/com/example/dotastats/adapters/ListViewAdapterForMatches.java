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

/*
 * List view adapter to populate all the matches that the user has played
 * with the Match IDs.
 * 
 * @author swaroop
 */
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

			// Inflate view and assign to the holder object views for recycle.
			LayoutInflater myInflater =  ((Activity) this.context).getLayoutInflater();
			myView = myInflater.inflate(this.layoutID, parent, false);
			holder = new ResultsHolder();
			holder.heroName = (TextView) myView.findViewById(R.id.heroname);
			holder.img = (ImageView) myView.findViewById(R.id.heroimage);
			holder.matchID = (TextView) myView.findViewById(R.id.matchid);

			myView.setTag(holder);

		} else {
			holder = (ResultsHolder) myView.getTag();
		}

		MatchResultObject thisResult = this.myMatchResults[position];

		holder.heroName.setText("Played as: " + thisResult.getHeroName());
		holder.img.setImageBitmap(thisResult.getHeroImage());
		holder.matchID.setText("Match ID: " + thisResult.getMatchID());

		return myView;
	}


	/*
	 * Holder class to simplify the view handling.
	 */
	private class ResultsHolder {
		public ImageView img;
		public TextView heroName;
		public TextView matchID;
	}
}
