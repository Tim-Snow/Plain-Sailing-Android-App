package com.tim.plainsailing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UpgradeDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";

	int[] currentUpgrades;
	
	private UpgradeContent.UpgradeItem mItem;

	public UpgradeDetailFragment() {	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentUpgrades = new int[Util.upgradesSize];
		currentUpgrades = Util.loadUpgradesFile(getActivity(), Util.upgradesSize);
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mItem = UpgradeContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
			
		}
	}

	public void upgrade(View view){
		if(currentUpgrades[Integer.parseInt(mItem.id)] < mItem.rank){
			currentUpgrades[Integer.parseInt(mItem.id)]++;
			//subtract player currency
			Util.saveUpgrades(getActivity(), Util.upgradesSize, currentUpgrades);
		
			((TextView) view.findViewById(R.id.current_upgrade))
				.setText(Integer.toString(currentUpgrades[Integer.parseInt(mItem.id)]));	
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_upgrade_detail, container, false);
		
		if (mItem != null) {
			((UpgradeDetailActivity) getActivity()).setBarTitle(mItem.name);
			
			((TextView) rootView.findViewById(R.id.upgrade_info)).setText(mItem.info);	
			((TextView) rootView.findViewById(R.id.current_upgrade)).setText(Integer.toString(currentUpgrades[Integer.parseInt(mItem.id)]));		
			((TextView) rootView.findViewById(R.id.max_upgrade)).setText("/ " + mItem.rank);	
			((TextView) rootView.findViewById(R.id.upgrade_cost)).setText(mItem.cost + " coins.");	
		}

		return rootView;
	}
}
