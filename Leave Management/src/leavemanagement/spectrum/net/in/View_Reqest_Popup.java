package leavemanagement.spectrum.net.in;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class View_Reqest_Popup extends Activity

{
	String name;
	SharedPrefs prefs;
	String[] ARD_rslt;
	String dept, leaveStatus, REQID;
	String type, from, to, reason, nodays;
	TextView txtName, txtDept, txttype, txtfrom, txtto, txtreason,
			txtno_of_days;
	Button approve, reject;
	ProgressDialog pDialog;
	String newsresponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_request_popup);
		prefs = new SharedPrefs(View_Reqest_Popup.this);
		txtName = (TextView) findViewById(R.id.txtname);
		txtDept = (TextView) findViewById(R.id.txtdept);
		txttype = (TextView) findViewById(R.id.txttype);
		txtfrom = (TextView) findViewById(R.id.txtfrom);
		txtto = (TextView) findViewById(R.id.txtto);
		txtreason = (TextView) findViewById(R.id.txtreason);
		txtno_of_days = (TextView) findViewById(R.id.txtdays);
		approve = (Button) findViewById(R.id.Approve);
		reject = (Button) findViewById(R.id.Disapprove);

		Bundle b1 = getIntent().getExtras();
		name = b1.getString("B_NAME");
		dept = b1.getString("B_DEPT");
		REQID = b1.getString("B_ID");
		type = b1.getString("B_TYPE");
		reason = b1.getString("B_REASON");
		from = b1.getString("B_FROM");
		to = b1.getString("B_TO");
		nodays = b1.getString("B_DAYS");

		txtName.setText(name);
		txtDept.setText(dept);
		txttype.setText(type);
		txtfrom.setText(from);
		txtto.setText(to);
		txtreason.setText(reason);
		txtno_of_days.setText(nodays);
	}

	public void APPROVE(View v) {

		try {
			pDialog = new ProgressDialog(View_Reqest_Popup.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThreadAPPR thread1 = new NewsThreadAPPR();
			thread1.start();
		} catch (Exception e) {
		}
	}

	private class NewsThreadAPPR extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public NewsThreadAPPR() {

		}

		public void run() {
			try {
				if (isNetworkAvailable1()) {

					newsresponse = Approve_Reject_Delete_webservice.ARD(
							prefs.getUsername(), "Approve", REQID);

					System.out.println("approve responje--------->"
							+ newsresponse);
					if (newsresponse.length() > 0 && newsresponse != null) {
						newshandlerAPPROVE.sendEmptyMessage(SUCCESS);
					} else {
						newshandlerAPPROVE.sendEmptyMessage(NO_DATA);
					}
				} else
					newshandlerAPPROVE.sendEmptyMessage(NO_INTERNET);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private Handler newshandlerAPPROVE = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewsThreadAPPR.SUCCESS:
				pDialog.dismiss();
				boolean status = false;

				try {
					status = Approve_Reject_Delete_jsonparse
							.parseARD(newsresponse);
				} catch (Exception e) {

					e.printStackTrace();
				}
				if (status) {

					
					ARD_rslt = Approve_Reject_Delete_jsonparse.getresult();

						Intent i = new Intent(View_Reqest_Popup.this,
							View_Request.class);
					startActivity(i);
				}
			case NewsThreadAPPR.NO_DATA:
					pDialog.dismiss();
				break;
			case NewsThreadAPPR.NO_INTERNET:
				pDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						View_Reqest_Popup.this);
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
				pDialog.dismiss();
				break;
			}

			super.handleMessage(msg);
		}

	};

	private boolean isNetworkAvailable1() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void DISAPPROVE(View vv) {

		try {
			pDialog = new ProgressDialog(View_Reqest_Popup.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThreadDISS thread1 = new NewsThreadDISS();
			thread1.start();
		} catch (Exception e) {
		}
	}

	private class NewsThreadDISS extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public NewsThreadDISS() {

		}

		public void run() {
			try {
				if (isNetworkAvailable1()) {

					newsresponse = Approve_Reject_Delete_webservice.ARD(
							prefs.getUsername(),"Reject",REQID);
					if (newsresponse.length() > 0 && newsresponse != null) {
						newshandlerDISS.sendEmptyMessage(SUCCESS);
					} else {
						newshandlerDISS.sendEmptyMessage(NO_DATA);
					}
				} else
					newshandlerDISS.sendEmptyMessage(NO_INTERNET);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private Handler newshandlerDISS = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewsThreadDISS.SUCCESS:
				pDialog.dismiss();
				boolean status = false;

				try {
					status = Approve_Reject_Delete_jsonparse
							.parseARD(newsresponse);
				} catch (Exception e) {

					e.printStackTrace();
				}
				System.out.println("statusd---------" + status);
				if (status) {

					
					ARD_rslt = Approve_Reject_Delete_jsonparse.getresult();

						Intent i = new Intent(View_Reqest_Popup.this,
							View_Request.class);
					startActivity(i);
				}
			case NewsThreadDISS.NO_DATA:
				pDialog.dismiss();
				break;
			case NewsThreadDISS.NO_INTERNET:
				pDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						View_Reqest_Popup.this);
				builder.setMessage("No Data Connection")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// do things
									}
								});
				AlertDialog alert = builder.create();

				alert.show();

			default:
				pDialog.dismiss();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private boolean isNetworkAvailable2() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
