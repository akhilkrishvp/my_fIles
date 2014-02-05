package leavemanagement.spectrum.net.in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
	SharedPrefs myPrefs;

	private final int SPLASH_DISPLAY_LENGHT = 3000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.splash);

		myPrefs = new SharedPrefs(Splash.this);
		final String SPuname = myPrefs.getUsername();
		final String SPemptype = myPrefs.getEmpType();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!SPuname.equals("")) {
					if (SPemptype.equals("Team Leader")
							|| SPuname.equals("admin")) {
						Intent i = new Intent(Splash.this, Administrator.class);
						Splash.this.startActivity(i);
					} else {
						Intent i = new Intent(Splash.this,
								EmployeMainpage.class);
						Splash.this.startActivity(i);
					}
				} else {
					Intent i = new Intent(Splash.this, Login.class);
					Splash.this.startActivity(i);
				}
				Splash.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
}
