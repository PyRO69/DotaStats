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
import com.example.dotastats.helperclasses.HeroResultObject;

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

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			myView = inflater.inflate(layoutID, parent, false);
			holder = new DataHolder();
			holder.img = (ImageView) myView.findViewById(R.id.herodisplaypic);
			holder.heroName = (TextView) myView.findViewById(R.id.herotitle);
			holder.heroWinRate = (TextView) myView.findViewById(R.id.herowinrate);
			holder.kda = (TextView) myView.findViewById(R.id.herokda);
			holder.numMatches = (TextView) myView.findViewById(R.id.numberofmatches);

			myView.setTag(holder);
		} else {

			holder = (DataHolder) myView.getTag();

		}

		HeroResultObject currentResult = myHeroResults[position];
		holder.heroName.setText("Hero: " + currentResult.getHeroName());
		//holder.img.setImageBitmap(currentResult.getHeroImage());
		holder.heroWinRate.setText("Win Rate: " + currentResult.getHeroWinRate());
		holder.kda.setText("KDA: " + currentResult.getKda());
		holder.numMatches.setText("Matches Played: " + currentResult.getNumMatches());

		return myView;
	}

	private class DataHolder {
		public ImageView img;
		public TextView heroName;
		public TextView numMatches;
		public TextView kda;
		public TextView heroWinRate;
	}
}
