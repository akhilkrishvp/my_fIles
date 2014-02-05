package leavemanagement.spectrum.net.in;


import Adapter.ViewRequestAdapter;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class View_Request extends Activity {
	ListView list;
	SharedPrefs prefs;
	ViewRequestAdapter adapter;
	int i, l;
	 String leave_id;
	ProgressDialog pDialog;
	String newsresponse;
	int ARD_length;
	String[] ARD_rslt;
	String[] name, dept, leaveStatus, req_id;
	String[] type, from, to, reason, nodays, leavedate;

	protected void onCreate(Bundle savedInstanceState)

	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewrequest);
		prefs = new SharedPrefs(View_Request.this);

		list = (ListView) findViewById(R.id.list);

		list.setOnItemClickListener(mOnGalleryClick);

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int pos, long arg3) {

				String insidestatus = leaveStatus[pos];

				if (insidestatus.equals("Pending")) {
					warning();
					
				}

				else if (insidestatus.equals("Cancelled")) {
					cannceled();
				}
				else if (insidestatus.equals("Approved")) {
					deletedail();
				}
				else if (insidestatus.equals("Rejected")) {
					deletedail();
				}
				else {
					dailogue();
				}

				return true;
			}
		});
		webthread();

		// .....single onclick in list.............//

	}

	private OnItemClickListener mOnGalleryClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			String pos = Integer.toString(position);
			System.out.println("position is " + position);
             leave_id=req_id[position];
			String typepos = leaveStatus[position];
			if (typepos.equals("Approved")) {
				deletedail();
			} else if (typepos.equals("Cancelled")) {
				cannceled();

			}
			else if (typepos.equals("Rejected")) {
				deletedail();

			}

			// ........bundle for leave request pop up.............//

			else {
				Intent i = new Intent(View_Request.this,
						View_Reqest_Popup.class);
				setFromAndToDates(leavedate);
				Bundle b1 = new Bundle();

				b1.putString("B_NAME", name[position]);
				b1.putString("B_DEPT", dept[position]);
				b1.putString("B_TYPE", type[position]);
				b1.putString("B_FROM", from[position]);
				b1.putString("B_TO", to[position]);
				b1.putString("B_REASON", reason[position]);
				b1.putString("B_DAYS", nodays[position]);
				b1.putString("B_ID", req_id[position]);

				i.putExtras(b1);

				startActivity(i);

			}
		}
	};

	// .........................delete on long press...............//

	public void dailogue()

	{

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				View_Request.this);

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setNeutralButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						{
							deletedail();

							
						}

					}
				});

		alertDialog.show();
	}

	// .............delete options.....................//

	public void deletedail() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				View_Request.this);

		
		alertDialog.setMessage("Are you sure want to delete ?");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						 Delete_Request();
						
					}
				});

		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertDialog.show();
	}

	// ..........thread for loading list................//

	public void webthread() {

		try {
			pDialog = new ProgressDialog(View_Request.this);
			pDialog.setMessage("Checking...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThread thread1 = new NewsThread();
			thread1.start();
		} catch (Exception e) {
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

					newsresponse = View_Request_Webservice.viewrequest(prefs
							.getUsername());
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
				pDialog.dismiss();
				boolean status = false;

				try {
					status = ViewRequest_JsonParse.parseViewReqst(newsresponse,
							prefs.getEmpType());
				} catch (Exception e) {

					e.printStackTrace();
				}
				if (status) {
					l = ViewRequest_JsonParse.parselength();
					name = new String[l];
					dept = new String[l];
					leaveStatus = new String[l];
					type = new String[l];
					req_id = new String[l];
					reason = new String[l];
					leavedate = new String[l];
					
					
					name = ViewRequest_JsonParse.getName();
					dept = ViewRequest_JsonParse.getDepartment();
					leaveStatus = ViewRequest_JsonParse.getRequestStatus();
					req_id = ViewRequest_JsonParse.getRequestId();
					nodays = ViewRequest_JsonParse.getNoOfDays();
					type = ViewRequest_JsonParse.getEmpType();
					reason = ViewRequest_JsonParse.getReason();
					leavedate = ViewRequest_JsonParse.getLeaveDates();
					adapter = new ViewRequestAdapter(View_Request.this, name,
							dept, leaveStatus);
					list.setAdapter(adapter);
				}
			case NewsThread.NO_DATA:
				
				pDialog.dismiss();
				break;
			case NewsThread.NO_INTERNET:
				pDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						View_Request.this);
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

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	protected void setFromAndToDates(String[] leaveDates2) {
		// TODO Auto-generated method stub
		int l = leavedate.length;
		from = new String[l];
		to = new String[l];
		for (int i = 0; i < l; i++) {
			String[] fromAndTo = new String[3];
			if (leaveDates2[i].length() != 10) {
				fromAndTo = leaveDates2[i].split(" ");
				from[i] = fromAndTo[0];
				to[i] = fromAndTo[2];
			} else {
				from[i] = leaveDates2[i];
				to[i] = leaveDates2[i];
			}
		}
	}

	public void showTaost(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();

	}

	// .......................warning for pending...........///

	public void warning() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				View_Request.this);

		// alertDialog.setTitle("New Requests");

		alertDialog.setMessage("Denied ! Status on pending");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						
					}
				});

		alertDialog.show();

	}

	// ............................Menu options..................//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.logout:

			return true;
		case R.id.about:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	// ............cancelled leave dail............//

	public void cannceled() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				View_Request.this);

		// alertDialog.setTitle("New Requests");

		alertDialog.setMessage("Request Cancelled by  Employee, Delete ?");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
                        deletedail();
						
					}
				});

		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						
					}
				});
		alertDialog.show();
	}

	// ...............Approve Delete Reject.................//

	public void Delete_Request(){

		try {
			pDialog = new ProgressDialog(View_Request.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThreadDelete thread1 = new NewsThreadDelete();
			thread1.start();
		} catch (Exception e) {
		}
	}

	private class NewsThreadDelete extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public NewsThreadDelete() {

		}

		public void run() {
			try {
				if (isNetworkAvailabledetete()) {

					newsresponse = Approve_Reject_Delete_webservice.ARD(
							prefs.getUsername(),"Delete", leave_id);
					if (newsresponse.length() > 0 && newsresponse != null) {
						newshandlerdetete.sendEmptyMessage(SUCCESS);
					} else {
						newshandlerdetete.sendEmptyMessage(NO_DATA);
					}
				} else
					newshandlerdetete.sendEmptyMessage(NO_INTERNET);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private Handler newshandlerdetete = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewsThreadDelete.SUCCESS:
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

					// ARD_length=Approve_Reject_Delete_jsonparse.G();

					ARD_rslt = Approve_Reject_Delete_jsonparse.getresult();

					Intent i = new Intent(View_Request.this, View_Request.class);
					startActivity(i);
				}
			case NewsThreadDelete.NO_DATA:
				pDialog.dismiss();
				break;
			case NewsThreadDelete.NO_INTERNET:
				pDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						View_Request.this);
				builder.setMessage("No internet")
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

	private boolean isNetworkAvailabledetete() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
