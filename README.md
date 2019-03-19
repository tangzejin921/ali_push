# 阿里的推送
> https://help.aliyun.com/document_detail/51056.html?spm=a2c4g.11186623.6.623.2eca7fa8edFL12

## 包引入
```gradle

implementation 'com.aliyun.ams:alicloud-android-push:3.1.4'

```


## 权限
```xml
<!--阿里移动推送相关权限-->
<!--Android 6.0版本可去除，用于选举信息（通道复用）的同步-->
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
<!--进行网络访问和网络状态监控相关的权限声明-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!--允许对sd卡进行读写操作-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<!--网络库使用，当网络操作时需要确保事务完成不被杀掉-->
<uses-permission android:name="android.permission.WAKE_LOCK" />
<!--用于读取手机硬件信息等，用于机型过滤-->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<!--选举使用，当应用有删除或者更新时需要重新选举，复用推送通道-->
<uses-permission android:name="android.permission.BROADCAST_PACKAGE_CHANGED" />
<uses-permission android:name="android.permission.BROADCAST_PACKAGE_REPLACED" />
<uses-permission android:name="android.permission.RESTART_PACKAGES" />
<!--补偿通道小米PUSH使用，不用可去除-->
<uses-permission android:name="android.permission.GET_TASKS" />
<!--补偿通道GCM使用，不使用可去除-->
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<!--允许监听启动完成事件-->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<!--允许访问震动器-->
<uses-permission android:name="android.permission.VIBRATE" />
<!-- 允许task重排序 -->
<uses-permission android:name="android.permission.REORDER_TASKS" />

<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
```


## Manifest
```xml
<!-- 消息接收监听器 （用户可自主扩展） -->
<receiver
    android:name=".AliPushMessageReceiver"
    android:exported="false"> <!-- 为保证receiver安全，建议设置不可导出，如需对其他应用开放可通过android：permission进行限制 -->
    <intent-filter>
        <action android:name="com.alibaba.push2.action.NOTIFICATION_OPENED" />
    </intent-filter>
    <intent-filter>
        <action android:name="com.alibaba.push2.action.NOTIFICATION_REMOVED" />
    </intent-filter>
    <intent-filter>
        <action android:name="com.taobao.accs.intent.action.COMMAND" />
    </intent-filter>

</receiver>

```


## 混淆

```txt
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**
```