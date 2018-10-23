package com.example.hbsgz.myweather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class alphLocation extends View {

    private String characters[] = { "#", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z" };
    private int choose = -1;
    private Paint paint = new Paint();
    private ViewTreeObserver.OnTouchModeChangeListener mOnTouchLetterChangedListener;
    private TextView mTextDialog;

    public alphLocation(Context context) {
        super(context);
    }

    public alphLocation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public alphLocation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmOnTouchLetterChangedListener(ViewTreeObserver.OnTouchModeChangeListener mOnTouchLetterChangedListener) {
        this.mOnTouchLetterChangedListener = mOnTouchLetterChangedListener;
    }

    public void setmTextDialog(TextView dialog){
        this.mTextDialog = dialog;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int singleHeight = height/characters.length;
        for(int i=0;i<characters.length;i++){
            paint.setColor(getResources().getColor(R.color.colorAccent));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(150*(float)width/320);
            if(i == choose){
                paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
                paint.setFakeBoldText(true);
            }

            float xPos = width/2 - paint.measureText(characters[i])/2;
            float yPos = singleHeight*i + singleHeight;

            canvas.drawText(characters[i],xPos,yPos,paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        int action = event.getAction();
        float y = event.getY();
        int c = (int)(y/getHeight() * characters.length);
/*
        switch (action){
            case MotionEvent.ACTION_UP:
                choose = -1;
                setBackgroundColor(0x0000);
                invalidate();
                if(mTextDialog != null){
                    mTextDialog.setVisibility(View.GONE);
                }
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (choose != c){
                    if (c>=0&&c<characters.length){
                        if (mOnTouchLetterChangedListener != null){
                            mOnTouchLetterChangedListener.onTouchModeChanged(true);
                        }
                    }
                }
        }
*/
        return true;
    }
}
