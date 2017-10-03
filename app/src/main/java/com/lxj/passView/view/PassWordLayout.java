package com.lxj.passView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.lxj.passView.R;
import com.lxj.passView.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 用于数字输入View
 * Created by lxj on 2017-03-06.
 */
public class PassWordLayout extends LinearLayout {

    private int maxLength = 6; //密码长度

    private int inputIndex = 0; //设置子View状态index

    private List<String> mPassList;//储存密码

    private pwdChangeListener pwdChangeListener;//密码状态改变监听


    private Context mContext;

    private boolean mIsShowInputLine;


    public void setPwdChangeListener(pwdChangeListener pwdChangeListener) {
        this.pwdChangeListener = pwdChangeListener;
    }

    public PassWordLayout(Context context) {
        this(context, null);
    }

    public PassWordLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassWordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化View
     */
    private void initView(Context context, AttributeSet attrs) {

        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PassWordLayoutStyle);

        int inputColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_box_input_color, R.color.pass_view_rect_input);
        int noinputColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_box_no_input_color, R.color.color_common_gray);
        int lineColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_input_line_color, R.color.pass_view_rect_input);
        int txtInputColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_text_input_color, R.color.pass_view_rect_input);
        int drawType = ta.getInt(R.styleable.PassWordLayoutStyle_box_draw_type, 0);
        int interval = ta.getInt(R.styleable.PassWordLayoutStyle_interval_width, 4);
        maxLength = ta.getInt(R.styleable.PassWordLayoutStyle_pass_leng, 6);
        int itemWidth = ta.getInt(R.styleable.PassWordLayoutStyle_item_width, 40);
        int itemHeight = ta.getInt(R.styleable.PassWordLayoutStyle_item_height, 40);
        int showPassType = ta.getInt(R.styleable.PassWordLayoutStyle_pass_inputed_type, 0);
        int txtSize = ta.getInt(R.styleable.PassWordLayoutStyle_draw_txt_size, 18);
        int boxLineSize = ta.getInt(R.styleable.PassWordLayoutStyle_draw_box_line_size, 4);
        mIsShowInputLine = ta.getBoolean(R.styleable.PassWordLayoutStyle_is_show_input_line, true);
        ta.recycle();

        mPassList = new ArrayList<>();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        for (int i = 0; i < maxLength; i++) {
            PassWordView passWordView = new PassWordView(context);
            LayoutParams params = new LayoutParams(DensityUtil.dip2px(mContext, itemWidth), DensityUtil.dip2px(mContext, itemHeight));
            if (i != 0) {                                       //第一个子View不添加边距
                params.leftMargin = DensityUtil.dip2px(context, DensityUtil.dip2px(mContext, interval));
            }

            passWordView.setInputStateColor(inputColor);
            passWordView.setNoinputColor(noinputColor);
            passWordView.setInputStateTextColor(txtInputColor);
            passWordView.setRemindLineColor(lineColor);
            passWordView.setmBoxDrawType(drawType);
            passWordView.setmShowPassType(showPassType);
            passWordView.setmDrawTxtSize(txtSize);
            passWordView.setmDrawBoxLineSize(boxLineSize);
            passWordView.setmIsShowRemindLine(mIsShowInputLine);

            addView(passWordView, params);
        }


        //设置点击时弹出输入法
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(PassWordLayout.this, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        this.setOnKeyListener(new MyKeyListener());//按键监听

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    PassWordView passWordView = (PassWordView) getChildAt(inputIndex);
                    if (passWordView != null) {
                        passWordView.setmIsShowRemindLine(mIsShowInputLine);
                        passWordView.startInputState();
                    }
                } else {
                    PassWordView passWordView = (PassWordView) getChildAt(inputIndex);
                    if (passWordView != null) {
                        passWordView.setmIsShowRemindLine(false);
                        passWordView.updateInputState(false);
                    }
                }
            }
        });
    }


    /**
     * 添加密码
     *
     * @param pwd
     */
    public void addPwd(String pwd) {
        if (mPassList != null && mPassList.size() < maxLength) {
            mPassList.add(pwd + "");
            setNextInput(pwd);
        }

        if (pwdChangeListener != null) {
            if (mPassList.size() < maxLength) {
                pwdChangeListener.onChange(getPassString());
            } else {
                pwdChangeListener.onFinished(getPassString());
            }
        }
    }

    /**
     * 删除密码
     */
    public void removePwd() {
        if (mPassList != null && mPassList.size() > 0) {
            mPassList.remove(mPassList.size() - 1);
            setPreviosInput();
        }

        if (pwdChangeListener != null) {
            if (mPassList.size() > 0) {
                pwdChangeListener.onChange(getPassString());
            } else {
                pwdChangeListener.onNull();
            }
        }
    }

    /**
     * 清空所有密码
     */
    public void removeAllPwd() {
        if (mPassList != null) {
            for (int i = mPassList.size(); i >= 0; i--) {
                if (i > 0) {
                    setNoInput(i, false, "");
                } else if (i == 0) {
                    PassWordView passWordView = (PassWordView) getChildAt(i);
                    if (passWordView != null) {
                        passWordView.setmPassText("");
                        passWordView.startInputState();
                    }
                }

            }

            mPassList.clear();
            inputIndex = 0;
        }


        if (pwdChangeListener != null) {
            pwdChangeListener.onNull();
        }
    }

    /**
     * 获取密码
     *
     * @return pwd
     */
    public String getPassString() {

        StringBuffer passString = new StringBuffer();

        for (String i : mPassList) {
            passString.append(i);
        }

        return passString.toString();
    }

    /**
     * 设置下一个View为输入状态
     */
    private void setNextInput(String pwdTxt) {
        if (inputIndex < maxLength) {
            setNoInput(inputIndex, true, pwdTxt);
            inputIndex++;
            PassWordView passWordView = (PassWordView) getChildAt(inputIndex);
            if (passWordView != null) {
                passWordView.setmPassText(pwdTxt + "");
                passWordView.startInputState();
            }
        }

    }

    /**
     * 设置上一个View为输入状态
     */
    private void setPreviosInput() {
        if (inputIndex > 0) {
            setNoInput(inputIndex, false, "");
            inputIndex--;
            PassWordView passWordView = (PassWordView) getChildAt(inputIndex);
            if (passWordView != null) {
                passWordView.setmPassText("");
                passWordView.startInputState();
            }
        } else if (inputIndex == 0) {
            PassWordView passWordView = (PassWordView) getChildAt(inputIndex);
            if (passWordView != null) {
                passWordView.setmPassText("");
                passWordView.startInputState();
            }
        }
    }

    /**
     * 设置指定View为不输入状态
     *
     * @param index   view下标
     * @param isinput 是否输入过密码
     */
    public void setNoInput(int index, boolean isinput, String txt) {
        if (index < 0) {
            return;
        }
        PassWordView passWordView = (PassWordView) getChildAt(index);
        if (passWordView != null) {
            passWordView.setmPassText(txt);
            passWordView.updateInputState(isinput);
        }
    }


    public interface pwdChangeListener {
        void onChange(String pwd);//密码改变

        void onNull();  //密码删除为空

        void onFinished(String pwd);//密码长度已经达到最大值
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;          //显示数字键盘
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        return new ZanyInputConnection(this, false);
    }


    private class ZanyInputConnection extends BaseInputConnection {

        @Override
        public boolean commitText(CharSequence txt, int newCursorPosition) {
            return super.commitText(txt, newCursorPosition);
        }

        public ZanyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }


    /**
     * 按键监听器
     */
    class MyKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.isShiftPressed()) {//处理*#等键
                    return false;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {//处理数字
                    addPwd(keyCode - 7 + "");              //点击添加密码
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DEL) {       //点击删除
                    removePwd();
                    return true;
                }

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            return false;
        }//onKey
    }


    //恢复状态
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mPassList = savedState.saveString;
        inputIndex = mPassList.size();
        if (mPassList.isEmpty()) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            PassWordView passWordView = (PassWordView) getChildAt(i);
            if (i > mPassList.size() - 1) {
                if (passWordView != null) {
                    passWordView.setmIsShowRemindLine(false);
                    passWordView.updateInputState(false);
                }
                break;
            }

            if (passWordView != null) {
                passWordView.setmPassText(mPassList.get(i));
                passWordView.updateInputState(true);
            }
        }

    }

    //保存状态
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.saveString = this.mPassList;
        return savedState;
    }


    public static class SavedState extends BaseSavedState {
        public List<String> saveString;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            dest.writeList(saveString);
        }

        private SavedState(Parcel in) {
            super(in);
            in.readStringList(saveString);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


}
