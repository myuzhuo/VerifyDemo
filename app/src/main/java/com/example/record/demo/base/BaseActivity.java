package com.example.record.demo.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("WrongConstant")
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    protected abstract void initView(Bundle savedInstanceState);
    protected abstract void initData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        initData();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder=ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder=ButterKnife.bind(this);
    }
}
