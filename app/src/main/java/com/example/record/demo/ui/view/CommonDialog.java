package com.example.record.demo.ui.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.record.demo.R;
import com.example.record.demo.fragment.SafeDialogFragment;
import com.example.record.demo.utils.DisplayUtil;


public class CommonDialog extends SafeDialogFragment {

    public static CommonDialog newInstance(String title, String content, String left, String right) {
        CommonDialog dialog = new CommonDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("left", left);
        args.putString("right", right);
        dialog.setArguments(args);
        return dialog;
    }

    TextView mCommonDialogTitle;

    TextView mCommonDialogContent;

    Button mCommonDialogLeftButton;

    Button mCommonDialogRightButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
        View view = inflater.inflate(R.layout.layout_common_dialog, container, false);
        mCommonDialogTitle = view.findViewById(R.id.common_dialog_title);
        mCommonDialogContent = view.findViewById(R.id.common_dialog_content);
        mCommonDialogLeftButton = view.findViewById(R.id.common_dialog_left_button);
        mCommonDialogRightButton = view.findViewById(R.id.common_dialog_right_button);

        // listener
        mCommonDialogLeftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.doLeftClick();
                }
                dismiss();
            }
        });

        mCommonDialogRightButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.doRightClick();
                }
                dismiss();
            }
        });

        String title = getArguments().getString("title");
        String content = getArguments().getString("content");
        String left = getArguments().getString("left");
        String right = getArguments().getString("right");


        if (!TextUtils.isEmpty(title)) {
            mCommonDialogTitle.setText(title);
            mCommonDialogTitle.setVisibility(View.VISIBLE);
        } else {
            mCommonDialogTitle.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(content)) {
            mCommonDialogContent.setVisibility(View.GONE);
        } else {
            mCommonDialogContent.setText(content);
            mCommonDialogContent.setVisibility(View.VISIBLE);
        }

        mCommonDialogLeftButton.setText(left);

        if (TextUtils.isEmpty(right)) {
            mCommonDialogRightButton.setVisibility(View.GONE);
            mCommonDialogLeftButton.setBackgroundResource(R.drawable.common_dialog_bottom_button_selector);
        } else {
            mCommonDialogRightButton.setText(right);
            mCommonDialogRightButton.setVisibility(View.VISIBLE);
        }

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        getDialog().setCanceledOnTouchOutside(false);

        return view;
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setLayout((int) DisplayUtil.dpToPx(300), ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }

    public interface Callback {
        void doLeftClick();

        void doRightClick();
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

}
