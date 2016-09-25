package brandon.trytry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

public class GraphActivity extends Activity implements OnTouchListener {

	float[] values = null;
	// String[] verlabels = null;
	GraphView graphView;
	private boolean zoom = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// values= new float[] { 2.0f, 1.5f, 2.5f, 1.0f, 3.0f, 5.5f, 1.1f, 6.6f,
		// 2.2f, 0.5f };

		values = getIntent().getFloatArrayExtra("pass");
		// verlabels=getIntent().getStringArrayExtra("pass2");
		String[] verlabels = new String[] { "Peak", "Middle", "Low" };
		String[] horlabels = new String[] { "0", "1", "2", "3", "4", "5", "6" };
		graphView = new GraphView(this, values, "Peak Flow", horlabels,
				verlabels, GraphView.BAR);
		setContentView(graphView);
		graphView.setBackgroundColor(Color.parseColor("#2196F3"));
		graphView.setOnTouchListener(this);
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

			graphView.setScaleX(1.33f);
			graphView.setScaleY(1.33f);
			graphView.setPivotX(X);
			graphView.setPivotY(Y);
			zoom = true;

			break;

		case MotionEvent.ACTION_UP:
			graphView.setScaleX(1f);
			graphView.setScaleY(1f);
			graphView.setPivotX(width / 2);
			graphView.setPivotY(height / 2);
			zoom = false;
			break;

		case MotionEvent.ACTION_MOVE:
			if (zoom) {
				graphView.setPivotX((int) event.getX());
				graphView.setPivotY((int) event.getY());
			}
			break;
		}

		return true;
	}

}