package leavemanagement.spectrum.net.in;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ApplyForLeave extends Activity implements OnItemSelectedListener {
	DatePicker d, d1, d2;
	TextView Apply_Date_text, From_Date_text, To_Date_text, EID_text,
			Spinner_text;
	EditText No_Of_Date_txt, Reason_text;
	int from_year, from_month, from_day, to_year, to_month, to_day, year,
			month, day;

	ProgressDialog pDialog, pDialog1;
	String newsresponse, leave_response;
	String[] New_Leave_Type;
	SharedPrefs prefs;

	static final int Date_id = 1;
	static final int Date_id1 = 2;
	static final int Date_id2 = 3;
	// ...........declerations for date checking.............//
	int nodays = 0;
	int totalday = 0;
	int totalyear = 0;
	int totalmon = 0;
	int From_year;
	int To_year, From_Month, From_Day, To_Month, To_Day;
	String FromDate, ToDate;

	// ............Spinner decleration....................//
	Spinner my_spin;
	ArrayAdapter aa;
	String Employee_ID, Apply_Date, No_Of_Dates, Type, From_Date, To_Date,
			Reason, New_Result, New_reason, ID;
	String[] IdArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applyforleave);
		// Parsing for Leave Type.........................................//
		prefs = new SharedPrefs(getApplicationContext());
		EID_text = (TextView) findViewById(R.id.textViewapply3);
		No_Of_Date_txt = (EditText) findViewById(R.id.No_of_Date);
		EID_text.setText(prefs.getUsername());
		Employee_ID = prefs.getUsername();
		try {
			pDialog1 = new ProgressDialog(ApplyForLeave.this);
			pDialog1.setMessage("Preparing...");
			pDialog1.setCancelable(false);
			pDialog1.show();

			Leave_Type_Thread thread2 = new Leave_Type_Thread();
			thread2.start();

		} catch (Exception e) {

		}

		No_Of_Date_txt = (EditText) findViewById(R.id.No_of_Date);
		Reason_text = (EditText) findViewById(R.id.reason);
		Spinner_text = (TextView) findViewById(R.id.textViewforsinner);
		my_spin = (Spinner) findViewById(R.id.Spinner);
		my_spin.setOnItemSelectedListener(this);

		setCurrentDateOnView();
		datetext();
		fromtext();
		totext();
	}

	// //............Thread for Leave Type Parse....................//

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
					System.out.println("leave response is " + leave_response);
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

	// //............Handler for Leave Type Parse....................//
	private Handler newshandler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				pDialog1.dismiss();

				try {

					System.out.println("New Leave types are ...............");

					New_Leave_Type = Leave_Type_Json_Parse.getLeave_type();
					trial();

					System.out.println("New Leave types are " + New_Leave_Type);

				} catch (Exception e) {

					e.printStackTrace();
				}
				break;
			case Leave_Type_Thread.no_internet:
				pDialog1.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ApplyForLeave.this);
				builder.setMessage("No Data Connection")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

									}
								});
				AlertDialog alert = builder.create();

				alert.show();
			default:
				pDialog1.dismiss();
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

	public String[] GetLeaveType() {
		return New_Leave_Type;

	}

	// ............Date Picker Starting.........................//

	public void setCurrentDateOnView() {
		Apply_Date_text = (TextView) findViewById(R.id.textViewapply5);
		From_Date_text = (TextView) findViewById(R.id.textViewapply8);
		To_Date_text = (TextView) findViewById(R.id.textViewapply10);

		d = (DatePicker) findViewById(R.id.datePicker1);
		d1 = (DatePicker) findViewById(R.id.datePicker2);
		d2 = (DatePicker) findViewById(R.id.datePicker3);

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		Apply_Date_text.setText(new StringBuilder().append(year).append("-")
				.append(month + 1).append("-").append(day).append(" "));

		d.init(year, month, day, null);
		d1.init(year, month, day, null);
		d2.init(year, month, day, null);
	}

	private void datetext() {
		Apply_Date_text = (TextView) findViewById(R.id.textViewapply5);
		Apply_Date_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_id);

			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Date_id:
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		case Date_id1:
			return new DatePickerDialog(this, from_datePickerListener, year,
					month, day);
		case Date_id2:
			return new DatePickerDialog(this, to_datePickerListener, year,
					month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.

		// .................For Apply Date.................................//
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textViews

			Apply_Date_text.setText(new StringBuilder().append(year)
					.append("-").append(month + 1).append("-").append(day)
					.append(" "));

			// set selected date into datePicker also

			d.init(year, month, day, null);

		}
	};

	// ..............................For From
	// Date.................................//
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

	// ..........................For To
	// Date.....................................//
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

				if (todate1 >= fromdate1) {
					totalday = (todate1 - fromdate1) + 1;
					totalyear = toyear1 - fromyear1;
					totalmon = tomonth1 - frommonth1;
				} else {
					totalday = ((31 - fromdate1) + todate1) + 1;
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

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	private void fromtext() {
		From_Date_text = (TextView) findViewById(R.id.textViewapply8);
		From_Date_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_id1);

			}
		});
	}

	private void totext() {
		To_Date_text = (TextView) findViewById(R.id.textViewapply10);
		To_Date_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_id2);

			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	// ................................Apply Button On
	// CLick.......................//

	public void Apply(View v) {

		try {

			Employee_ID = EID_text.getText().toString();
			Apply_Date = Apply_Date_text.getText().toString();
			No_Of_Dates = No_Of_Date_txt.getText().toString();
			From_Date = From_Date_text.getText().toString();
			To_Date = To_Date_text.getText().toString();
			Type = my_spin.getSelectedItem().toString();
			Reason = Reason_text.getText().toString();
			pDialog = new ProgressDialog(ApplyForLeave.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			ApplyThread thread1 = new ApplyThread();
			thread1.start();
		} catch (Exception e) {

		}
	}

	// ......................................Thread for Apply For
	// Leave......................//

	private class ApplyThread extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public ApplyThread() {
		}

		@Override
		public void run() {
			try {
				if (isNetworkAvailable()) {
					newsresponse = Apply_Webservice.ApplyForLeavDetails(
							Employee_ID, Type, Apply_Date, No_Of_Dates,
							From_Date, To_Date, Reason);
					System.out.println("response is " + newsresponse);
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
			case ApplyThread.SUCCESS:
				pDialog.dismiss();

				try {
					Apply_Json_Parse.parseApply(newsresponse);
					ID = Apply_Json_Parse.Get_Id();
					System.out.println("The new Id Is......" + ID);
					// IdArray ;
					New_Result = Apply_Json_Parse.Get_Result();
					System.out.println("New result is " + New_Result);
					New_reason = Apply_Json_Parse.Get_Reason();
				} catch (Exception e) {

					e.printStackTrace();
				}

				if (New_Result.equals("Applied Successfully")) {

					Toast.makeText(getApplicationContext(),
							" You Successfully Applied.. ", Toast.LENGTH_SHORT)
							.show();

				} else {
					Toast.makeText(getApplicationContext(),
							"Failed " + New_reason, Toast.LENGTH_SHORT).show();

				}

			default:
				pDialog.dismiss();
				break;
			}
			super.handleMessage(msg);
		}

	};

	// ....................Checking Network Availability......................//

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
