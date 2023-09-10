package com.york1996.samplecode4varlens.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.york1996.samplecode4varlens.R;

public class CustomSettingWindowButton extends androidx.appcompat.widget.AppCompatImageButton {

    public interface SettingWindowListener {
        /**
         * 窗口是否显示
         *
         * @param showing 是否显示
         */
        void onWindowShowingStateChange(boolean showing);

        /**
         * 功能勾选状态变化
         *
         * @param functionType 功能类型
         * @param check        是否勾选
         */
        void onFunctionCheckChange(int functionType, boolean check);
    }

    /**
     * 功能类型0
     */
    public static final int FUNCTION_TYPE_0 = 0;

    /**
     * 功能类型1
     */
    public static final int FUNCTION_TYPE_1 = 1;

    /**
     * 功能类型2
     */
    public static final int FUNCTION_TYPE_2 = 2;

    private final PopupWindow mSettingWindow;
    private final View mSettingWindowLayout;

    private CheckBox mFunction0CheckBox;
    private CheckBox mFunction1CheckBox;
    private CheckBox mFunction2CheckBox;

    private SettingWindowListener mSettingWindowListener;

    public CustomSettingWindowButton(@NonNull Context context) {
        this(context, null);
    }

    public CustomSettingWindowButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSettingWindowButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.ic_launcher_background);
        mSettingWindow = new PopupWindow(context);
        mSettingWindowLayout = LayoutInflater.from(context).inflate(R.layout.layout_setting_window, null);
        mSettingWindow.setContentView(mSettingWindowLayout);
        mSettingWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mSettingWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mSettingWindow.setOutsideTouchable(true);
        mSettingWindow.setFocusable(true);
        mSettingWindow.setOnDismissListener(() -> {
            if (mSettingWindowListener != null) {
                mSettingWindowListener.onWindowShowingStateChange(false);
            }
        });

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            if (mSettingWindowListener == null) {
                return;
            }
            if (buttonView.equals(mFunction0CheckBox)) {
                mSettingWindowListener.onFunctionCheckChange(FUNCTION_TYPE_0, isChecked);
            } else if (buttonView.equals(mFunction1CheckBox)) {
                mSettingWindowListener.onFunctionCheckChange(FUNCTION_TYPE_1, isChecked);
            } else if (buttonView.equals(mFunction2CheckBox)) {
                mSettingWindowListener.onFunctionCheckChange(FUNCTION_TYPE_2, isChecked);
            }
        };
        mFunction0CheckBox = mSettingWindowLayout.findViewById(R.id.checkbox_function_0);
        mFunction0CheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
        mFunction1CheckBox = mSettingWindowLayout.findViewById(R.id.checkbox_function_1);
        mFunction1CheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
        mFunction2CheckBox = mSettingWindowLayout.findViewById(R.id.checkbox_function_2);
        mFunction2CheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    /**
     * 展示功能弹窗
     */
    public void showSettingWindow() {
        // 先测量mSettingWindowLayout的宽度
        mSettingWindowLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = mSettingWindowLayout.getMeasuredWidth();

        int[] buttonLocation = new int[2];
        getLocationOnScreen(buttonLocation);
        int popupX = buttonLocation[0] - popupWidth;
        if (currentLocationInLeft(buttonLocation)) {
            popupX = buttonLocation[0] + getWidth();
        }
        int popupY = buttonLocation[1];

        mSettingWindow.showAtLocation(this, Gravity.NO_GRAVITY, popupX, popupY);
        if (mSettingWindowListener != null) {
            mSettingWindowListener.onWindowShowingStateChange(true);
        }
    }

    /**
     * 隐藏功能弹窗
     */
    public void hideSettingWindow() {
        mSettingWindow.dismiss();
    }

    /**
     * 设置功能弹窗回调
     *
     * @param listener 监听器
     */
    public void setSettingWindowListener(SettingWindowListener listener) {
        mSettingWindowListener = listener;
    }

    /**
     * 设置是否勾选
     *
     * @param functionType 功能类型
     * @param check        是否勾选
     */
    public void setFunctionCheck(int functionType, boolean check) {
        switch (functionType) {
            case FUNCTION_TYPE_0:
                mFunction0CheckBox.setChecked(check);
                break;
            case FUNCTION_TYPE_1:
                mFunction1CheckBox.setChecked(check);
                break;
            case FUNCTION_TYPE_2:
                mFunction2CheckBox.setChecked(check);
                ;
                break;
        }
    }

    /**
     * 对某功能进行隐藏
     *
     * @param functionType 功能类型
     */
    public void disableFunctionShowing(int functionType) {
        switch (functionType) {
            case FUNCTION_TYPE_0:
                mFunction0CheckBox.setVisibility(View.GONE);
                break;
            case FUNCTION_TYPE_1:
                mFunction1CheckBox.setVisibility(View.GONE);
                break;
            case FUNCTION_TYPE_2:
                mFunction2CheckBox.setVisibility(View.GONE);
                break;
        }
    }

    public void setButtonState(int stateType) {
        // TODO: 按需求设置状态并设置button的资源
    }

    private boolean currentLocationInLeft(int[] location) {
        int viewX = location[0];

        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        return viewX < screenWidth / 2;
    }
}
