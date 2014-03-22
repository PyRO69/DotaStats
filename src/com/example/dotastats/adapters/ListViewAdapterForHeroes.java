package com.example.dotastats.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dotastats.R;
import com.example.dotastats.helperclasses.HeroResultObject;

/*
 * Adapter to populate the List View for all the returned users for a query.
 * 
 * @author swaroop
 */
public class ListViewAdapterForHeroes extends ArrayAdapter<HeroResultObject> {

	private Context context;
	private int layoutID;
	private HeroResultObject[] myHeroResults;

	public ListViewAdapterForHeroes(Context context, int layoutID, HeroResultObject[] objects) {
		super(context, layoutID, objects);
		this.context = context;
		this.layoutID = layoutID;
		this.myHeroResults = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View myView = convertView;
		DataHolder holder = null;

		if(myView == null) {

			// Inflate the view and assign to the local variables.
			LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
			myView = inflater.inflate(this.layoutID, parent, false);
			holder = new DataHolder();
			holder.heroName = (TextView) myView.findViewById(R.id.herotitle);
			holder.heroWinRate = (TextView) myView.findViewById(R.id.herowinrate);
			holder.kda = (TextView) myView.findViewById(R.id.herokda);
			holder.numMatches = (TextView) myView.findViewById(R.id.numberofmatches);

			myView.setTag(holder);
		} else {

			holder = (DataHolder) myView.getTag();

		}

		// Set the information.
		HeroResultObject currentResult = this.myHeroResults[position];
		holder.heroName.setText("Hero: " + currentResult.getHeroName());
		holder.heroWinRate.setText("Win Rate: " + currentResult.getHeroWinRate());
		holder.kda.setText("KDA: " + currentResult.getKda());
		holder.numMatches.setText("Matches Played: " + currentResult.getNumMatches());

		return myView;
	}

	/*
	 * Helper class to hold the view. Can be made immutable ?
	 */
	private class DataHolder {
		public TextView heroName;
		public TextView numMatches;
		public TextView kda;
		public TextView heroWinRate;
	}
}
