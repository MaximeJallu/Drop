package com.oxylane.android.cubeinstore.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.oxylane.android.cubeinstore.R;
import com.oxylane.android.cubeinstore.data.model.SpinnerDialogItem;
import com.oxylane.android.cubeinstore.data.model.SpinnerItemComparableValue;
import com.oxylane.android.cubeinstore.data.validator.string.NotEmptyStringValidator;
import com.oxylane.android.cubeinstore.ui.adapter.SpinnerElementAdapter;
import com.oxylane.android.cubeinstore.ui.fragment.dialog.SearchDialogFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 31/08/2016.
 */
public class EditTextSpinner<T extends SpinnerDialogItem> extends AppCompatEditText {

    //region Property
    private T mSelectedItem;
    private String mDefaultValue;
    private String mTitleHintFilter = null;
    private SpinnerElementAdapter<T> mAdapter;
    private WeakReference<FragmentActivity> mReferenceActivity;
    private WeakReference<EditTextSpinner<T>> mChildLinkSpinner;
    private SearchDialogFragment.OnSelectedItemCallback<T> mSelectedItemCallback;
    //endregion

    public EditTextSpinner(Context context) {
        super(context);
        initView();
    }

    public EditTextSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EditTextSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * Definit notre callback afin de récupérer l'item selectionné de la Dialog
     */
    final SearchDialogFragment.OnSelectedItemCallback<T> mItemCallback = new SearchDialogFragment.OnSelectedItemCallback<T>() {
        @Override
        public void onSelectedItem(T item, int position) {
            if (!item.equals(mSelectedItem)){
                clearChildLink();
                mSelectedItem = item;
                setText(item.getTitle());
            }
            if (mSelectedItemCallback != null){
                mSelectedItemCallback.onSelectedItem(item, position);
            }
        }
    };

    /**
     * Initialise le minima de la Vue avec le Drawable style Spinner
     */
    private void initView() {
        Drawable d = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_down_black_24dp));
        DrawableCompat.setTint(d.mutate(), ContextCompat.getColor(getContext(), R.color.primaryColor));//ColorUtils.getPrimaryColor(getContext()));
        this.setCompoundDrawablesWithIntrinsicBounds(
                getCompoundDrawables()[0],
                getCompoundDrawables()[1],
                d,
                getCompoundDrawables()[3]);
        this.setKeyListener(null);
        this.setFocusable(false);
    }

    /**
     * Initialise le listener qui va ouvrir la Dialog
     */
    private void initDialogTouchListener() {
        this.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SearchDialogFragment<T> dialogFragment = SearchDialogFragment.newInstance(
                        mTitleHintFilter,
                        mAdapter.getList(),
                        mSelectedItem);
                dialogFragment.addListener(mItemCallback);
                dialogFragment.show(
                        mReferenceActivity.get().getSupportFragmentManager(),
                        "SpinnerMaterialDialog");
            }
            /** On return toujours TRUE pour disable l'action d'OPEN SpinnerValue*/
            return true;
        });
    }

    /**
     * Definit les données du Spinner
     *
     * @param activity Context FragmentActivity used openDialogFragment
     * @param items    data list
     */
    public void setAdapter(@NonNull FragmentActivity activity, List<T> items) {
        setAdapter(activity, items, null);
    }

    /**
     * Definit les données du Spinner
     *
     * @param activity        Context FragmentActivity used openDialogFragment
     * @param items           data list
     * @param titleHintFilter active la fonction recherche avec un titre donnée si la liste est supérieur à 10 items
     */
    public void setAdapter(@NonNull FragmentActivity activity, List<T> items, String titleHintFilter) {
        mAdapter = new SpinnerElementAdapter<>(activity, new ArrayList<>(items));
        mReferenceActivity = new WeakReference<>(activity);
        mTitleHintFilter = items.size() > 10 ? titleHintFilter : null;
        initDialogTouchListener();
        /** Si une valeur par defaut est initialise on tente de la retrouver*/
        if (new NotEmptyStringValidator(mDefaultValue).isValid()){
            for (T item : items) {
                if ((item instanceof SpinnerItemComparableValue && ((SpinnerItemComparableValue) item).isMatchValue(mDefaultValue))
                        || mDefaultValue.equals(item.getTitle())){
                    setMatchingValueInAdapter(item, items.indexOf(item));
                    break;
                }
            }
        }
        setEnabled(items.size() > 0);
    }

    private void setMatchingValueInAdapter(T item, int position){
        setValue(item);
        if(mSelectedItemCallback != null){
            mSelectedItemCallback.onSelectedItem(item, position);
        }
    }

    /**
     * Optient la liste des items du Spinner ou une liste vide.
     *
     * @return item arrays ou new ArrayList<>()
     */
    @NonNull
    public List<T> getSpinnerDialogItems() {
        return (mAdapter != null && mAdapter.getList() != null) ? mAdapter.getList() : new ArrayList<>();
    }

    /**
     * Optient l'objet courant
     *
     * @return item selectionné
     */
    public T getSelectedItem() {
        return mSelectedItem;
    }

    /**
     * Definie l'objet courant
     *
     * @param itemValue item
     */
    public void setValue(T itemValue) {
        mSelectedItem = itemValue;
        if (mSelectedItem != null) {
            setText(mSelectedItem.getTitle());
        }
    }

    public void setSelectedItemCallback(SearchDialogFragment.OnSelectedItemCallback<T> selectedItemCallback) {
        mSelectedItemCallback = selectedItemCallback;
    }

    public void clearSelectedValue(){
        mSelectedItem = null;
        setText("");
        clearChildLink();
    }

    private void clearChildLink(){
        if (mChildLinkSpinner != null && mChildLinkSpinner.get() != null){
            mChildLinkSpinner.get().clearSelectedValue();
        }
    }

    /**
     * Create a link with another Spinner, when current view changed clear link value her
     * @param provinceSpinner
     */
    public void setChildLinkSpinner(EditTextSpinner<T> provinceSpinner) {
        mChildLinkSpinner = new WeakReference<>(provinceSpinner);
    }

    public EditTextSpinner<T> getChildLinkSpinner() {
        return mChildLinkSpinner != null ? mChildLinkSpinner.get() : null;
    }

    /**
     * Initialise la valeur par default du Spinner
     * After set adapter, try to find this item value in items list for select
     * @param defaultValue String value
     */
    public void setDefaultValue(String defaultValue) {
        mDefaultValue = defaultValue;
    }
}
