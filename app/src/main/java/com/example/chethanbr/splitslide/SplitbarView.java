package com.example.chethanbr.splitslide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by CHETHAN B R on 03-Sep-15.
 */
public class SplitbarView extends ImageView {
    double barValue, barWidth;
    EditText etCredit, etQR, etRecharge;
    UpdateSlideListener listener = null;
    private String TAG = this.getClass().getSimpleName();
    private Bitmap leftThumb = BitmapFactory.decodeResource(getResources(),
            R.drawable.split_scrube);
    private Bitmap rightThumb = BitmapFactory.decodeResource(getResources(),
            R.drawable.split_scrube);
    private Bitmap centre_color = BitmapFactory.decodeResource(getResources(),
            R.drawable.splitslider_recharge);

    private Bitmap left_end_color = BitmapFactory.decodeResource(getResources(),
            R.drawable.splitslider_bankaccount);
    private Bitmap right_end_color = BitmapFactory.decodeResource(getResources(),
            R.drawable.splitslider_scanpay);

    private int thumblX, thumbrX;
    private int selectedThumb;
    private int offset;
    private int minwindow = 0;
    double creditValue = 0, rechargeValue = 0, qrValue = 0, widththumblX, widththumbrX;
    RelativeLayout.MarginLayoutParams params = null;


    public SplitbarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SplitbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SplitbarView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("Activity", "onMeasure Called ");
        if (getHeight() > 0)
            init();
    }


    private void init() {
        params = (RelativeLayout.MarginLayoutParams) getLayoutParams();
        barWidth = getWidth() - (params.leftMargin + params.rightMargin);
        printLog("View Height =" + getHeight() + "\t\t Thumb Height :"
                + leftThumb.getHeight());
        if (leftThumb.getHeight() > getHeight())
            getLayoutParams().height = leftThumb.getHeight();
        // initial position here ( should be perameterized )
        thumblX = leftThumb.getWidth();
        thumbrX = getWidth() / 2;
       /* if (creditValue != 0 && rechargeValue != 0 && qrValue != 0 && thumbrX < getWidth() - params.rightMargin && thumblX > params.leftMargin) {
            updateBar((int) qrValue, (int) rechargeValue, (int) creditValue, (int) barValue);
        }*/
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(left_end_color, 0, 0, null);
        canvas.drawBitmap(right_end_color, getWidth() - 7, 0, null);
        //Apply color for left region
        for (int i = 7; i < thumblX - leftThumb.getWidth(); i++) {
            canvas.drawBitmap(left_end_color, i, 0, null);
        }
        //Apply color for Right region
        for (int i = thumbrX + rightThumb.getWidth(); i < getWidth() - 7; i++) {
            canvas.drawBitmap(right_end_color, i, 0, null);
        }
        //Apply color for Center region
        for (int i = thumblX - leftThumb.getWidth(); i < thumbrX + rightThumb.getWidth(); i++) {
            canvas.drawBitmap(centre_color, i, 0, null);
        }
        canvas.drawBitmap(leftThumb, thumblX - leftThumb.getWidth(), 0, null);
        canvas.drawBitmap(rightThumb, thumbrX, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int mx = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (mx >= thumblX - leftThumb.getWidth() && mx <= thumblX) {
                    selectedThumb = 1;
                    offset = mx - thumblX;
                    printLog("Select Thumb 1 " + offset);
                } else if (mx >= thumbrX && mx <= thumbrX + leftThumb.getWidth()) {
                    selectedThumb = 2;
                    offset = thumbrX - mx;
                    printLog("Select Thumb 2" + offset);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                printLog("Mouse Move : " + selectedThumb);
                if (selectedThumb == 1) {
                    Log.i("CHAKRI", "offset Left Thumb X-->" + offset);
                    thumblX = mx - offset;
                    Log.i("CHAKRI", "thumblX in Move-->" + thumblX);
                    // Check to not cross the Right Thumb

                    if (thumblX >= thumbrX - minwindow) {

                        thumblX = thumbrX - minwindow;
                    }

                    //Check the LeftThumbX to not to cross the min point
                    if (event.getRawX() <= params.leftMargin) {
                        thumblX = params.leftMargin;
                    }


                } else if (selectedThumb == 2) {
                    Log.i("CHAKRI", "offset Right Thumb X-->" + offset);
                    thumbrX = mx + offset;
                    //Check the RightThumbX to not to cross the max point
                    if (thumbrX > getWidth() - rightThumb.getWidth()) {
                        thumbrX = getWidth() - rightThumb.getWidth();
                    }

                    // Check for not to cross the leftthumb image
                    if (thumbrX <= thumblX + minwindow) {
                        thumbrX = thumblX + minwindow;
                    }

                }
                widththumblX = thumblX - params.leftMargin;
                creditValue = (int) ((barValue / barWidth) * widththumblX);
                widththumbrX = thumbrX - (widththumblX);
                rechargeValue = (int) ((barValue / barWidth) * widththumbrX);
                qrValue = barValue - (rechargeValue + creditValue);
                Log.i("CHAKRI", "Credit value=" + creditValue);
                Log.i("CHAKRI", "rechargeValue=" + rechargeValue);
                Log.i("CHAKRI", "qrValue=" + qrValue);


                break;
            case MotionEvent.ACTION_UP:

                selectedThumb = 0;
                break;
        }
        invalidate();

        return true;
    }

    public void setUpdateSlideListener(UpdateSlideListener listener) {

        this.listener = listener;

    }

    public void updateBar(int qrAmount, int rechargeAmount, int creditAmount, int totalAmount) {
        Log.i("CHAKRI", "inUpdate Bar ");

        barValue = totalAmount;
     /*   thumblX = (int) ((barWidth / barValue) * creditAmount) + params.leftMargin;
        thumbrX = (int) (thumblX + ((barWidth / barValue) * rechargeAmount));*/
        Log.i("CHAKRI", "ThumblX: " + thumblX);
        Log.i("CHAKRI", "ThumbrX: " + thumbrX);
//        invalidate();

    }


    public void setETtoUpdate(EditText creditet, EditText rechargeet, EditText qret) {


        etCredit = creditet;
        etRecharge = rechargeet;
        etQR = qret;
    }

    private void printLog(String log) {
        Log.i(TAG, log);
    }
}