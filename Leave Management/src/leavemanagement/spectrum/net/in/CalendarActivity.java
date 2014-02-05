package leavemanagement.spectrum.net.in;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends Activity implements OnClickListener {

	private static final String tag = "MyCalendarActivity";
	SharedPrefs prefs;
	ProgressDialog pDialog;
	String newsresponse;
	private TextView currentMonth;
	private Button selectedEvent;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	private static final String dateTemplate = "MMMM yyyy";
	String dayEvent = "nothing here";
	String[] dateOfEvent;
	String[] eventDescription;
	int l, i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_calendar_view);

		prefs = new SharedPrefs(CalendarActivity.this);

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);

		try {
			pDialog = new ProgressDialog(CalendarActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			NewsThread thread1 = new NewsThread();
			thread1.start();
		} catch (Exception e) {

		}

	}

	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;

		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

			printMonth(month, year);

			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		private void printMonth(int mm, int yy) {
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);

				list.add(String.valueOf(i) + "-WHITE" + "-"
						+ getMonthAsString(currentMonth) + "-" + yy);

			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			String eventString = (String) getEvent(theday, themonth, theyear);
			gridcell.setTag(eventString);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.lightgray02));
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(Color.BLACK);
			}

			if (!eventString.equals("nothing here"))
				gridcell.setTextColor(getResources().getColor(R.color.orange));
			if (isToday(theday, themonth, theyear)) {
				gridcell.setTextColor(Color.BLUE);
				gridcell.setTag("Today");
			}
			if (isToday(theday, themonth, theyear)
					&& !eventString.equals("nothing here"))
				gridcell.setTextColor(Color.YELLOW);
			if (eventString.equals("nothing here")
					&& !isToday(theday, themonth, theyear))
				gridcell.setTag(" ");
			return row;
		}

		@Override
		public void onClick(View view) {
			String eventOfTheDay = (String) view.getTag();
			selectedEvent.setText(eventOfTheDay);
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}

		public int getCurrentMonth() {
			return month;
		}

		public int getCurrentYear() {
			return year;
		}
	}

	public Object getEvent(String theday, String themonth, String theyear) {
		// TODO Auto-generated method stub
		String[] monthsList = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		System.out.println(theday);
		System.out.println(themonth);
		System.out.println(theyear);

		if (Integer.valueOf(theday) < 10)
			theday = "0" + theday;
		int mm = 0;

		for (int i = 0; i < 12; i++)
			if (monthsList[i].equals(themonth))
				mm = i + 1;
		themonth = "" + mm;
		if (Integer.valueOf(mm) < 10)
			themonth = "0" + themonth;

		Calendar myDate = Calendar.getInstance();

		myDate.set(Integer.valueOf(theyear), Integer.valueOf(themonth) - 1,
				Integer.valueOf(theday));
		if (myDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			dayEvent = "Sunday";
		} else {
			dayEvent = "nothing here";
		}

		String ddmmyyyy = theday + "-" + themonth + "-" + theyear;
		int len = dateOfEvent.length;
		for (int i = 0; i < len; i++) {
			if (dateOfEvent[i].equals(ddmmyyyy)) {
				dayEvent = eventDescription[i];
			}
		}

		return dayEvent;

	}

	public boolean isToday(String theday, String themonth, String theyear) {
		// TODO Auto-generated method stub
		String[] monthsList = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };

		for (int i = 0; i < 12; i++)
			if (monthsList[i].equals(themonth))
				themonth = "" + i + 1;

		Calendar today = Calendar.getInstance(Locale.getDefault());
		int day = today.get(Calendar.DAY_OF_MONTH);
		int month = today.get(Calendar.MONTH) + 1;
		int year = today.get(Calendar.YEAR);
		if (day == Integer.valueOf(theday)
				&& month == Integer.valueOf(themonth)
				&& year == Integer.valueOf(theyear))
			return true;
		return false;
	}

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

					newsresponse = Calendar_WebServices.EventList(
							prefs.getUsername(),
							String.valueOf(prefs.getYear()));
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
					Calendar_JsonParse.parseEvents(newsresponse);
				} catch (Exception e) {

					e.printStackTrace();
				}

				l = Calendar_JsonParse.EventLen();

				dateOfEvent = new String[l];
				eventDescription = new String[l];
				dateOfEvent = Calendar_JsonParse.getEventDate();
				eventDescription = Calendar_JsonParse.getEventDescriptioin();

				initializeCalendar();

				break;
			case NewsThread.NO_DATA:
				showTaost("NO DATA");
				pDialog.dismiss();
				break;
			case NewsThread.NO_INTERNET:
				pDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CalendarActivity.this);
				builder.setMessage("no internet")
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

	public void showTaost(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();

	}

	protected void initializeCalendar() {
		// TODO Auto-generated method stub

		String uname = prefs.getUsername();
		String year22 = prefs.getYear();
		System.out.println("Username getting==" + uname);
		System.out.println("Username year ==" + year22);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		selectedEvent = (Button) this.findViewById(R.id.selectedEvent);
		selectedEvent.setText("");

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialized
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);

	}

}
