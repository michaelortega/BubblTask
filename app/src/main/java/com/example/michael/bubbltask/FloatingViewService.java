package com.example.michael.bubbltask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;



public class FloatingViewService extends Service {
    private WindowManager mWindowManager;

    public View getmFloatingView() {
        return mFloatingView;
    }

    private View mFloatingView;
    private ImageView imageView;

    @SuppressLint("StaticFieldLeak")
    public static FloatingViewService floatingViewService;

    public FloatingViewService() {
    }

    public static FloatingViewService getInstance() {
        return floatingViewService;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_bubble_layout, null);

        imageView = (ImageView) mFloatingView.findViewById(R.id.collapsed_iv);
        initBubbleSize();

        floatingViewService = this;

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
//        //The root element of the expanded view layout
//        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);

        //Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                destroyBubble();
            }
        });


        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                //collapsedView.setVisibility(View.GONE);
                                //expandedView.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(FloatingViewService.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                collapsedView.setVisibility(View.GONE);

                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    private void initBubbleSize() {
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = MainActivity.bubbleWidth;
        layoutParams.height = MainActivity.bubbleHeight;
        imageView.setLayoutParams(layoutParams);
    }



    public void destroyBubble() {
        stopSelf();
    }


    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    public void increaseSize(){
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = imageView.getWidth() + 100;
        layoutParams.height = imageView.getHeight() + 100;
        imageView.setLayoutParams(layoutParams);
        MainActivity.bubbleHeight = layoutParams.height;
        MainActivity.bubbleWidth = layoutParams.width;
    }

    public void decreaseSize(){
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int width =layoutParams.width = imageView.getWidth();
        int height =layoutParams.height = imageView.getHeight();
        if (240 != width && 240 != height){

            layoutParams.width = imageView.getWidth() - 100;
            layoutParams.height = imageView.getHeight() - 100;
            imageView.setLayoutParams(layoutParams);
            MainActivity.bubbleHeight = layoutParams.height;
            MainActivity.bubbleWidth = layoutParams.width;
        }
    }



}