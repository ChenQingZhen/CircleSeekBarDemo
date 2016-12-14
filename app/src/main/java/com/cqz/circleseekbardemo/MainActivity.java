package com.cqz.circleseekbardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cqz.mylibrary.CircleSeekBar;

public class MainActivity extends AppCompatActivity implements CircleSeekBar.OnProgressChangeListener, SeekBar.OnSeekBarChangeListener
{
    private CircleSeekBar mCircleSeekBar;
    private TextView mProgressTv;
    private SeekBar mProgressSb;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {
        mCircleSeekBar= (CircleSeekBar) findViewById(R.id.circle_seek_bar);
        mCircleSeekBar.setOnProgressChangeListener(this);
        mProgressTv= (TextView) findViewById(R.id.tv_progress);
        mProgressSb= (SeekBar) findViewById(R.id.sb_progress);
        mProgressSb.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgress(int progress)
    {
        mProgressTv.setText("progress:"+progress);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {
        mCircleSeekBar.setCurrentProgress(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }
}
