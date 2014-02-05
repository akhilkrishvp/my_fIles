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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.Toast;

public class Administrator extends Activity {
	Button Apply;
	Button History;
	Button Holiday;
	Button Request;
	String newsresponse;
	int count=0;
	SharedPrefs prefs;
	String NoOfRequests;
	ProgressDialog pDialog;
	String Employee_Id,Status;
	int len;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_main_page);
		prefs = new SharedPrefs(Administrator.this);
		Apply = (Button) findViewById(R.id.apply);
		History = (Button) findViewById(R.id.leave);
		Holiday = (Button) findViewById(R.id.holidays);
		Request = (Button) findViewById(R.id.request);
		Employee_Id=prefs.getUsername();
		Apply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Employee_Id.equals("admin"))
				{
					Toast.makeText(getApplicationContext(), "Logged in as Administrator", Toast.LENGTH_SHORT).show();
				}
				else{
				Intent i = new Intent(Administrator.this, ApplyForLeave.class);
				startActivity(i);
				}
			}
		});
		History.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Employee_Id.equals("admin"))
				{
					Toast.makeText(getApplicationContext(), "Logged in as Administrator", Toast.LENGTH_SHORT).show();
				}
				else{
				Intent i = new Intent(Administrator.this, History.class);
				startActivity(i);
				}
			}
		});
		Holiday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Administrator.this,
						CalendarActivity.class);
				startActivity(i);
			}
		});
		Request.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Administrator.this, View_Request.class);
				startActivity(i);
			}
		});
		request();
	}
		public void request()
		{
			

				try {
					pDialog = new ProgressDialog(Administrator.this);
					pDialog.setMessage("Updating...");
					pDialog.setCancelable(false);
					pDialog.show();

					NewsThread thread1 = new NewsThread();
					thread1.start();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "No Network Access  !!",
							5000).show();
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
						if (isNetworkAvailable()) 
						{

							newsresponse = Pending_Request_webservices.pendingequest(prefs
									.getUsername());
							System.out.println("pending response is..........."+newsresponse);
							if (newsresponse.length() > 0 && newsresponse != null)
							{
								newshandler.sendEmptyMessage(SUCCESS);
							} else 
							{
								newshandler.sendEmptyMessage(NO_DATA);
							}
						}
						else
							newshandler.sendEmptyMessage(NO_INTERNET);
					}
					catch (Exception e) 
					{
						e.printStackTrace();
					}

			//			newsresponse = View_Request_Webservice.viewrequest();
			//		newshandler.sendEmptyMessage(0);
//						
		//

				}
			}

			private Handler newshandler = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 0:
						pDialog.dismiss();
						try {
							Pending_request_Json.pendingRequest(newsresponse);
							NoOfRequests=Pending_request_Json.Get_No_of_requests();
							System.out.println("NoOfRequests..............."+NoOfRequests);
							alert();
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








		//..........................New Request popop up...........//
			
			
			public void alert()
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(Administrator.this);

				alertDialog.setTitle("New Requests");
				
				//int counter=count-1;
				
				alertDialog.setMessage("You have "+NoOfRequests+" new leave request");

				alertDialog.setIcon(R.drawable.ic_launcher);

				alertDialog.setPositiveButton("VIEW",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
		                          Intent i=new Intent(Administrator.this,View_Request.class);
		                          startActivity(i);
		                          count=0;
		                          
								
							}
						});
			
				alertDialog.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								// Toast.makeText(getApplicationContext(),
								// "You clicked on NO", Toast.LENGTH_SHORT).show();

							}
						});

				alertDialog.show();
			}

	// ..........................Menu Items.....................//

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
