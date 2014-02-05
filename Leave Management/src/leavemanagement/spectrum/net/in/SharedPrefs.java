package leavemanagement.spectrum.net.in;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefs {

	private final static String USERNAME = "username";
	private final static String PASSWORD = "password";
	private final static String YEAR = "year";
	private final static String EMPTYPE = "emptype";

	private final static String SHARED_PREFS = "settings";
	private SharedPreferences prefs;
	private Editor editor;

	public SharedPrefs(Context context) {
		prefs = context
				.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
	}

	public void setPassword(String password) {
		editor = prefs.edit();
		editor.putString(PASSWORD, password);
		editor.commit();
	}

	public void setUsername(String username) {
		editor = prefs.edit();
		editor.putString(USERNAME, username);
		editor.commit();
	}

	public void setYear(String year) {
		editor = prefs.edit();
		editor.putString(YEAR, year);
		editor.commit();
	}

	public void setEmpType(String emptype) {
		editor = prefs.edit();
		editor.putString(EMPTYPE, emptype);
		editor.commit();
	}

	public String getUsername() {
		return prefs.getString(USERNAME, "");
	}

	public String getPassword() {
		return prefs.getString(PASSWORD, "");
	}

	public String getYear() {
		return prefs.getString(YEAR, "");
	}

	public String getEmpType() {
		return prefs.getString(EMPTYPE, "");
	}

	public boolean resetPreferences() {
		// TODO Auto-generated method stub
		editor = prefs.edit();
		editor.clear();
		editor.commit();
		return true;

	}

}