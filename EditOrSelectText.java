package com.oxylane.android.cubeinstore.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oxylane.android.cubeinstore.R;
import com.oxylane.android.cubeinstore.data.model.SpinnerDialogItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Maxime Jallu
 * @since 25/10/2016
 * <p>
 * Create for CubeInStore - Android (Decathlon)
 * <p>
 * Use this Class for : <br/>
 * ... {DOCUMENTATION}
 */
public class EditOrSelectText<T extends SpinnerDialogItem> extends LinearLayout {

    @BindView(R.id.image)
    ImageView mImageView;
    @BindView(R.id.input_spinner)
    TextInputLayout mInputSpinnerText;
    @BindView(R.id.input_text)
    TextInputLayout mInputEditText;

    @BindView(R.id.spinner_text)
    EditTextSpinner<T> mEditTextSpinner;
    @BindView(R.id.manual_text)
    EditText mEditText;

    //    private @DrawableRes int mDrawableRes;
    private boolean mIsSelected;
    private boolean mIsSpaceHideDrawable;
    private String mHint;
    private String mText;
    private int mType;

    public EditOrSelectText(Context context) {
        super(context);
        initView(context, null);
    }

    public EditOrSelectText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EditOrSelectText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditOrSelectText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.layout_custom_view_edit_select_text, this);
        ButterKnife.bind(this, view);

        if (attrs != null) {
            Drawable drawable;

//            int[] set = {
//                    android.R.attr.inputType, // idx 0
//                    android.R.attr.imeOptions        // idx 1
//            };

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditOrSelectText, 0, 0);
//            TypedArray b = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditOrSelectText, 0, 0);

            try {
                drawable = a.getDrawable(R.styleable.EditOrSelectText_drawableStart);
                mIsSelected = a.getBoolean(R.styleable.EditOrSelectText_manualEnterText, false);
                mIsSpaceHideDrawable = a.getBoolean(R.styleable.EditOrSelectText_spaceHideDrawable24, false);
                mHint = a.getString(R.styleable.EditOrSelectText_android_hint);
                mText = a.getString(R.styleable.EditOrSelectText_android_text);

                mType = a.getInt(R.styleable.EditOrSelectText_android_inputType, 0);

                if (drawable != null) {
                    mImageView.setImageDrawable(drawable);
                    mImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.hint_color));
                }else if (mIsSpaceHideDrawable){
                    mImageView.setImageResource(R.drawable.ic_cancel_radio_checked_24dp);
                    mImageView.setAlpha(0.0f);
                }

                mInputSpinnerText.setVisibility(mIsSelected ? GONE : VISIBLE);
                mInputEditText.setVisibility(mIsSelected ? VISIBLE : GONE);

                mInputSpinnerText.setHint(mHint);
                mInputEditText.setHint(mHint);

                mEditTextSpinner.setHighlightColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                mEditTextSpinner.setHintTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                mEditTextSpinner.setText(mText);

                mEditText.setHighlightColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                mEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                mEditText.setText(mText);
                if (mType > 0) {
                    mEditText.setInputType(mType);
                }

//                mEditTextSpinner.setEditMode(mIsSelected);
            } finally {
                a.recycle();
            }
        }
    }

}
