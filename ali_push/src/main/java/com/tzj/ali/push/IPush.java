package com.tzj.ali.push;

import android.content.Context;


import java.util.List;

/*

 */
public interface IPush {
    void onBind(Context context, int errorCode, String appId, String userId, String channelId, String requestId);
    void onUnbind(Context context, int errorCode, String requestId);
    void onSetTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId);
    void onDelTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId);
    void onListTags(Context context, int errorCode, List<String> tags, String requestId);
    void onMessage(Context context, String message, String customContentString);
    void onNotificationArrived(Context context, String title, String description, String customContentString);
    void onNotificationClicked(Context context, String title, String description, String customContentString) ;
}
