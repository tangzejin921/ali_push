package com.tzj.ali.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class AliPushReceiver extends MessageReceiver implements CommonCallback {

    private static LruCache<String, IPush> weakReference = new LruCache<>(10);//注意：只里给了10

    public static void addPushListener(String tag, IPush push) {
        if (tag != null) {
            weakReference.put(tag, push);
        }
    }

    public static void removeListener(String str) {
        if (str != null) {
            weakReference.remove(str);
        }
    }

    public static void clear() {
        weakReference.evictAll();
    }



    @Override
    public void onSuccess(String s) {
        try {
            SharedPreferences sp = AliPushApplication.mContext.getSharedPreferences(PushInfo.class.getName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            if (s == null) {
                s = "";
            }
            edit.putString("msg", s);
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Map.Entry<String, IPush>> entries = weakReference.snapshot().entrySet();
        for (Map.Entry<String, IPush> entry : entries) {
            entry.getValue().onBind(AliPushApplication.mContext, 0, "", "", "", "");
        }
    }

    @Override
    public void onFailed(String s, String s1) {
        try {
            SharedPreferences sp = AliPushApplication.mContext.getSharedPreferences(PushInfo.class.getName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("msg", "");
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Map.Entry<String, IPush>> entries = weakReference.snapshot().entrySet();
        for (Map.Entry<String, IPush> entry : entries) {
            entry.getValue().onUnbind(AliPushApplication.mContext, 0, "");
        }
    }


    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        Log.e("push", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Set<Map.Entry<String, IPush>> entries = weakReference.snapshot().entrySet();
        for (Map.Entry<String, IPush> entry : entries) {
            entry.getValue().onMessage(context, cPushMessage.getTitle(), cPushMessage.getContent());
        }
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        try {
            SharedPreferences sp = context.getSharedPreferences(AliPushReceiver.class.getName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            if (title == null) {
                title = "";
            }
            edit.putString("title", title);
            if (summary == null) {
                summary = "";
            }
            edit.putString("description", summary);
            if (extraMap == null) {
                extraMap = "";
            }
            edit.putString("customContentString", extraMap);
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Map.Entry<String, IPush>> entries = weakReference.snapshot().entrySet();
        for (Map.Entry<String, IPush> entry : entries) {
            entry.getValue().onNotificationClicked(context, title, summary, extraMap);
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("push", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        OpenNotifiActivity.start(context);
        Set<Map.Entry<String, IPush>> entries = weakReference.snapshot().entrySet();
        String customContentString = new JSONObject(extraMap).toString();
        for (Map.Entry<String, IPush> entry : entries) {
            entry.getValue().onNotificationArrived(context, title, summary, customContentString);
        }
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("push", "onNotificationRemoved");
    }


    public static class PushInfo {
        public String msg;

        public boolean isEmpty() {
            return TextUtils.isEmpty(msg);
        }
    }

    public static class PushMsg {
        public String title;
        public String description;
        public String customContentString;

        public boolean isEmpty() {
            return TextUtils.isEmpty(title)
                    && TextUtils.isEmpty(description)
                    && TextUtils.isEmpty(customContentString);
        }
    }


}
