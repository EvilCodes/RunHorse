# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/yujie/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-libraryjars libs/gson-2.2.4.jar
-libraryjars libs/hellocharts-library-1.5.8.jar
-libraryjars libs/jackson-core-asl-1.9.11.jar
-libraryjars libs/jackson-mapper-asl-1.9.11.jar
-libraryjars libs/okhttp-2.7.5.jar
-libraryjars libs/okhttp-urlconnection-2.7.5.jar
-libraryjars libs/okio-1.6.0.jar
-libraryjars libs/picasso-2.4.0.jar
-keep class com.yujie.hero.utils.**{*;}