package com.example.record.demo.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.example.record.demo.R;
import com.example.record.demo.base.BaseActivity;
import com.example.record.demo.model.UserInfo;
import com.example.record.demo.ui.jsbridge.MyHandlerCallBack;
import com.example.record.demo.ui.jsbridge.MyWebViewClient;
import com.example.record.demo.ui.view.TipsToast;
import com.example.record.demo.utils.IntentUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * JSBridge的使用方法
 */
final public class BridgeWebViewActivity extends BaseActivity {
    public static final String URL = "url";
    private String TAG="BridgeWebViewActivity";

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


    //自定义HandlerCallBack回调函数
    private MyHandlerCallBack.OnSendDataListener mOnSendDataListener;


    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;



    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bridge_web_view);
        initListener();
        initWebView();
    }

    @Override
    protected void initData() {
//        String url = IntentUtils.getStringExtra(getIntent(),URL,"");
//        webView.loadUrl(url);
    }

    private void initListener(){
        mOnSendDataListener=new MyHandlerCallBack.OnSendDataListener() {
            @Override
            public void sendData(String data) {
                editView.setText("通过webview发消息接受到数据：\n"+data);
            }
        };
    }
    private void initWebView(){
        //辅助WebView设置处理关于页面跳转，页面请求等操作
        webView.setWebViewClient(new MyWebViewClient(webView,this));
        //Handler做为通信桥梁的作用，接收处理来自H5数据及回传Native数据的处理，当h5调用send()发送消息的时候，调用MyHandlerCallBack
        webView.setDefaultHandler(new MyHandlerCallBack(mOnSendDataListener));
        //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等比等，不过它还能处理文件上传操作
        webView.setWebChromeClient(new MyChromeWebClient());
        // 如果不加这一行，当点击界面链接，跳转到外部时，会出现net::ERR_CACHE_MISS错误
        // 需要在androidManifest.xml文件中声明联网权限
        // <uses-permission android:name="android.permission.INTERNET"/>
        if (Build.VERSION.SDK_INT>=19){
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        //有方法名的都需要注册Handler后使用
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i(TAG,"html返回数据为："+data);
                if (!TextUtils.isEmpty(data)){
                    editView.setText("通过调用Native方法接收数据：\n"+data);
                }
                function.onCallBack("Native已经接收到数据："+data+"请确认！");
            }
        });

        webView.registerHandler("functionOpen", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                TipsToast.INSTANCE.show("网页在打开你的文件预览");
            }
        });
        //应用启动后初始化数据调用，js处理方法connectWebViewJavascriptBridge(function(bridge)
        webView.callHandler("functionInJs", new Gson().toJson(new UserInfo("01", "name")), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                editView.setText("向h5发送初始化数据成功，接收h5返回值为：\n" + data);
            }
        });
        //对应js中的bridge.init处理，此处需加CallBackFunction,如果只使用mBridgeWebview.send("")；会导致js中只收到通知，接收不到值
        webView.send("来自java的发送消息！！！", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                TipsToast.INSTANCE.show("bridge.init初始化数据成功" + data);
            }
        });
    }

    @OnClick({R.id.btn_send,R.id.btn_reset})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.btn_send:
                String data=editView.getText().toString();
                webView.loadUrl("javascript:nativeFunction("+data+")");
                break;
            case R.id.btn_reset:
                webView.loadUrl("file:///android_asset/web.html");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==FILECHOOSER_RESULTCODE){
            if (null== mUploadMessage && null== mUploadCallbackAboveL){
                return ;
            }
            Uri result=data ==null || resultCode !=RESULT_OK ? null :data.getData();
            if (mUploadCallbackAboveL!=null){
                onActivityResultAbovel(requestCode,resultCode,data);
            }else if (mUploadMessage!=null){
                mUploadMessage.onReceiveValue(result);
                mUploadMessage=null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAbovel(int requestCode,int resultCode,Intent data){
        if (requestCode!=FILECHOOSER_RESULTCODE || mUploadCallbackAboveL==null){
            return ;
        }
        Uri[] results=null;
        if (resultCode== Activity.RESULT_OK){
            if (data!=null){
                String dataString=data.getDataString();
                ClipData clipData=data.getClipData();
                if (clipData!=null){
                    results=new Uri[clipData.getItemCount()];
                    for (int i=0;i<clipData.getItemCount();i++){
                        ClipData.Item item=clipData.getItemAt(i);
                        results[i]=item.getUri();
                    }
                }
                if (dataString!=null){
                    results=new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL=null;
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


    /**
     * Created by myuzh on 2019/2/21.
     * 自定义 WebChromeClient 辅助WebView处理图片上传操作【<input type=file> 文件上传标签,点击会自动调用】
     */
    public class MyChromeWebClient extends WebChromeClient {


        private String TAG ="MyChromeWebClient";
        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Logger.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            BridgeWebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Logger.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            BridgeWebViewActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Logger.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            BridgeWebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }

        // For Android 5.0+会调用此方法
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            Logger.d(TAG, "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            BridgeWebViewActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
            return true;
        }

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
