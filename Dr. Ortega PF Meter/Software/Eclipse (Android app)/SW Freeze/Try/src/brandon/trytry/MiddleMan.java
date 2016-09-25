package brandon.trytry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MiddleMan extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_middle_man);
		final Button his = (Button) findViewById(R.id.History2);
		his.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View view, MotionEvent event) {
	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                his.setBackgroundColor(Color.parseColor("#E1F5FE"));
	            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                his.setBackgroundColor(Color.parseColor("#444444"));
	            }
	            return false;
	        }

	    });
		his.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// History Class Protocol

				Intent intentHist = new Intent(MiddleMan.this, History.class);

				startActivity(intentHist);
			}
		});
		final Button con = (Button) findViewById(R.id.Connect2);
		con.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View view, MotionEvent event) {
	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                con.setBackgroundColor(Color.parseColor("#E1F5FE"));
	            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                con.setBackgroundColor(Color.parseColor("#444444"));
	            }
	            return false;
	        }

	    });
		con.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentBlue = new Intent(MiddleMan.this,
						Bluetooth.class); //BluetoothGate

				startActivity(intentBlue);
			}
		});
		final Button loi = (Button) findViewById(R.id.Home2);
		loi.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View view, MotionEvent event) {
	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                loi.setBackgroundColor(Color.parseColor("#E1F5FE"));
	            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                loi.setBackgroundColor(Color.parseColor("#444444"));
	            }
	            return false;
	        }

	    });
		loi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentHome = new Intent(MiddleMan.this,
						MainActivity.class); //BluetoothGate

				startActivity(intentHome);
			}
		});
	}
	
}
