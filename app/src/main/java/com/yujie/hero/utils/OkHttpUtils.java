package com.yujie.hero.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by yao on 2016/8/16.
 */
public class OkHttpUtils<T> {
    public static final String TAG = OkHttpUtils.class.getSimpleName();
    private static String UTF_8 = "utf-8";
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_ERROR = 1;

    private static OkHttpClient mOkHttpClient;
    private Handler mHandler;

    /**
     * 存放post请求的实体，实体中存放File类型的文件
     */
    RequestBody mFileBody;
    FormEncodingBuilder mFormEncodingBuilder;

    public interface OnCompleteListener<T> {
        void onSuccess(T result);

        void onError(String error);
    }

    private OnCompleteListener<T> mListener;

    public OkHttpUtils() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RESULT_ERROR:
                        mListener.onError(msg.obj.toString());
                        break;
                    case RESULT_SUCCESS:
                        T result = (T) msg.obj;
                        mListener.onSuccess(result);
                        break;
                }
            }
        };
    }

    /**
     * 用post请求，添加一个文件
     *
     * @param file
     * @return
     */
    public OkHttpUtils<T> addFile(File file) {
        mFileBody = RequestBody.create(null, file);
        return this;
    }

    public OkHttpUtils<T> post() {
        mFormEncodingBuilder = new FormEncodingBuilder();

        return this;
    }

    StringBuilder mUrl;

    public OkHttpUtils<T> url(String url) {
        mUrl = new StringBuilder(url);
        return this;
    }

    /**
     * 用于json解析的类对象
     */
    Class<T> mClazz;
    boolean downloadBitmap = false;
    public OkHttpUtils<T> downloadFile(){
        downloadBitmap = true;
        return this;
    }
    /**
     * 设置json解析的类对象
     *
     * @param clazz
     * @return
     */
    public OkHttpUtils<T> targetClass(Class<T> clazz) {
        mClazz = clazz;
        return this;
    }

    public OkHttpUtils<T> addParam(String key, String value) {
        try {
            if (mFormEncodingBuilder != null) {//post请求的参数添加方式
                mFormEncodingBuilder.add(key, URLEncoder.encode(value, UTF_8));
            } else {//get请求的参数添加方式
                if (mUrl.indexOf("?") == -1) {
                    mUrl.append("?");
                } else {
                    mUrl.append("&");
                }
                mUrl.append(key).append("=").append(URLEncoder.encode(value, UTF_8));
            }
            Log.e("OkHttpUtils", "mUrl=" + mUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public StringBuilder getmUrl() {
        return mUrl;
    }

    public void execute(OnCompleteListener<T> listener) {
        Log.e(TAG, "mUrl=" + mUrl.toString());
        if (listener != null) {
            mListener = listener;
        }
        Request.Builder builder = new Request.Builder().url(mUrl.toString());
        if (mFormEncodingBuilder != null) {
            RequestBody body = mFormEncodingBuilder.build();
            builder.post(body);
        }

        if (mFileBody != null) {
            builder.post(mFileBody);
        }
        //创建请求
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        if (mCallback != null) {
            call.enqueue(mCallback);
            return;
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Message msg = Message.obtain();
                msg.what = RESULT_ERROR;
                msg.obj = request.body()+e.getMessage();
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (downloadBitmap){
                    InputStream stream = response.body().byteStream();
                    Bitmap value = BitmapFactory.decodeStream(stream);
                    Message msg = Message.obtain();
                    msg.what = RESULT_SUCCESS;
                    msg.obj = value;
                    mHandler.sendMessage(msg);
                }else {
                    String json = response.body().string();
                    ObjectMapper om = new ObjectMapper();
                    T value = om.readValue(json, mClazz);
                    Message msg = Message.obtain();
                    msg.what = RESULT_SUCCESS;
                    msg.obj = value;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    Callback mCallback;
    /**
     * 在工作线程中执行一段代码
     * @param callback
     * @return
     */
    public OkHttpUtils<T> doInBackground(Callback callback) {
        mCallback=callback;
        return this;
    }

    /**
     * 在主线程中执行一段程序员写的代码
     * @param listener
     * @return
     */
    public OkHttpUtils<T> onPostExecute(OnCompleteListener<T> listener) {
        mListener=listener;
        return this;
    }

    /**
     * 工作线程向主线程发送消息
     * @param msg
     */
    public void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }
}
