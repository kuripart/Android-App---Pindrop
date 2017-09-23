package com.kuri.pindrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    FrameLayout frameLayout;
    Bitmap pin;
    Bitmap ball;
    Bitmap noBall;
    Boolean pinFall = false; //to decide when pin will attack
    float pinHeight;
    float pinWidth;
    float yBall; //y position of Ball
    float xBall; //x position of Ball
    float xBallNo; // x -> Punctured
    float yBallNo; // y -> Punctured
    float pinX; // pin's x
    float pinY; // pin's y
    float incrementXBall; //decides how fast ball moves along x axis
    float factorSpeed; //decides pin's speed
    int screenHeight;
    int screenWidth;
    int ballHeight;
    int ballWidth;
    Paint ballPaint, puncturePaint;
    GestureDetector gestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
        gestures = new GestureDetector(this);
        frameLayout = (FrameLayout)findViewById(R.id.graphicsIt);
        Pin pin = new Pin(this);
        frameLayout.addView(pin);
        ballPaint = new Paint();
        ballPaint.setAlpha(255); //ball visible
        puncturePaint = new Paint();
        puncturePaint.setAlpha(0); //no puncture
        xBall = 0; //start from left
        yBall = 760; //near bottom
        xBallNo = 0; //will change as per ball's position
        yBallNo = 800; //near bottom, a little lower since flat
        incrementXBall = 50; //speed changes by 50
        pinY = 0; //pin y start
        pinX = 0; //pin x start
        factorSpeed = 0; //no speed to pin initially
        runIt();
    }

    public class Pin extends View{
        public Pin(Context context){
            super(context);
            pin = BitmapFactory.decodeResource(getResources(),R.drawable.needleok);
            ball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
            noBall = BitmapFactory.decodeResource(getResources(),R.drawable.puncture);
            pinHeight = pin.getHeight();
            pinWidth = pin.getWidth();
            ballHeight = ball.getHeight();
            ballWidth = ball.getWidth();
        }

        @Override
        protected void onDraw(Canvas canvas){
            canvas.drawBitmap(ball,xBall,yBall,ballPaint);
            canvas.drawBitmap(pin,pinX,pinY + factorSpeed,null);
            canvas.drawBitmap(noBall,xBallNo,yBallNo,puncturePaint);
            invalidate();
        }
    }

    public void movePinX(MotionEvent event){
        if(factorSpeed == 0) { //intially can scroll to set pin's x position
            pinX = event.getX();
        }else{ //after pin starts moving, no scrolling
            pinX = pinX + 0;
        }
    }

    public void movePinY(){
        pinFall = true; //when ATTACK button pressed, set to true
    }

    public void resetBall(){
        xBall = 0;
        incrementXBall = 100; //speed increased by 100
        puncturePaint.setAlpha(0); //puncture gone
        ballPaint.setAlpha(255); //ball back
    }

    public void runIt(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                xBall += incrementXBall;
                if(xBall >= screenWidth - ballWidth){
                    xBall = 0; //at end of screen, restart
                }
                //pin falls
                if(pinFall){ //when button ATTACK clicked
                    factorSpeed += 100;
                }
                if(factorSpeed >= screenHeight - pinHeight){ //reached end of screen
                    factorSpeed = 0;
                    pinFall = false; //don't fall
                }
                //check collision
                if(pinX+pinWidth <= ballWidth+xBall && pinX >= xBall){
                    if(factorSpeed >= 600){
                        incrementXBall = 0;
                        xBallNo = xBall; //puncture where needle falls at ball
                        ballPaint.setAlpha(0);
                        puncturePaint.setAlpha(255);
                    }
                }

                handler.postDelayed(this,900);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.isButtonPressed(R.id.attack)){ //at button press, ignore gestures
            return false;
        }else{
            return gestures.onTouchEvent(event);
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) { //reset here
        resetBall();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { //set pin's x position here
        movePinX(e2);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    //BUTTON ATTACK!!
    public void attackIt(View view){
        movePinY();
    }
}
