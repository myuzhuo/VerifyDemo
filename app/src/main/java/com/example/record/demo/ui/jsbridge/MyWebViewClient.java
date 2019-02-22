package com.example.record.demo.ui.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * Created by myuzh on 2019/2/21.
 * 自定义WebViewClient
 */
public class MyWebViewClient extends BridgeWebViewClient {

    private String TAG="MyWebViewClient";

    private Context mContext;

    public MyWebViewClient(BridgeWebView webView,Context context) {
        super(webView);
        mContext=context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.i(TAG,"url地址为："+url);
        try {
            url=URLDecoder.decode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.e(TAG,e.getMessage());
            e.printStackTrace();
        }
        //默认操作url地址yy://__QUEUE_MESSAGE__/
        if (url.trim().startsWith("yy:")){
            return super.shouldOverrideUrlLoading(view, url);
        }
        //特殊情况tel，调用系统的拨号软件拨号<a href=“tel:110”>拨打电话110<a>
        if (url.trim().startsWith("tel")){
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            mContext.startActivity(intent);
        }else{
            if (url.contains("csdn")){
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            }else{
                view.loadUrl(url);
            }
        }
        return true;
    }
}
