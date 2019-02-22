package com.example.record.demo.ui.jsbridge;

import android.text.TextUtils;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.orhanobut.logger.Logger;

/**
 * Created by myuzh on 2019/2/21.
 * 自定义Handler回调
 */
public class MyHandlerCallBack implements BridgeHandler {
    private String TAG="MyHandlerCallBack";
    private OnSendDataListener mOnSendDataListener;

    public MyHandlerCallBack(OnSendDataListener onSendDataListener) {
        this.mOnSendDataListener=onSendDataListener;
    }

    @Override
    public void handler(String data, CallBackFunction function) {
        Logger.i(TAG,"接收数据为："+data);
        if (!TextUtils.isEmpty(data) && mOnSendDataListener!=null){
            mOnSendDataListener.sendData(data);
        }
        function.onCallBack("Native已收到消息!");
    }

    public interface OnSendDataListener{
        void sendData(String data);
    }
}
