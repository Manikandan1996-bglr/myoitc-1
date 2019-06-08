package com.velozion.myoitc.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.velozion.myoitc.BaseActivity;
import com.velozion.myoitc.R;
import com.velozion.myoitc.Utils;

public class DisplayFullImage extends BaseActivity {

    PhotoView imageView;
    private int previousFingerPosition = 0;
    private int imageViewPosition = 0;
    private int defaultViewHeight;

    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarRequired(true);
        setToolbarTitle(getIntent().getStringExtra("profile_name"));
        setContentView(R.layout.activity_display_full_image);

        try {
            getToolbar().setBackgroundColor(getResources().getColor(R.color.black));
            changeStatusbar(getResources().getColor(R.color.black));
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        imageView = findViewById(R.id.fullimagview);


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                final int Y = (int) event.getRawY();

                // Switch on motion event type
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        // save default base layout height
                        defaultViewHeight = imageView.getHeight();

                        // Init finger and view position
                        previousFingerPosition = Y;
                        imageViewPosition = (int) imageView.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        // If user was doing a scroll up
                        if (isScrollingUp) {
                            // Reset imageView position
                            imageView.setY(0);
                            // We are not in scrolling up mode anymore
                            isScrollingUp = false;
                        }

                        // If user was doing a scroll down
                        if (isScrollingDown) {
                            // Reset imageView position
                            imageView.setY(0);
                            // Reset base layout size
                            imageView.getLayoutParams().height = defaultViewHeight;
                            imageView.requestLayout();
                            // We are not in scrolling down mode anymore
                            isScrollingDown = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isClosing) {
                            int currentYPosition = (int) imageView.getY();

                            // If we scroll up
                            if (previousFingerPosition > Y) {
                                // First time android rise an event for "up" move
                                if (!isScrollingUp) {
                                    isScrollingUp = true;
                                }

                                // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                                if (imageView.getHeight() < defaultViewHeight) {
                                    imageView.getLayoutParams().height = imageView.getHeight() - (Y - previousFingerPosition);
                                    imageView.requestLayout();
                                } else {
                                    // Has user scroll enough to "auto close" popup ?
                                    if ((imageViewPosition - currentYPosition) > defaultViewHeight / 4) {
                                        closeUpAndDismissDialog(currentYPosition);
                                        return true;
                                    }

                                    //
                                }
                                imageView.setY(imageView.getY() + (Y - previousFingerPosition));

                            }
                            // If we scroll down
                            else {

                                // First time android rise an event for "down" move
                                if (!isScrollingDown) {
                                    isScrollingDown = true;
                                }

                                // Has user scroll enough to "auto close" popup ?
                                if (Math.abs(imageViewPosition - currentYPosition) > defaultViewHeight / 2) {
                                    closeDownAndDismissDialog(currentYPosition);
                                    return true;
                                }

                                // Change base layout size and position (must change position because view anchor is top left corner)
                                imageView.setY(imageView.getY() + (Y - previousFingerPosition));
                                imageView.getLayoutParams().height = imageView.getHeight() - (Y - previousFingerPosition);
                                imageView.requestLayout();
                            }

                            // Update position
                            previousFingerPosition = Y;
                        }
                        break;
                }


                return true;
            }
        });


        Utils.ImageLoaderInitialization(getApplicationContext());
        Utils.LoadImage(getIntent().getStringExtra("profile_pic"), imageView);

    }

    void changeStatusbar(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    public void closeUpAndDismissDialog(int currentPosition) {


        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(imageView, "y", currentPosition, -imageView.getHeight());
        positionAnimator.setDuration(500);
        positionAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        positionAnimator.start();
    }

    public void closeDownAndDismissDialog(int currentPosition) {


        isClosing = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(imageView, "y", currentPosition, screenHeight + imageView.getHeight());
        positionAnimator.setDuration(500);
        positionAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        positionAnimator.start();
    }


}
