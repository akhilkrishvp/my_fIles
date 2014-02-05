package leavemanagement.spectrum.net.in;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditForLeave extends Activity {
	DatePicker d1, d2;
	ProgressDialog pDialog, pDialog1;
	String newsresponse, leave_response;
	SharedPrefs pref;
	TextView Apply_Date_text, From_Date_text, To_Date_text, EID_text,
			Spinner_text;
	int from_year, from_month, from_day, to_year, to_month, to_day, year,
			month, day, id;

	String Get_date_applied, Get_no_of_leaves, Get_from_date, Get_to_date,
			Get_reason, Get_leave_id, getLeaveType;

	EditText No_Of_Date_txt, Reason_text;
	static final int Date_id = 1;
	static final int Date_id1 = 2;
	static final int Date_id2 = 3;

	int nodays = 0;
	int totalday = 0;
	int totalyear = 0;
	int totalmon = 0;
	int From_year;
	int To_year, From_Month, From_Day, To_Month, To_Day;

	int position;

	String FromDate, ToDate;
	String[] New_Leave_Type;

	Button edit;
	Spinner my_spin;
	ArrayAdapter aa;
	String Employee_ID, Apply_Date, No_Of_Dates, Type, From_Date, To_Date,
			Reason, New_Result, New_reason, ID;
	String[] fromDateArray, toDateArray;

	SimpleDateFormat tf;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_for_leave);
		edit = (Button) findViewById(R.id.buttonEdit);
		pref = new SharedPrefs(getApplicationContext());

		tf = new SimpleDateFormat("yyyy-MM-dd");

		// Parsing for Leave Type.........................................//
		EID_text = (TextView) findViewById(R.id.Edit_textViewapply3);
		No_Of_Date_txt = (EditText) findViewById(R.id.Edit_No_of_Date);
		Employee_ID = pref.getUsername();
		EID_text.setText(Employee_ID);
		Reason_text = (EditText) findViewById(R.id.Edit_reason);
		Spinner_text = (TextView) findViewById(R.id.Edit_textViewapply60);
		my_spin = (Spinner) findViewById(R.id.Edit_Spinner);
		Apply_Date_text = (TextView) findViewById(R.id.Edit_textViewapply5);
		From_Date_text = (TextView) findViewById(R.id.Edit_textViewapply8);
		To_Date_text = (TextView) findViewById(R.id.Edit_textViewapply10);

		Intent i = getIntent();

		Get_date_applied = i.getStringExtra("date_Applied");
		Get_no_of_leaves = i.getStringExtra("no_of_leaves");
		Get_from_date = i.getStringExtra("from_date");
		System.out.println("froooooooooooooooom");
		Get_to_date = i.getStringExtra("to_date");
		Get_reason = i.getStringExtra("reason");
		Get_leave_id = i.getStringExtra("leave_id");
		getLeaveType = i.getStringExtra("type");

		try {
			String str = tf.parse(Get_from_date).toString();

			System.out.println("fffffffffffffffffff------>" + str);

			tf.parse(Get_to_date);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Apply_Date_text.setText(Get_date_applied);
		
		No_Of_Date_txt.setText(Get_no_of_leaves);

		Spinner_text.setText(getLeaveType);
		ID = Get_leave_id;

		fromDateArray = new String[3];
		toDateArray = new String[3];

		fromDateArray = Get_from_date.split("-");
		toDateArray = Get_to_date.split("-");
		Get_from_date = fromDateArray[2] + "-" + fromDateArray[1] + "-"
				+ fromDateArray[0];
		Get_to_date = toDateArray[2] + "-" + toDateArray[1] + "-"
				+ toDateArray[0];
		From_Date_text.setText(Get_from_date);
		To_Date_text.setText(Get_to_date);
		Reason_text.setText(Get_reason);

		setCurrentDateOnView();
		fromtext();
		totext();

		// ......................LeaveType Parsing....................//

		try {

			pDialog = new ProgressDialog(EditForLeave.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			Leave_Type_Thread thread2 = new Leave_Type_Thread();
			thread2.start();

		} catch (Exception e) {

		}

		my_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				Spinner_text.setText(New_Leave_Type[pos]);
				my_spin.setVisibility(View.GONE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private class Leave_Type_Thread extends Thread {
		public static final int success = 0;
		public static final int no_data = 1;
		public static final int no_internet = -1;

		public Leave_Type_Thread() {
		}

		public void run() {
			try {
				if (isNetworkAvailable()) {
					leave_response = Leave_Type_Webservices
							.Leave_type_Details(Employee_ID);
					System.out.println("leave type response is ==========="
							+ leave_response);
					if (leave_response.length() > 0 && leave_response != null) {
						System.out.println("leave response is ksdbaskdj");
						newshandler1.sendEmptyMessage(success);
						int x = Leave_Type_Json_Parse
								.Parse_LeaveType(leave_response);
						System.out.println("leave len====" + x);
					} else {
						newshandler1.sendEmptyMessage(no_data);
					}
				} else
					newshandler1.sendEmptyMessage(no_internet);

			} catch (Exception e) {
			}
		}

	}

	private Handler newshandler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Leave_Type_Thread.success:
				pDialog.dismiss();

				// Spinner_text.setText(New_Leave_Type[pos]);

				try {

					System.out.println("New Leave types are ...............");

					New_Leave_Type = Leave_Type_Json_Parse.getLeave_type();
					trial();

					System.out.println("New Leave types are "
							+ New_Leave_Type[0]);

				} catch (Exception e) {

					e.printStackTrace();
				}

			default:
				pDialog.dismiss();
				break;
			}
			super.handleMessage(msg);
		}

	};

	public void trial() {
		aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
				New_Leave_Type);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		my_spin.setAdapter(aa);
	}

	public void setCurrentDateOnView() {

		d1 = (DatePicker) findViewById(R.id.Edit_datePicker2);
		d2 = (DatePicker) findViewById(R.id.Edit_datePicker3);

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		Apply_Date_text.setText(new StringBuilder().append(year).append("-")
				.append(month + 1).append("-").append(day).append(" "));

		d1.init(year, month, day, null);
		d2.init(year, month, day, null);

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Date_id1:
			return new DatePickerDialog(this, from_datePickerListener, year,
					month, day);

		case Date_id2:
			return new DatePickerDialog(this, to_datePickerListener, year,
					month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener from_datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			from_year = selectedYear;
			from_month = selectedMonth;
			from_month++;
			from_day = selectedDay;

			From_Date_text.setText(new StringBuilder().append(from_year)
					.append("-").append(from_month).append("-")
					.append(from_day).append(" "));
			d1.init(from_year, from_month, from_day, null);
			FromDate = From_Date_text.getText().toString();
			if (!FromDate.equals(null)) {
				To_Date_text.setVisibility(View.VISIBLE);

			}

		}
	};
	private DatePickerDialog.OnDateSetListener to_datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			try {
				System.out.println("From date is......" + FromDate);

				to_year = selectedYear;
				to_month = selectedMonth;
				to_month++;
				to_day = selectedDay;

				To_Date_text.setText(new StringBuilder().append(to_year)
						.append("-").append(to_month).append("-")
						.append(to_day).append(" "));

				d2.init(to_year, to_month, to_day, null);

				// .......................Date Checking For Validation of No of
				// Dates.................//

				ToDate = To_Date_text.getText().toString();
				String[] From_Date_split;
				String[] To_Date_split;
				From_Date_split = new String[3];
				From_Date_split = FromDate.split("-");
				To_Date_split = new String[3];
				To_Date_split = ToDate.split("-");

				System.out.println("fromxx FromDate===" + FromDate);
				System.out.println("fromxx ToDate===" + ToDate);
				// System.out.println("fromxx year33==="+From_Date_split[2]);
				// System.out.println("fromxx to year111==="+To_Date_split[0]);
				// System.out.println("fromxx to year22==="+To_Date_split[1]);
				// System.out.println("fromxx to year22==="+To_Date_split[2]);

				int fromdate1, frommonth1, fromyear1, todate1, toyear1, tomonth1;
				String date1, month1, year1, date2, month2, year2;
				year1 = From_Date_split[0].trim();
				month1 = From_Date_split[1].trim();
				date1 = From_Date_split[2].trim();

				fromdate1 = Integer.valueOf(date1);
				frommonth1 = Integer.valueOf(month1);
				fromyear1 = Integer.valueOf(year1);

				year2 = To_Date_split[0].trim();
				month2 = To_Date_split[1].trim();
				date2 = To_Date_split[2].trim();

				todate1 = Integer.valueOf(date2);
				tomonth1 = Integer.valueOf(month2);
				toyear1 = Integer.valueOf(year2);

				System.out.println("From_Day111====" + fromdate1);
				System.out.println("From_Day111====" + frommonth1);
				System.out.println("From_Day111====" + fromyear1);

				System.out.println("From_Day111====" + todate1);
				System.out.println("From_Day111====" + tomonth1);
				System.out.println("From_Day111====" + toyear1);

				if (todate1 >= fromdate1) {
					totalday = todate1 - fromdate1;
					totalyear = toyear1 - fromyear1;
					totalmon = tomonth1 - frommonth1;
				} else {
					totalday = (31 - fromdate1) + todate1;
					totalmon = tomonth1 - frommonth1;
					totalmon--;
					totalyear = toyear1 - fromyear1;
				}

				// ............Getting No Of dates.........................//

				nodays = (totalday) + (totalmon * 30) + (totalyear * 365);
				System.out.println("nodays getting===" + nodays);
				String NoOfDates = new Integer(nodays).toString();
				No_Of_Date_txt.setText(NoOfDates);

				if (nodays < 0) {
					Toast.makeText(getApplicationContext(), "Invalid Date",
							Toast.LENGTH_SHORT).show();
				} else {

					String Days = new Integer(nodays).toString();
					Toast.makeText(getApplicationContext(),
							"the remaining Days is" + Days, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	private void fromtext() {
		From_Date_text = (TextView) findViewById(R.id.Edit_textViewapply8);
		From_Date_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_id1);

			}
		});
	}

	private void totext() {
		To_Date_text = (TextView) findViewById(R.id.Edit_textViewapply10);
		To_Date_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_id2);

			}
		});
	}

	// ............................Button Edit On Click.....................//

	public void Edit(View v) {
		try {

			System.out.println("button clicked");

			Employee_ID = EID_text.getText().toString();
			Apply_Date = Apply_Date_text.getText().toString();
			No_Of_Dates = No_Of_Date_txt.getText().toString();
			From_Date = From_Date_text.getText().toString();

			To_Date = To_Date_text.getText().toString();

			System.out.println("aaaaaaaaaaaaaaaaaa---->" + From_Date
					+ "     to " + To_Date);
			Type = my_spin.getSelectedItem().toString();
			Reason = Reason_text.getText().toString();

			// System.out.println("Values is............"+Employee_ID +
			// Apply_Date+No_Of_Dates+Reason+ID+Type+To_Date);
			pDialog1 = new ProgressDialog(EditForLeave.this);
			pDialog1.setMessage("Loading...");
			pDialog1.setCancelable(false);
			pDialog1.show();

			EditForLeaveThread thread2 = new EditForLeaveThread();
			thread2.start();
		} catch (Exception e) {

		}
	}

	// ...............Thread for EditLeave Parse......................//

	private class EditForLeaveThread extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public EditForLeaveThread() {
		}

		@Override
		public void run() {
			try {

				if (isNetworkAvailable()) {

					newsresponse = Edit_Webservices.EditLeavDetails(
							Employee_ID, Type, Apply_Date, No_Of_Dates,
							From_Date, To_Date, Reason, ID);
					System.out.println(" new response is " + newsresponse);
					if (newsresponse.length() > 0 && newsresponse != null) {
						newshandler.sendEmptyMessage(SUCCESS);
					} else {
						newshandler.sendEmptyMessage(NO_DATA);
					}
				} else
					newshandler.sendEmptyMessage(NO_INTERNET);

			} catch (Exception e) {
			}
		}
	}

	// ..............................Handler for
	// Apply_Json_Parse...................//

	private Handler newshandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EditForLeaveThread.SUCCESS:
				pDialog1.dismiss();

				try {
					Edit_Json_Parse.Edit_Parse(newsresponse);
					New_Result = Edit_Json_Parse.Get_Edit_Result();
					System.out.println("New result is " + New_Result);
				} catch (Exception e) {

					e.printStackTrace();
				}

				if (New_Result.equals("Applied Successfully")) {

					Toast.makeText(getApplicationContext(), "Succeed",
							Toast.LENGTH_SHORT).show();
				}

			default:
				pDialog1.dismiss();
				break;
			}
			super.handleMessage(msg);
		}

	};

	public void Spinner(View v) {
		my_spin.performClick();
		my_spin.setVisibility(View.VISIBLE);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
