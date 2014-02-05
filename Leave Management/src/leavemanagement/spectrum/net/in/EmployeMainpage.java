package leavemanagement.spectrum.net.in;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EmployeMainpage extends Activity {
	Button Apply;
	Button History;
	Button Holiday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.employemainpage);
		Apply = (Button) findViewById(R.id.apply);
		History = (Button) findViewById(R.id.leave);
		Holiday = (Button) findViewById(R.id.holidays);

		Apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EmployeMainpage.this, ApplyForLeave.class);
				startActivity(i);

			}
		});
		History.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EmployeMainpage.this, History.class);
				startActivity(i);

			}
		});
		Holiday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EmployeMainpage.this,
						CalendarActivity.class);
				startActivity(i);

			}
		});

	}

	// ............................Menu options..................//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_logout:
			if (new SharedPrefs(getApplicationContext()).resetPreferences()) {
				Intent intent = new Intent(getApplicationContext(), Login.class);
				startActivity(intent);
				finish();
				return true;
			}
			return false;

		case R.id.menu_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
