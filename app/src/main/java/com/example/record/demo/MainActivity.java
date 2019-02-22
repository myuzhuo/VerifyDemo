package com.example.record.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.record.demo.base.BaseActivity;
import com.example.record.demo.ui.BridgeWebViewActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.button_js_bridge)
    Button jsBridge;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.button_js_bridge})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_js_bridge:
                BridgeWebViewActivity.startIntent(this,"");
                break;
        }
    }
}
