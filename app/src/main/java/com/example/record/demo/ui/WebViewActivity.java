package com.example.record.demo.ui;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.record.demo.R;
import com.example.record.demo.base.BaseActivity;

import butterknife.BindView;


/**
 * 展示网页
 */
final public class WebViewActivity extends BaseActivity {


    @BindView(R.id.webView)
    WebView webView;

    public static final String URL = "url";
    private WebSettings settings;



    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(URL);

        settings = webView.getSettings();
        settings.setAllowFileAccess(false);//如果不需要File协议,则这样设置
        //webView  加载视频，下面五行必须
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setPluginState(WebSettings.PluginState.ON);// 支持插件
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
//        settings.setSupportZoom(true);//设置可以支持缩放
//        settings.setBuiltInZoomControls(true);// 设置出现缩放工具
        settings.setUseWideViewPort(true);  //将图片调整到适合webview的大小  无效
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完成
//                    dismissWaitDialog();
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            //设置为网页的标题
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                tvTitle.setText(title);
//            }
        });
        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
                // handleMessage(Message msg);// 进行其他处理
                super.onReceivedSslError(view, handler, error);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

              /*  if (url.toString().contains("sina.cn")){
                    view.loadUrl("http://ask.csdn.net/questions/178242");
                    return true;
                }*/

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("sina.cn")){
                        view.loadUrl("http://ask.csdn.net/questions/178242");
                        return true;
                    }
                }*/

                return false;
            }

        });
        webView.loadUrl(url);
    }

    @Override
    protected void onStart() {
        super.onStart();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        settings.setJavaScriptEnabled(false);
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
