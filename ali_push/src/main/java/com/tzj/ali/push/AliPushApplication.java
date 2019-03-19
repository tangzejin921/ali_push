package com.tzj.ali.push;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;


public class AliPushApplication extends Application{
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        initCloudChannel(this);
    }

    /**
     * 初始化云推送通道
     */
    private void initCloudChannel(Context applicationContext) {
        AliPushApplication.mContext = applicationContext;
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        final AliPushReceiver aliPushReceiver = new AliPushReceiver();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                aliPushReceiver.onSuccess(s);
            }

            @Override
            public void onFailed(String s, String s1) {
                aliPushReceiver.onFailed(s,s1);
            }
        });
    }


    /**
     * 最后一次点击通知的那条消息
     * 调用方法后将被清除
     * @param ctx
     */
    public static AliPushReceiver.PushMsg getLastClickMsg(Context ctx){
        AliPushReceiver.PushMsg pushMsg = new AliPushReceiver.PushMsg();
        SharedPreferences sp = ctx.getSharedPreferences(AliPushReceiver.class.getName(), Context.MODE_PRIVATE);
        pushMsg.title = sp.getString("title", "");
        pushMsg.description = sp.getString("description", "");
        pushMsg.customContentString = sp.getString("customContentString", "");
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("title","");
        edit.putString("description","");
        edit.putString("customContentString","");
        edit.commit();
        return pushMsg;
    }

    /**
     * 百度的推送 账号
     */
    public static AliPushReceiver.PushInfo getPushInfo(Context ctx){
        AliPushReceiver.PushInfo pushInfo = new AliPushReceiver.PushInfo();
        SharedPreferences sp = ctx.getSharedPreferences(AliPushReceiver.PushInfo.class.getName(), Context.MODE_PRIVATE);
        pushInfo.msg = sp.getString("msg", "");
        return pushInfo;
    }
}
