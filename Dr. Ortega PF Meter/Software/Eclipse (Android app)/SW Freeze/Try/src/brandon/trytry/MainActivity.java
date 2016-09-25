package brandon.trytry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class MainActivity extends Activity implements OnTouchListener {
	// https://www.google.com/design/spec/style/color.html#color-color-palette

	// BLUE=#2196F3
	// L_Blue=#BBDEFB
	// LL_Blue=#E1F5FE
	// NEO GREEN=#76FF03
	// D_GREEN=#33691E
	// YELLOW=#FFEB3B

	GraphActivity ga;
	IFace iface;
	boolean startup = true;
	boolean login = false;
	Canvas dims;
	View orig;
	Button button;
	private EditText t1;
	private EditText t2;
	float[] tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		iface = new IFace(this);
		iface.setBackgroundColor(Color.WHITE);
		setContentView(iface);
		iface.setOnTouchListener(this);
		t1 = null;
		t2 = null;
		File yourFile = new File("Dates.txt");
		if(!yourFile.exists()) {
		    try {
				yourFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		try {
			FileOutputStream oFile = new FileOutputStream(yourFile, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int X = (int) event.getX();
		int Y = (int) event.getY();
		int width = this.getResources().getDisplayMetrics().widthPixels;
		int height = this.getResources().getDisplayMetrics().heightPixels;
		int eventaction = event.getAction();

		switch (eventaction) {

		case MotionEvent.ACTION_DOWN:
			if (startup) {
				if (X < width / 2) {
					setContentView(R.layout.activity_login);
					login = true;
					loginButton(login);

				} else {
					System.exit(0);
				}
				startup = false;
			}
			break;

		case MotionEvent.ACTION_UP:
			break;
		}
		return false;
	}

	private void loginButton(boolean log) {
		if (log) {
			t1 = (EditText) findViewById(R.id.text1);
			t2 = (EditText) findViewById(R.id.text2);
			button = (Button) findViewById(R.id.confirm);
			button.setOnTouchListener(new View.OnTouchListener() {

		        @Override
		        public boolean onTouch(View view, MotionEvent event) {
		            if(event.getAction() == MotionEvent.ACTION_UP) {
		                button.setBackgroundColor(Color.parseColor("#76FF03"));
		            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
		                button.setBackgroundColor(Color.parseColor("#444444"));
		            }
		            return false;
		        }

		    });
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// DO
					if (t1.getText().toString().equals("")
							&& t2.getText().toString().equals("")) {
						boolean ago = false;
						View vi = findViewById(R.id.insert);
						orig = findViewById(R.id.original);
						orig.setAlpha(0.2f);
						orig.setOnTouchListener(new View.OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								orig.setAlpha(1f);
								return false;
							}
						});
						showPopup(vi);
					}
				}
			});
		}
	}

	public void showPopup(View anchorView) {

		View popupView = getLayoutInflater().inflate(R.layout.popup_layout,
				null);

		final PopupWindow popupWindow = new PopupWindow(popupView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		final Button his = (Button) popupView.findViewById(R.id.History);
		his.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View view, MotionEvent event) {
	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                his.setBackgroundColor(Color.parseColor("#76FF03"));
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

				Intent intentHist = new Intent(MainActivity.this, History.class);

				startActivity(intentHist);
				orig.setAlpha(1f);
				popupWindow.dismiss();
			}
		});
		final Button con = (Button) popupView.findViewById(R.id.Connect);
		con.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View view, MotionEvent event) {
	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                con.setBackgroundColor(Color.parseColor("#76FF03"));
	            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                con.setBackgroundColor(Color.parseColor("#444444"));
	            }
	            return false;
	        }

	    });
		con.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentBlue = new Intent(MainActivity.this,
						Bluetooth.class); //Bluetooth
				startActivity(intentBlue);
				orig.setAlpha(1f);
				popupWindow.dismiss();
			}
		});

		popupWindow.setFocusable(true);

		// If you need the PopupWindow to dismiss when when touched outside
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				orig.setAlpha(1f);
			}
		});

		int location[] = new int[2];

		// Get the View's(the one that was clicked in the Fragment) location
		// anchorView.getLocationOnScreen(location);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		location[0] = 0;
		location[1] = height / 2;

		// Using location, the PopupWindow will be displayed right under
		// anchorView
		popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0],
				location[1] + anchorView.getHeight());

	}
}
