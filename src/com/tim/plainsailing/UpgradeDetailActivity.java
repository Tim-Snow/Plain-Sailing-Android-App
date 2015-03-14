package com.tim.plainsailing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

public class UpgradeDetailActivity extends ActionBarActivity {
	UpgradeDetailFragment fragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade_detail);

		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(UpgradeDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(UpgradeDetailFragment.ARG_ITEM_ID));
			arguments.putBoolean("twoPane", getIntent().getBooleanExtra("twoPane", false));
			fragment = new UpgradeDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.upgrade_detail_container, fragment).commit();
		}		
	}
	
	public void upgrade(View view){
		fragment.upgrade(view.getRootView());
	}
	
	public void setBarTitle(String title){
		 ActionBar actionbar = this.getSupportActionBar();
		 actionbar.setTitle(title);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					UpgradeListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
