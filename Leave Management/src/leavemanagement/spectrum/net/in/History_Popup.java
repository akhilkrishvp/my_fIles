package leavemanagement.spectrum.net.in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class History_Popup extends Activity {
	Button bEdit;
	String dateApplied, noOfDays, fromDate, toDate, reason, status, requestId,
			type;
	TextView tvDateApplied, tvNoOfDays, tvFromDate, tvToDate, tvReason,
			tvStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_list_popup);
		tvDateApplied = (TextView) findViewById(R.id.tvDateApplied);
		tvNoOfDays = (TextView) findViewById(R.id.tvNoOfDays);
		tvFromDate = (TextView) findViewById(R.id.tvFromDate);
		tvToDate = (TextView) findViewById(R.id.tvToDate);
		tvReason = (TextView) findViewById(R.id.tvReason);
		tvStatus = (TextView) findViewById(R.id.tvStatus);

		Bundle b1 = getIntent().getExtras();
		dateApplied = b1.getString("date_applied");
		noOfDays = b1.getString("no_of_leaves");
		fromDate = b1.getString("from_date");
		toDate = b1.getString("to_date");
		reason = b1.getString("reason");
		status = b1.getString("leave_status");
		requestId = b1.getString("leave_id");
		type = b1.getString("type");

		tvDateApplied.setText(dateApplied);
		tvNoOfDays.setText(noOfDays);
		tvFromDate.setText(fromDate);
		tvToDate.setText(toDate);
		tvReason.setText(reason);
		tvStatus.setText(status);

	}

	public void edit(View v) {
		Intent i = new Intent(History_Popup.this, EditForLeave.class);
		i.putExtra("date_Applied", dateApplied);
		i.putExtra("no_of_leaves", noOfDays);
		i.putExtra("from_date", fromDate);
		i.putExtra("to_date", toDate);
		i.putExtra("reason", reason);
		i.putExtra("leave_id", requestId);
		i.putExtra("type", type);
		startActivity(i);
	}
}
