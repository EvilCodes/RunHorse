package com.yujie.hero.data.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yujie.hero.R;


/**
 * Created by lyj on 2016/6/23.
 */
public class CircleTextImageView extends ImageView {
    /** 选择图片截取方式为中心截取*/
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    /** 设置图片色彩存储方法，使之成为32位的ARGB位图*/
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private static final int COLOR_DRAWABLE_DIMENSION = 2;
    /** 设置默认的边框宽度*/
    private static final int DEF_BORDER_WIDTH = 2;
    /** 设置默认的边框颜色为黑色*/
    private static final int DEF_BORDER_COLOR  = Color.BLACK;
    /** 设置填充颜色为半透明*/
    private static final int DEF_FILL_COLOR  = Color.TRANSPARENT;
    /** 设置文字颜色默认为黑色*/
    private static final int DEF_TEXT_COLOR = Color.BLACK;
    /** 设置默认*/
    private static final int DEF_TEXT_SIZE = 22;
    /** 设置默认的文字外间距为*/
    private static final int DEF_TEXT_PADDING = 3;
    /** 设置边框默认不覆盖*/
    private static final boolean DEF_BORDER_OVERLAY = false;

    /** 画圆形背景或者是背景图片*/
    private final RectF mDrawableRectF = new RectF();
    /** 画圆形边框*/
    private final RectF mBorderRectF = new RectF();

    private final Matrix mShaderMatrix = new Matrix();

    /** 设置位图画笔*/
    private final Paint mBitMapPaint = new Paint();
    /** 设置边框画笔*/
    private final Paint mBorderPaint = new Paint();
    /** 设置背景填充色画笔*/
    private final Paint mFillPaint = new Paint();
    /** 设置绘制文字画笔*/
    private final Paint mTextPaint = new Paint();

    private int mBorderColor = DEF_BORDER_COLOR;
    private int mBorderWidth = DEF_BORDER_WIDTH;
    private int mFillColor = DEF_FILL_COLOR;

    private String mString;
    private int mTextSize = DEF_TEXT_SIZE;
    private int mTextColor = DEF_TEXT_COLOR;
    private int mTextPadding = DEF_TEXT_PADDING;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private ColorFilter mColorFilter;

    private boolean mReady;
    private boolean mSetUpPending;
    private boolean mBorderOverlay;

    public CircleTextImageView(Context context) {
        super(context);
        init();
    }

    public CircleTextImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleTextImageView, defStyleAttr, 0);

        mBorderWidth = array.getDimensionPixelSize(R.styleable.CircleTextImageView_border_width,DEF_BORDER_WIDTH);
        mBorderColor = array.getColor(R.styleable.CircleTextImageView_border_color,DEF_BORDER_COLOR);
        mBorderOverlay = array.getBoolean(R.styleable.CircleTextImageView_border_overlay,DEF_BORDER_OVERLAY);
        mFillColor = array.getColor(R.styleable.CircleTextImageView_fill_color,DEF_FILL_COLOR);
        mString = array.getString(R.styleable.CircleTextImageView_text);
        mTextColor = array.getColor(R.styleable.CircleTextImageView_text_color,DEF_TEXT_COLOR);
        mTextPadding = array.getDimensionPixelSize(R.styleable.CircleTextImageView_text_padding,DEF_TEXT_PADDING);
        mTextSize = array.getDimensionPixelSize(R.styleable.CircleTextImageView_text_size,DEF_TEXT_SIZE);
        array.recycle();
        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if(mSetUpPending){
            setUp();
            mSetUpPending = false;
        }
    }
    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null&&TextUtils.isEmpty(mString)) {
            return;
        }

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mFillPaint);
        }
        if(mBitmap!=null) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitMapPaint);
        }
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
        }

        if (!TextUtils.isEmpty(mString)) {
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            canvas.drawText(mString,
                    getWidth() / 2 - mTextPaint.measureText(mString) / 2,
                    getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2, mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }

    public String getTextString() {
        return mString;
    }

    public void setTextString(String mString) {
        this.mString = mString;
        invalidate();
    }

    public void setTExtString(@StringRes int textId){
        setTextString(getResources().getString(textId));
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(@ColorInt int mTextColor) {
        this.mTextColor = mTextColor;
        this.mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public void setTextColorResource(@ColorRes int clorId){
        setTextColor(getResources().getColor(clorId));
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        this.mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public int getTextPadding() {
        return mTextPadding;
    }

    public void setTextPadding(int mTextPadding) {
        this.mTextPadding = mTextPadding;
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(@ColorInt int mBorderColor) {
        this.mBorderColor = mBorderColor;
        this.mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public void setBorderColorResource(@ColorRes int colorId){
        setBorderColor(getResources().getColor(colorId));
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setUp();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setUp();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setUp();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setUp();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap=uri !=null ? getBitmapFromDrawable(getDrawable()):null;
        setUp();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        super.setColorFilter(cf);
        if (cf == mColorFilter){
            return;
        }
        mColorFilter = cf;
        mBitMapPaint.setColorFilter(mColorFilter);
        invalidate();
    }



    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setUp() {
        if(!mReady){
            mSetUpPending = true;
            return;
        }

        if (getWidth()==0&&getHeight()==0){
            return;
        }

        if (mBitmap==null&&mString.isEmpty()){
            invalidate();
            return;
        }

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        mBorderRectF.set(0,0,getWidth(),getHeight());
        mBorderRadius = Math.min((mBorderRectF.height()-mBorderWidth)/2.0f,(mBorderRectF.width()-mBorderWidth)/2.0f);

        mDrawableRectF.set(mBorderRectF);
        if (!mBorderOverlay){
            mDrawableRectF.inset(mBorderWidth,mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRectF.height()/2.0f,mDrawableRectF.width()/2.0f);

        if (mBitmap!=null){
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            mBitMapPaint.setAntiAlias(true);
            mBitMapPaint.setShader(mBitmapShader);
            updateShaderMatrinx();
        }
    }

    private void updateShaderMatrinx() {
        float scale;
        float dx = 0;
        float dy = 0;
        mShaderMatrix.set(null);
        if (mBitmapWidth*mDrawableRectF.height() > mDrawableRectF.width()*mBitmapHeight){
            scale = mDrawableRectF.height()/(float) mBitmapHeight;
            dx = (mDrawableRectF.width()-mBitmapWidth*scale)*0.5f;
        }else {
            scale = mDrawableRectF.width()/(float) mBitmapWidth;
            dy = (mDrawableRectF.height()-mBitmapHeight*scale)*0.5f;
        }

        mShaderMatrix.setScale(scale,scale);
        mShaderMatrix.postTranslate((int)(dx+0.5f)+mDrawableRectF.left,(int)(dy+0.5f)+mDrawableRectF.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(!TextUtils.isEmpty(mString)){
            int measureSize = (int)(mTextPaint.measureText(mString));
            measureSize+=mTextPadding*2;
            if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
                if (measureSize>getMeasuredWidth()||measureSize>getMeasuredHeight()){
                    setMeasuredDimension(measureSize,measureSize);
                }
            }
        }
    }
}
