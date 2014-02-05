package leavemanagement.spectrum.net.in;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class Leave_Status_Popup extends Activity implements
		OnItemSelectedListener {
	TextView text1, text2;
	String Spinner;

	String[] total;
	String[] remaing;
	String[] type;
	String newsresponse, Employee_Id;
	SharedPrefs prefff;
	int parslen;
	ProgressDialog pDialog;
	ArrayAdapter aa;

	TextView txttotal, txtremaining, txtcasul, txtsick, txtothers;

	int totalLeave = 0;
	int remainingSumInt = 0;

	public static String[] othertype = {};
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history__leave_status_popup);

		txttotal = (TextView) findViewById(R.id.tvTotalHistoryPopup);
		txtremaining = (TextView) findViewById(R.id.txtremaing);
		txtcasul = (TextView) findViewById(R.id.txtcasul);
		txtsick = (TextView) findViewById(R.id.txtsick);
		txtothers = (TextView) findViewById(R.id.txtothers);

		text1 = (TextView) findViewById(R.id.spinnerlist);

		spinner = (Spinner) findViewById(R.id.others);
		spinner.setOnItemSelectedListener(this);
		prefff = new SharedPrefs(Leave_Status_Popup.this);
		Employee_Id = prefff.getUsername();
		webthread();

	}

	// ..............Spinner list view ..................//

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int postion,
			long arg3) {
		text1.setText(type[postion]);
		Spinner = text1.getText().toString();

		txtothers.setText(remaing[postion]);

		System.out.println("the word in spinner is" + Spinner);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	// .................Receiving data from json................//

	public void webthread() {

		try {
			pDialog = new ProgressDialog(Leave_Status_Popup.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThread thread1 = new NewsThread();
			thread1.start();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "No Network Access  !!",
					000).show();
		}
	}

	private class NewsThread extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public NewsThread() {

		}

		public void run() {
			try {
				if (isNetworkAvailable()) {

					newsresponse = LeaveStatus_webservice
							.leavestatuspop(Employee_Id);
					if (newsresponse.length() > 0 && newsresponse != null) {
						newshandler.sendEmptyMessage(SUCCESS);
					} else {
						newshandler.sendEmptyMessage(NO_DATA);
					}
				} else
					newshandler.sendEmptyMessage(NO_INTERNET);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private Handler newshandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewsThread.SUCCESS:
				String status = "Failed";
				pDialog.dismiss();
				System.out.println("asdghfgnhvgkj");

				try {

					status = LeaveStatusJasonParse
							.parseLeavestatuspopup(newsresponse);

					if (status.equals("Success")) {
						parslen = LeaveStatusJasonParse.parselength();

						total = new String[parslen];
						remaing = new String[parslen];
						type = new String[parslen];

						total = LeaveStatusJasonParse.getleavetotal();

						remaing = LeaveStatusJasonParse.getleaveremaing();
						type = LeaveStatusJasonParse.gettype();

						for (int i = 0; i < parslen; i++) {
							System.out.println("Total leave...." + total[i]);
							totalLeave += Integer.parseInt(total[i]);
							System.out.println("Total leave.............."
									+ totalLeave);
							remainingSumInt += Integer.parseInt(remaing[i]);
						}

						txttotal.setText("" + totalLeave);
						txtremaining.setText("" + remainingSumInt);
						System.out.println("Remaining " + remaing[0] + " "
								+ remaing[1]);
						txtcasul.setText("" + Integer.parseInt(remaing[1]));
						txtsick.setText("" + Integer.parseInt(remaing[0]));
						setSpinnerData();

					}
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

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	protected void setSpinnerData() {
		// TODO Auto-generated method stub
		aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(aa);

	}

}
