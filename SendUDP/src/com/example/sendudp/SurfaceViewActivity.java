 package com.example.sendudp;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SurfaceViewActivity extends Activity implements Runnable {
  private Button swapBtn;
  private SurfaceView surface;
  private SurfaceHolder holder;
  private boolean locker=true;
  private Thread thread;
 
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        
        swapBtn = (Button) findViewById(R.id.buttonswap);
        
        surface = (SurfaceView) findViewById(R.id.mysurface);
        holder = surface.getHolder();
        
        thread = new Thread(this);
        thread.start();
        swapBtn.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View arg0) {
        /** SWAP ANIMATION LEFT OR RIGHT CIRCLE*/
    	 needleAngle = 0;
      }
    });
    }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
    while(locker){
      //checks if the lockCanvas() method will be success,and if not, will check this statement again
      if(!holder.getSurface().isValid()){
        continue;
      }
      /** Start editing pixels in this surface.*/
      Canvas canvas = holder.lockCanvas();
      
      //ALL PAINT-JOB MAKE IN draw(canvas); method.
      draw(canvas);
      
      // End of painting to canvas. system will paint with this canvas,to the surface.
      holder.unlockCanvasAndPost(canvas);
    }
  }
  /**This method deals with paint-works. Also will paint something in background*/
  int needleAngle = 0;
  long lastTime = 0;
  
  private void draw(Canvas canvas) {
    // paint a background color
    canvas.drawColor(Color.WHITE);
    Paint paint = new Paint();
    int width = canvas.getWidth();
    int height = canvas.getHeight();
    paint.setARGB(255, 255, 255, 255); //paint color GRAY+SEMY TRANSPARENT 
    canvas.drawRect(0,0,width,height , paint );
    
    if(needleAngle>359) needleAngle = 0;
	
    
   
    // Gauge properties
    int radius = height/2-10; 
    int center_x = radius + 10; // small offsets from sides
    int center_y = height/2;
    int tick_length = 40;
    
    // Draw gauge outer border
    paint.setColor(Color.BLACK);
    paint.setStrokeWidth(10);
    paint.setStyle(Paint.Style.STROKE);
    canvas.drawCircle(center_x, center_y, radius, paint);
    
    //Draw gauge needle center
    int needleWidth = 30;
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.RED);
    canvas.drawCircle(center_x, center_y, needleWidth/2, paint);
    
    
    for(int angle = 0; angle < 360; angle+=15) {
    	
    	// Draw tick marks
    	paint.setStyle(Paint.Style.STROKE);
    	paint.setColor(Color.BLACK);
    	paint.setStrokeWidth(5);
    	int start_x = center_x - (int) ((radius-tick_length)*Math.cos(Math.toRadians((double)angle)));
    	int start_y = center_y - (int) ((radius-tick_length)*Math.sin(Math.toRadians((double)angle)));
    	int end_x = center_x - (int) (radius*Math.cos(Math.toRadians((double)angle)));
    	int end_y = center_y - (int) (radius*Math.sin(Math.toRadians((double)angle)));
    	canvas.drawLine(start_x, start_y, end_x, end_y, paint);
        
    	// Draw tick mark labels
    	int text_x = center_x - 35 - (int) (0.8*radius*Math.cos(Math.toRadians((double)angle)));
    	int text_y = center_y - (int) (0.8*radius*Math.sin(Math.toRadians((double)angle)));
    	paint.setTypeface(Typeface.MONOSPACE);
    	paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(40);
        canvas.drawText(Integer.toString(angle), text_x, text_y, paint);
    }
    
    // Draw needle line
    int start_x = center_x;
    int start_y = center_y;
    int end_x = center_x - (int) (.5*radius*Math.cos(Math.toRadians((double)needleAngle)));
    int end_y = center_y - (int) (.5*radius*Math.sin(Math.toRadians((double)needleAngle)));
    paint.setColor(Color.RED);
    paint.setStrokeWidth(needleWidth);
	canvas.drawLine(start_x, start_y, end_x, end_y, paint);
	
	// draw needle triangle;
	
	
	paint.setStyle(Paint.Style.FILL);
	int p1_x = end_x + (int) (needleWidth/2*Math.sin(Math.toRadians((double)needleAngle)));
	int p1_y = end_y - (int) (needleWidth/2*Math.cos(Math.toRadians((double)needleAngle)));
	
	int p2_x = end_x - (int) (needleWidth/2*Math.sin(Math.toRadians((double)needleAngle)));
	int p2_y = end_y + (int) (needleWidth/2*Math.cos(Math.toRadians((double)needleAngle)));
	
	int p3_x = center_x - (int) (.75*radius*Math.cos(Math.toRadians((double)needleAngle)));
    int p3_y = center_y - (int) (.75*radius*Math.sin(Math.toRadians((double)needleAngle)));
    

    
    
    Path path = new Path();
    path.reset();
    paint.setStrokeWidth(3);
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
    path.setFillType(Path.FillType.EVEN_ODD);
    path.moveTo(p1_x, p1_y);
    path.lineTo(p2_x, p2_y);
    path.lineTo(p3_x, p3_y);
    path.lineTo(p1_x, p1_y);
    path.close();

    canvas.drawPath(path, paint);
    
    
    
    
	// Display frames per second in bottom left hand corner of surfaceView
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.BLACK);
    paint.setTypeface(Typeface.MONOSPACE);
    paint.setTextSize(80);
    long thisTime = System.currentTimeMillis();
    long diffTime = thisTime - lastTime;
    long frameRate = 1000 / diffTime;
    canvas.drawText(Long.toString(frameRate), 0, height-10, paint);
    lastTime = thisTime;
    needleAngle++;
    
  }

  // Pause / Resume Thread

  @Override
  protected void onPause() {    
    super.onPause();
    pause();
  }
  
  private void pause() {
    //CLOSE LOCKER FOR run();
    locker = false;
    while(true){
      try {
        //WAIT UNTIL THREAD DIE, THEN EXIT WHILE LOOP AND RELEASE a thread
        thread.join();
      } catch (InterruptedException e) {e.printStackTrace();
      }
      break;
    }
    thread = null;
  }

  @Override
  protected void onResume() {
    super.onResume();
    resume();    
  }

  private void resume() {
    //RESTART THREAD AND OPEN LOCKER FOR run();
    locker = true;
    
  }
} 