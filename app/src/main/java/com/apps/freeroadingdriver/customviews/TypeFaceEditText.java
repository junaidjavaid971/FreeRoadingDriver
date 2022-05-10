package com.apps.freeroadingdriver.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import com.apps.freeroadingdriver.R;


/**
 * Created by Admin on 7/17/2017.
 */

public class TypeFaceEditText extends AppCompatEditText {

    private static final String TAG = TypeFaceEditText.class.getName();

    private Context context;

    public TypeFaceEditText(Context context) {
        super(context);
        this.context = context;
        //init();
    }

    public TypeFaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public TypeFaceEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = null;
        String typeFace = "";
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.TypeFaceTextView, 0, 0);
            typeFace = a.getString(R.styleable.TypeFaceTextView_customFont);
        } catch (Exception e) {
            Log.d(TAG, "Unable to get typeface from attributes");
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        try {
            if (!isInEditMode()) {
                String fontPath = "fonts/OpenSans_Regular.ttf";
                if (!TextUtils.isEmpty(typeFace) && typeFace.equals(context.getString(R.string.font_opensans_light))) {
                    fontPath = "fonts/trebucit.ttf";
                }
                setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath));
            }
        } catch (Exception ex) {
            Log.d(TAG, "Unable to set typeface on textview");
        }
    }
}
