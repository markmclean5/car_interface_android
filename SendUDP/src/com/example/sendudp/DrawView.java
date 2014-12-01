package com.example.sendudp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
    private static final String TAG = "DrawView";

    List<Point> points = new ArrayList<Point>();
    long lastTime = 0;
    int needleAngle = 0;
    
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
    }
    

    @Override
    public void onDraw(Canvas canvas) {
    	
    	if(needleAngle>359) needleAngle = 0;
    	
        int width = this.getWidth();
        int height = this.getHeight();
       
        // Gauge properties
        int radius = width/4; 
        int center_x = radius+100; // small offsets from sides
        int center_y = radius+50;
        int tick_length = 40;
        
        // Left gauge
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(center_x, center_y, radius, paint);
        paint.setStrokeWidth(5);
        
        
        for(int angle = 0; angle < 360; angle+=15) {
        	paint.setStyle(Paint.Style.STROKE);
        	int start_x = center_x + (int) ((radius-tick_length)*Math.cos(Math.toRadians((double)angle)));
        	int start_y = center_y - (int) ((radius-tick_length)*Math.sin(Math.toRadians((double)angle)));
        	
        	int end_x = center_x + (int) (radius*Math.cos(Math.toRadians((double)angle)));
        	int end_y = center_y - (int) (radius*Math.sin(Math.toRadians((double)angle)));
        	
        	canvas.drawLine(start_x, start_y, end_x, end_y, paint);
            
        	int text_x = center_x - 35 + (int) (0.85*radius*Math.cos(Math.toRadians((double)angle)));
        	int text_y = center_y - (int) (0.85*radius*Math.sin(Math.toRadians((double)angle)));
        	paint.setTypeface(Typeface.MONOSPACE);
        	paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            canvas.drawText(Integer.toString(angle), text_x, text_y, paint);
        }
        
        int start_x = center_x;
        int start_y = center_y;
        int end_x = center_x + (int) (.85*radius*Math.cos(Math.toRadians((double)needleAngle)));
        int end_y = center_y - (int) (.85*radius*Math.sin(Math.toRadians((double)needleAngle)));
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);
    	canvas.drawLine(start_x, start_y, end_x, end_y, paint);

        
        
        
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(80);
        long thisTime = System.currentTimeMillis();
        long diffTime = thisTime - lastTime;
        long frameRate = 1000 / diffTime;
        canvas.drawText(Long.toString(frameRate), 0, height-10, paint);
        lastTime = thisTime;
        
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 5, paint);
            
        }
        needleAngle++;
        invalidate();
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);
        paint.setColor(Color.WHITE);

        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        invalidate();
        Log.d(TAG, "point: " + point);
        return true;
    }
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}