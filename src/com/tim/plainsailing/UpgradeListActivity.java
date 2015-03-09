package com.tim.plainsailing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class UpgradeListActivity extends FragmentActivity implements
		UpgradeListFragment.Callbacks {
	UpgradeDetailFragment fragment;
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade_list);

		if (findViewById(R.id.upgrade_detail_container) != null) {
			mTwoPane = true;

			((UpgradeListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.upgrade_list))
					.setActivateOnItemClick(true);
		}
	}
	
	public void upgrade(View view){
		fragment.upgrade(view.getRootView());
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(UpgradeDetailFragment.ARG_ITEM_ID, id);
			fragment = new UpgradeDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.upgrade_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, UpgradeDetailActivity.class);
			detailIntent.putExtra(UpgradeDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
