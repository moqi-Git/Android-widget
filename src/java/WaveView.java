import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class WaveView extends View {

    private int mWaveNum = 7;
    private float mRadiusMin = 90f;
    private float mRadiusMax = 1080f;
    private float mWaveInterval = 240f;
    private int mWaveColor = Color.WHITE;
    private int mWaveAlpha = 128;
    private int mBgColor = Color.TRANSPARENT;

    private Paint mPaint;
    private ArrayList<Float> mWaveList;
    private CountDownTimer waveAnim = new CountDownTimer(30000, 16) {
        @Override
        public void onTick(long millisUntilFinished) {
            for (int i = 0; i < mWaveList.size(); i++) {
                mWaveList.set(i, mWaveList.get(i) + 20f);
                if (mWaveList.get(i) < mRadiusMin + mWaveInterval){
                    mWaveList.set(i+1, mRadiusMin);
                    break;
                }
            }
            if (mWaveList.get(0) > mRadiusMax){
                mWaveList.set(0, 0f);
                ArrayList<Float> tempList = transList(mWaveList);
                mWaveList.clear();
                mWaveList.addAll(tempList);
            }

            invalidate();
        }

        @Override
        public void onFinish() {

        }
    };


    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mWaveList = new ArrayList<>(mWaveNum);
        mPaint.setColor(mWaveColor);
        initRadius();
    }

    private void initRadius() {
        mWaveList.add(mRadiusMin);
        for (int i = 1; i < mWaveNum; i++) {
            mWaveList.add(0f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = canvas.getWidth()/2;
        float centerY = canvas.getHeight()/2;
        canvas.drawColor(mBgColor);

        for (int i = 0; i < mWaveList.size(); i++) {
            mPaint.setAlpha(calcAlpha(mWaveList.get(i)));
            canvas.drawCircle(centerX, centerY, mWaveList.get(i), mPaint);
        }
    }


    private ArrayList<Float> transList(ArrayList<Float> list) {
        ArrayList<Float> newList = new ArrayList<>(mWaveNum);
        for (int i = 1; i < list.size(); i++) {
            newList.add(list.get(i));
        }
        newList.add(list.get(0));

        return newList;
    }

    private int calcAlpha(float radius){
        return (int)((mRadiusMax - radius)/mRadiusMax * mWaveAlpha);
    }

    public void startWave(){
        waveAnim.start();
    }

    public void stopWave(){
        waveAnim.cancel();
    }

    public void resetWave(){
        mWaveList.clear();
        mPaint.setAlpha(mWaveAlpha);
        mPaint.setColor(mWaveColor);
        initRadius();
        invalidate();
    }

    public int getmWaveNum() {
        return mWaveNum;
    }

    public void setmWaveNum(int mWaveNum) {
        this.mWaveNum = mWaveNum;
    }

    public float getmRadiusMin() {
        return mRadiusMin;
    }

    public void setmRadiusMin(float mRadiusMin) {
        this.mRadiusMin = mRadiusMin;
        mWaveList.clear();
        initRadius();
    }

    public float getmRadiusMax() {
        return mRadiusMax;
    }

    public void setmRadiusMax(float mRadiusMax) {
        this.mRadiusMax = mRadiusMax;
    }

    public int getmWaveColor() {
        return mWaveColor;
    }

    public void setmWaveColor(int mWaveColor) {
        this.mWaveColor = mWaveColor;
        mPaint.setColor(mWaveColor);
    }
}