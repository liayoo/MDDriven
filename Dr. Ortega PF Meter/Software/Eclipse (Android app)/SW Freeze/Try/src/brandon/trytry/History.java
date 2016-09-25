package brandon.trytry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Switch;
import android.widget.TextView;

public class History extends Activity {

	CalendarView cv;
	TextView tv;
	Button bt;
	String Time;
	Bundle b;
	SimpleDateFormat df;
	Date date;
	Handler h = new Handler();
	boolean off;
	boolean click;
	boolean timing;
	Calendar today = Calendar.getInstance();
	Calendar reach;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		off = false;
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.history_layout);

		cv = (CalendarView) findViewById(R.id.calendarView1);
		cv.setBackgroundColor(Color.parseColor("#BBDEFB"));
		cv.setVerticalFadingEdgeEnabled(true);
		df = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a");
		click = false;
		Calendar time = Calendar.getInstance();
		Calendar time2 = Calendar.getInstance();
		time.add(today.DATE, -(365 / 4));// 365 days in year
		time2.add(today.DATE, 0);
		cv.setMinDate(time.getTimeInMillis());
		cv.setMaxDate(time2.getTimeInMillis());
		cv.setDate(today.getTimeInMillis(), true, true);
		cv.getBackground().setColorFilter(
                new LightingColorFilter(0xff888888, 0x000000));
		cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				click = true;
				b = new Bundle();

				b.putInt("year", year);
				b.putInt("month", month + 1);
				b.putInt("day", dayOfMonth);

			}
		});

		tv = (TextView) findViewById(R.id.textView1);

		bt = (Button) findViewById(R.id.confirm);

		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentList = new Intent(History.this, Listing.class);
				if (!click) {
					b = new Bundle();

					b.putInt("year", today.get(Calendar.YEAR));
					b.putInt("month", today.get(Calendar.MONTH) + 1);
					b.putInt("day", today.get(Calendar.DAY_OF_MONTH));
				}
				intentList.putExtras(b);

				startActivity(intentList);
			}
		});

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					reach = Calendar.getInstance();
					date = reach.getTime();
					Time = df.format(date);
					h.post(new Runnable() {

						@Override
						public void run() {

							tv.setText(Time);
						}

					});
				}

			}
		}).start();
	}
	@Override
	public void onBackPressed() {
		Intent intentBack = new Intent(History.this, MiddleMan.class);
		startActivity(intentBack);
		
	    return;
	}

}
