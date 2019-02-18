package com.example.record.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.example.record.demo.R;
import com.example.record.demo.base.BaseActivity;
import com.example.record.demo.utils.IntentUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * JSBridge的使用方法
 */
final public class BridgeWebViewActivity extends BaseActivity {
    public static final String URL = "url";

    public static void startIntent(Context context,String url){
        Intent intent=new Intent(context,BridgeWebViewActivity.class);
        intent.putExtra(URL,url);
        context.startActivity(intent);
    }

    @BindView(R.id.bridge_webview)
    BridgeWebView webView;

    @BindView(R.id.edit_view)
    EditText editView;

    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.btn_reset)
    Button btnReset;






    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bridge_web_view);
    }

    @Override
    protected void initData() {
        String url = IntentUtils.getStringExtra(getIntent(),URL,"");
        webView.loadUrl(url);
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }


    @Override
    protected void onDestroy() {
        if (webView != null)
            webView.destroy();
        webView = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果按了返回键
            if (webView.canGoBack()) {//如果能返回到上个页面
                webView.goBack();//返回到上个页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
