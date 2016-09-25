package brandon.trytry;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Listing extends ListActivity {

	private SimpleAdapter sa;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		int y = getIntent().getIntExtra("year", 0);
		int m = getIntent().getIntExtra("month", 0);
		int d = getIntent().getIntExtra("day", 0);
		populateListView(y, m, d);

		try {
			FileOutputStream outputStream = getApplicationContext().openFileOutput("text.txt", Listing.MODE_PRIVATE);
			outputStream.write("yo".getBytes());
			outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private void populateListView(int y, int m, int d) {

		String[][] DateAndFlow = new String[][] { { "Alabama", "Montgomery" },
				{ "Alaska", "Juneau" }, { "Arizona", "Phoenix" },
				{ "Arkansas", "Little Rock" }, { "California", "Sacramento" },
				{ "Colorado", "Denver" }, { "Connecticut", "Hartford" },
				{ "Delaware", "Dover" },
				{ "PEF = 15L/s FEV = 4L", d + "d " + m + "m " + y + "y " } };
		HashMap<String, String> item;
		for (int i = 0; i < DateAndFlow.length; i++) {
			item = new HashMap<String, String>();
			item.put("line1", DateAndFlow[i][0]);
			item.put("line2", DateAndFlow[i][1]);
			list.add(item);
		}
		sa = new SimpleAdapter(this, list, R.layout.item, new String[] {
				"line1", "line2" }, new int[] { R.id.line_a, R.id.line_b });

		setListAdapter(sa);
		// R.layout.item

		// ListView list = (ListView)findViewById(R.id.list);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
	}
}
