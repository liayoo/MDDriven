package brandon.trytry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;

public class IFace extends ViewGroup{

	Canvas c;
	Canvas c2;
	MainActivity ma;
	Paint greG;
	Paint oranG;
	Paint darkG;
	Paint white;
	int Cwidth,Cheight;
	public IFace(Context context) {
		super(context);
		ma=new MainActivity();
		c=new Canvas();
		greG=new Paint();
		oranG=new Paint();
		darkG=new Paint();
		white=new Paint();
		Cwidth=c.getWidth();
		Cheight=c.getHeight();
	}
	@Override
    public void onDraw(Canvas c) {
		super.onDraw(c);
		int x=c.getWidth()/90,y=c.getWidth()/90;
		greG.setColor(Color.parseColor("#2196F3"));
        greG.setStyle(Paint.Style.FILL);
        darkG.setColor(Color.parseColor("#76FF03"));
        darkG.setStyle(Paint.Style.STROKE);
        darkG.setStrokeWidth(5);
        
        c.drawPaint(greG);
        white.setStyle(Style.FILL); 
        white.setColor(Color.WHITE);
        white.setFlags(white.ANTI_ALIAS_FLAG);
        white.setTextSize(64); 
        int xx=c.getWidth()/90+c.getWidth()/16;
        float xxx=(float)(c.getWidth()/1.6);
        c.drawText("Sign In", xx, c.getHeight()/2, white);
        c.drawText("Quit", xxx, c.getHeight()/2, white);
        if(ma.startup==true){
        	//ma.startup=false; //put somewhere that declares main screen as gone
        	c.drawRect(x,y,c.getWidth()/2-x,c.getHeight()-y, darkG);
        	
        }
        //invalidate();
    }
	
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}
	

}
