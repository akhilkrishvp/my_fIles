package leavemanagement.spectrum.net.in;

import Adapter.HistoryAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class History extends Activity {
	SharedPrefs prefs;
	ListView list;
	HistoryAdapter adapter;
	String[] dateAppliedArray;
	String[] noOfLeaves;
	String[] statusArray, reason, leaveId, type;
	ProgressDialog pDialog;
	String newsresponse;
	int l, i;
	String[] Name;
	String[] Eid;
	String[] Phone;
	String[] FromAndToDateArray;
	String[] leaveDates;
	String[] from_date, to_date;
	String insidestatus;
	String LEAVID;
	String[] ARD_rslt;

	@Override
	protected void onCreate(Bundle savedInstanceState)

	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.leavehistory);
		prefs = new SharedPrefs(History.this);

		list = (ListView) findViewById(R.id.list);
		registerForContextMenu(list);
		list.setOnItemClickListener(mOnGalleryClick);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int pos, long arg3) {

				String position = Integer.toString(pos);

				insidestatus = statusArray[pos];
				LEAVID = leaveId[pos];
				System.out.println(" leave is ...................." + LEAVID);
				Toast.makeText(getApplicationContext(),
						"postion" + statusArray, 5000).show();

				if (insidestatus.equals("Pending")) {
					canceldail();

					Toast.makeText(getApplicationContext(),
							"Request on pending !", 3000).show();
				}

				else {
					deletedail();
					Toast.makeText(getApplicationContext(),
							"Request on pending !", 3000).show();
				}

				return true;
			}
		});
		try {
			pDialog = new ProgressDialog(History.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThread thread1 = new NewsThread();
			thread1.start();
		} catch (Exception e) {

		}

	};

	private class NewsThread extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public NewsThread() {
		}

		@Override
		public void run() {
			try {
				if (isNetworkAvailable()) {

					newsresponse = History_WebServices
							.getLeaveHistoryListResponse(prefs.getUsername());

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

	@SuppressLint("HandlerLeak")
	private Handler newshandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewsThread.SUCCESS:
				pDialog.dismiss();

				try {
					History_JsonParse.parseHistory(newsresponse);
				} catch (Exception e) {

					e.printStackTrace();
				}

				l = History_JsonParse.appliedLeavesLength();

				dateAppliedArray = new String[l];
				noOfLeaves = new String[l];
				statusArray = new String[l];
				leaveDates = new String[l];
				reason = new String[l];
				leaveId = new String[l];
				from_date = new String[l];
				to_date = new String[l];
				type = new String[l];

				dateAppliedArray = History_JsonParse.getDateApplied();
				noOfLeaves = History_JsonParse.getNoOfLeaves();
				statusArray = History_JsonParse.getLeaveStatus();
				leaveDates = History_JsonParse.getLeaveDates();
				reason = History_JsonParse.getReason();
				leaveId = History_JsonParse.getLeaveId();
				type = History_JsonParse.getType();
				// LEAVID = leaveId.toString();

				adapter = new HistoryAdapter(History.this, dateAppliedArray,
						noOfLeaves, statusArray);
				list.setAdapter(adapter);

				break;
			case NewsThread.NO_DATA:
				showTaost("nO DATA");
				pDialog.dismiss();
				break;
			case NewsThread.NO_INTERNET:
				pDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						History.this);
				builder.setMessage("no Data Connection")
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
		int l = leaveDates2.length;
		System.out.println("LLL " + l);
		from_date = new String[l];
		to_date = new String[l];
		System.out.println("hereeeee");
		for (int i = 0; i < l; i++) {
			String[] fromAndTo = new String[3];
			if (leaveDates2[i].length() != 10) {
				fromAndTo = leaveDates2[i].split(" ");
				from_date[i] = fromAndTo[0];
				System.out
				.println("  From Datessssssssssssssss" + from_date[0]);
				to_date[i] = fromAndTo[2];
				System.out
						.println("  From Datessssssssssssssss" + from_date[0]);
			} else {
				from_date[i] = leaveDates2[i];
				to_date[i] = leaveDates2[i];
			}
		}
	}

	public void showTaost(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();

	}

	public void status(View v) {

		System.out.println("test-----> inside status");

		Intent i = new Intent(History.this, Leave_Status_Popup.class);
		Bundle b1 = new Bundle();
		// b1.putString("FDATE", from_date);
		// b1.putString("TDATE", to_date);
		startActivity(i);

	}

	public OnItemClickListener mOnGalleryClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			String listpostion = statusArray[position];
			if (listpostion.equals("Approved")
					|| listpostion.equals("Cancelled")) {
				deletedail();
				
				
			} else {
				Intent i = new Intent(History.this, History_Popup.class);
				setFromAndToDates(leaveDates);
				Bundle b1 = new Bundle();

				b1.putString("date_applied", dateAppliedArray[position]);
				b1.putString("no_of_leaves", noOfLeaves[position]);
				b1.putString("from_date", from_date[position]);
				b1.putString("to_date", to_date[position]);
				b1.putString("reason", reason[position]);
				b1.putString("leave_status", statusArray[position]);
				b1.putString("leave_id", leaveId[position]);
				b1.putString("type", type[position]);
				i.putExtras(b1);
				startActivity(i);
			}
		}

	};

	// ............dailogue in list on longclick..............//

	public void dailogue()

	{

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(History.this);

		// alertDialog.setTitle("New Requests");

		// alertDialog.setMessage("You have 2 leave request");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						deletedail();

					}
				});

		alertDialog.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						canceldail();
						// Toast.makeText(getApplicationContext(),
						// "You clicked on NO", Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.show();
	}

	// ........delete dailoluge....//

	public void deletedail() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(History.this);

		// alertDialog.setTitle("New Requests");

		alertDialog.setMessage("Are you sure want to delete ?");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						deleterequest();

						Toast.makeText(getApplicationContext(),
								"operation invoked", Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Toast.makeText(getApplicationContext(),
								"operation revoked", Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.show();

	}

	// ........cancel dailogue....//

	public void canceldail() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(History.this);

		// alertDialog.setTitle("New Requests");

		alertDialog.setMessage("Sure to cancel your current leave request ?");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						cancelrequest();
						Intent i = new Intent(History.this, History.class);
						startActivity(i);
						Toast.makeText(getApplicationContext(),
								"operation invoked", Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Toast.makeText(getApplicationContext(),
								"operation revoked", Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.show();

	}

	public void warning() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(History.this);

		// alertDialog.setTitle("New Requests");

		// alertDialog.setMessage("Denied ! Status on pending");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Toast.makeText(getApplicationContext(),
								"operation invoked", Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.show();

	}

	public void directcancel() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(History.this);

		// alertDialog.setTitle("New Requests");

		// alertDialog.setMessage("Sure to cancel your current leave request ?");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						canceldail();
						Toast.makeText(getApplicationContext(),
								"operation invoked", Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.show();

	}

	public void directdelete() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(History.this);

		// alertDialog.setTitle("New Requests");

		// alertDialog.setMessage("Sure to cancel your current leave request ?");

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						deletedail();
						Toast.makeText(getApplicationContext(),
								"operation invoked", Toast.LENGTH_SHORT).show();

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

			Context context = History.this;
			SharedPreferences myPrefs = context.getSharedPreferences("myPrefs",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = myPrefs.edit();
			editor.clear();
			editor.commit();
			Intent i = new Intent(History.this, Login.class);

			startActivity(i);
			return true;
		case R.id.about:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// .............cancel request......................//

	public void cancelrequest() {

		try {
			pDialog = new ProgressDialog(History.this);
			pDialog.setMessage("Cancelling...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThreadCAN thread1 = new NewsThreadCAN();
			thread1.start();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "No Network Access  !!",
					5000).show();
		}
	}

	private class NewsThreadCAN extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public NewsThreadCAN() {

		}

		public void run() {
			try {
				if (isNetworkAvailable()) {

					newsresponse = Canncel_webservice.cancel_leave(
							prefs.getUsername(), LEAVID);
					if (newsresponse.length() > 0 && newsresponse != null) {
						newshandlerCANCEL.sendEmptyMessage(SUCCESS);
					} else {
						newshandlerCANCEL.sendEmptyMessage(NO_DATA);
					}
				} else
					newshandlerCANCEL.sendEmptyMessage(NO_INTERNET);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// newsresponse = View_Request_Webservice.viewrequest();
			// newshandler.sendEmptyMessage(0);
			//
			//

		}
	}

	private Handler newshandlerCANCEL = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewsThreadCAN.SUCCESS:
				pDialog.dismiss();
				boolean status = false;
				try {
					status = ViewRequest_JsonParse.parseViewReqst(newsresponse,
							prefs.getEmpType());
					;
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

	private boolean isNetworkAvailableCANCEL() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	// ..............Delete History....................//

	public void deleterequest() {
		try {
			pDialog = new ProgressDialog(History.this);
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
							prefs.getUsername(), "Delete", LEAVID);
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

					Toast.makeText(getApplicationContext(),
							"Result" + ARD_rslt, 5000).show();
					Intent i = new Intent(History.this, View_Request.class);
					startActivity(i);
				}
			case NewsThreadDelete.NO_DATA:
				Toast.makeText(getApplicationContext(), "nO DATA", 5000).show();
				pDialog.dismiss();
				break;
			case NewsThreadDelete.NO_INTERNET:
				pDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						History.this);
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
