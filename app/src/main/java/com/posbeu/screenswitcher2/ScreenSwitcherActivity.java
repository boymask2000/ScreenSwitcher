package com.posbeu.screenswitcher2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class ScreenSwitcherActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	
		setContentView(R.layout.screenswitcher);
	
		handle_buttons();
	}

	private void handle_buttons() {
		View aboutButton = findViewById(R.id.start_button);
		aboutButton.setOnClickListener(this);

		View newButton = findViewById(R.id.stop_button);
		newButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_button:
			startService(new Intent(this, Switcher.class));

			break;
		case R.id.stop_button:
			stopService(new Intent(this, Switcher.class));
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, PreferencesActivity.class));
			return true;
		}
		return false;
	}
}
