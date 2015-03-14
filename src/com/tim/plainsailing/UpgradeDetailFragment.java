package com.tim.plainsailing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UpgradeDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";

	int[] 	currentUpgrades;
	int[] 	playerInfo;
	
	boolean twoPane;
	
	private UpgradeContent.UpgradeItem mItem;

	public UpgradeDetailFragment() {	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentUpgrades = new int[Util.upgradesSize];
		currentUpgrades = Util.loadUpgradesFile(getActivity(), Util.upgradesSize);
		
		playerInfo 		= new int[2];
		playerInfo 		= Util.loadPlayerInfo(getActivity());
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mItem = UpgradeContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
		twoPane = getArguments().containsKey("twoPane");
	}

	public void upgrade(View view){
		playerInfo 		= Util.loadPlayerInfo(getActivity());
		
		if(currentUpgrades[Integer.parseInt(mItem.id)] < mItem.rank){
			if(playerInfo[0] >= mItem.cost){
				
				playerInfo[0] -= mItem.cost; 
				Util.savePlayerInfo(getActivity(), playerInfo); 
				
				currentUpgrades[Integer.parseInt(mItem.id)]++; 
				Util.saveUpgrades(getActivity(), Util.upgradesSize, currentUpgrades); 
			
				((TextView) view.findViewById(R.id.current_upgrade))
					.setText(Integer.toString(currentUpgrades[Integer.parseInt(mItem.id)]));	
				
				((TextView) view.findViewById(R.id.error_message)).setText("");
			} else {
				((TextView) view.findViewById(R.id.error_message))
					.setText("Not enough money!");
			}
		} else {
			((TextView) view.findViewById(R.id.error_message))
			.setText("Upgrade already at max rank!");
		}
		
		((TextView) view.findViewById(R.id.currency)).setText("Coins: " + Integer.toString(playerInfo[0]));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_upgrade_detail, container, false);
		
		if (mItem != null) {
			if(twoPane)
				((UpgradeDetailActivity) getActivity()).setBarTitle(mItem.name);
			
			((TextView) rootView.findViewById(R.id.upgrade_info)).setText(mItem.info);	
			((TextView) rootView.findViewById(R.id.current_upgrade)).setText(Integer.toString(currentUpgrades[Integer.parseInt(mItem.id)]));		
			((TextView) rootView.findViewById(R.id.max_upgrade)).setText("/ " + mItem.rank);	
			((TextView) rootView.findViewById(R.id.upgrade_cost)).setText(mItem.cost + " coins.");	
			((TextView) rootView.findViewById(R.id.currency)).setText("Coins: " + Integer.toString(playerInfo[0]));	
		}

		return rootView;
	}
}
