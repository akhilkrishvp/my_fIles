package leavemanagement.spectrum.net.in;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	protected static final boolean Teamleader = true;
	protected static final boolean Admin = true;
	// protected static final String Status = null;
	protected static final String success = null;

	// Data to be Shared................................/////

	private SharedPrefs prefs;
	ProgressDialog pDialog;
	String loginresponse;
	String empType;

	Button but;
	EditText unametext, pwdtext;
	String username, password;
	int l, i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		prefs = new SharedPrefs(Login.this);

		unametext = (EditText) findViewById(R.id.username);
		pwdtext = (EditText) findViewById(R.id.password);

	
		but = (Button) findViewById(R.id.Login);
		but.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					username = unametext.getText().toString();
					password = pwdtext.getText().toString();

					pDialog = new ProgressDialog(Login.this);
					pDialog.setMessage("Logging in...");
					pDialog.setCancelable(false);
					pDialog.show();

					NewsThread thread1 = new NewsThread();
					thread1.start();
				} catch (Exception e) {
				}

			}

			// }
		});

		unametext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String str = unametext.getText().toString();
				but.setVisibility(View.VISIBLE);
				if (str.equals("")) {
					but.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	// .......return username and password to webservice.........//

	// .......thread for webresponse.........................//

	private class NewsThread extends Thread {
		public static final int SUCCESS = 0;
		public static final int NO_DATA = 1;
		public static final int NO_INTERNET = -1;

		public NewsThread() {
		}

		@Override
		public void run() {
			try {
				System.out.println("Username getting1111==" + username);
				if (isNetworkAvailable()) {
					System.out.println("Username getting==" + username);
					loginresponse = Login_Webservices.loginDetails(username,
							password);
					System.out.println("response is " + loginresponse);
					if (loginresponse.length() > 0 && loginresponse != null) {
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

	private Handler newshandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewsThread.SUCCESS:
				pDialog.dismiss();
				boolean status = false;

				try {
					status = Login_Jsonparse.parseLogin(loginresponse);
					System.out.println("Statuss: " + status);
				} catch (Exception e) {

					e.printStackTrace();
				}

				if (status) {

					empType = Login_Jsonparse.getEmpType();
					System.out.println("Typee: " + empType);
					Intent i;
					if (empType.equals("Admin")) {
						i = new Intent(Login.this, Administrator.class);
						startActivity(i);

					} else if (empType.equals("Team Leader")) {
						i = new Intent(Login.this, Administrator.class);
					} else {

						i = new Intent(Login.this, EmployeMainpage.class);
					}
					showTaost(" Login Success");
					setPreferences();
					startActivity(i);
				} else {
					showTaost("Login Failed");
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

	protected void setPreferences() {
		// TODO Auto-generated method stub
		prefs = new SharedPrefs(Login.this);
		prefs.setUsername(username);
		prefs.setEmpType(empType);

	}

	public void showTaost(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
