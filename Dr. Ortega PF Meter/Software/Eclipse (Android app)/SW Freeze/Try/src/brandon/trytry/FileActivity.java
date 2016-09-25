package brandon.trytry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FileActivity extends Activity {

	Button butt;
	EditText edit1;
	EditText edit2;
	View v;
	String filename="DATES";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);

		edit1 = (EditText) findViewById(R.id.editText1);

		edit2 = (EditText) findViewById(R.id.editText2);

		butt = (Button) findViewById(R.id.button1);
		
		v=(View)findViewById(R.layout.activity_file);
		
		
		makerTexter(readFile(this,filename,100).toString());
		
		butt.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View view, MotionEvent event) {
	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                butt.setBackgroundColor(Color.RED);
	            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                butt.setBackgroundColor(Color.BLUE);
	            }
	            return false;
	        }

	    });
		butt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String strMsgToSave = edit1.getText().toString();
				FileOutputStream fos;
				/*try
				{
				    //fos = openFileOutput( filename, Context.MODE_PRIVATE);
				    //fos = openFileOutput( filename, Context.MODE_APPEND);
				    try
				    {
				        fos.write( strMsgToSave.getBytes() );
				        fos.close();

				    }
				    catch (IOException e)
				    {
				        e.printStackTrace();
				    }

				}
				catch (FileNotFoundException e)
				{
				    e.printStackTrace();
				}*/
				
				int ch;
				StringBuffer fileContent = new StringBuffer("");
				FileInputStream fis;
				try {
				    fis = openFileInput( filename );
				    try {
				        while( (ch = fis.read()) != -1)
				            fileContent.append((char)ch);
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				}

				String data = new String(fileContent);
				makerTexter(data);
				
			}
		});
	}
	protected void makerTexter(String s) {
		edit2.setText(s);
	}

	public String readFile(Context context, String fn, int readBlockSize) {
		String s="";
		try {
			 FileInputStream fileIn=openFileInput(fn);
			 InputStreamReader InputRead= new InputStreamReader(fileIn);
			 
			 char[] inputBuffer= new char[readBlockSize];
			 
			 int charRead;
			 
			 while ((charRead=InputRead.read(inputBuffer))>0) {
			 // char to string conversion
			 String readstring=String.copyValueOf(inputBuffer,0,charRead);
			 s +=readstring; 
			 }
			 InputRead.close();
			 Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
			 
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
		return s;
	}
}
