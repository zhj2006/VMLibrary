package com.vmloft.develop.library.tools.picker.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.vmloft.develop.library.tools.R;

import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.util.NavigationBarChangeListener;
import com.vmloft.develop.library.tools.utils.VMDimen;

/**
 * Create by lzan13 on 2019/05/17
 *
 * 图片预览界面
 */
public class VMPickPreviewActivity extends VMPickPreviewBaseActivity implements VMPicker.OnImageSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String ISORIGIN = "isOrigin";

    private boolean isOrigin;                      //是否选中原图
    private CheckBox mCbCheck;                //是否选中当前图片的CheckBox
    private CheckBox mCbOrigin;               //原图
    private Button mBtnOk;                         //确认图片的选择
    private View bottomBar;
    private View marginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VMPicker.getInstance().addOnImageSelectedListener(this);

        isOrigin = getIntent().getBooleanExtra(VMPickPreviewActivity.ISORIGIN, false);
        mBtnOk = findViewById(R.id.vm_common_ok_btn);
        mBtnOk.setVisibility(View.VISIBLE);
        mBtnOk.setOnClickListener(this);

        bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setVisibility(View.VISIBLE);

        mCbCheck = findViewById(R.id.vm_pick_grid_item_cb);
        mCbOrigin = findViewById(R.id.cb_origin);
        marginView = findViewById(R.id.margin_bottom);
        mCbOrigin.setText(getString(R.string.ip_origin));
        mCbOrigin.setOnCheckedChangeListener(this);
        mCbOrigin.setChecked(isOrigin);

        //初始化当前页面的状态
        onImageSelected(0, null, false);
        VMPictureBean item = mVMPictureBeans.get(mCurrentPosition);
        boolean isSelected = VMPicker.getInstance().isSelect(item);
        mTitleCount.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mVMPictureBeans
                .size()));
        mCbCheck.setChecked(isSelected);
        //滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                VMPictureBean item = mVMPictureBeans.get(mCurrentPosition);
                boolean isSelected = VMPicker.getInstance().isSelect(item);
                mCbCheck.setChecked(isSelected);
                mTitleCount.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mVMPictureBeans
                        .size()));
            }
        });
        //当点击当前选中按钮的时候，需要根据当前的选中状态添加和移除图片
        mCbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VMPictureBean VMPictureBean = mVMPictureBeans.get(mCurrentPosition);
                int selectLimit = VMPicker.getInstance().getSelectLimit();
                if (mCbCheck.isChecked() && selectedImages.size() >= selectLimit) {
                    Toast.makeText(VMPickPreviewActivity.this, getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT)
                            .show();
                    mCbCheck.setChecked(false);
                } else {
                    VMPicker.getInstance().addSelectedImageItem(mCurrentPosition, VMPictureBean, mCbCheck.isChecked());
                }
            }
        });
        NavigationBarChangeListener.with(this)
                .setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
                    @Override
                    public void onNavigationBarShow(int orientation, int height) {
                        marginView.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams layoutParams = marginView.getLayoutParams();
                        if (layoutParams.height == 0) {
                            layoutParams.height = VMDimen.getNavigationBarHeight();
                            marginView.requestLayout();
                        }
                    }

                    @Override
                    public void onNavigationBarHide(int orientation) {
                        marginView.setVisibility(View.GONE);
                    }
                });
        NavigationBarChangeListener.with(this, NavigationBarChangeListener.ORIENTATION_HORIZONTAL)
                .setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
                    @Override
                    public void onNavigationBarShow(int orientation, int height) {
                        topBar.setPadding(0, 0, height, 0);
                        bottomBar.setPadding(0, 0, height, 0);
                    }

                    @Override
                    public void onNavigationBarHide(int orientation) {
                        topBar.setPadding(0, 0, 0, 0);
                        bottomBar.setPadding(0, 0, 0, 0);
                    }
                });
    }

    /**
     * 图片添加成功后，修改当前图片的选中数量
     * 当调用 addSelectedImageItem 或 deleteSelectedImageItem 都会触发当前回调
     */
    @Override
    public void onImageSelected(int position, VMPictureBean item, boolean isAdd) {
        if (VMPicker.getInstance().getSelectImageCount() > 0) {
            mBtnOk.setText(getString(R.string.ip_select_complete, VMPicker.getInstance().getSelectImageCount(), VMPicker.getInstance().getSelectLimit()));
        } else {
            mBtnOk.setText(getString(R.string.ip_complete));
        }

        if (mCbOrigin.isChecked()) {
            long size = 0;
            for (VMPictureBean VMPictureBean : selectedImages)
                size += VMPictureBean.size;
            String fileSize = Formatter.formatFileSize(this, size);
            mCbOrigin.setText(getString(R.string.ip_origin_size, fileSize));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.vm_common_ok_btn) {
            if (VMPicker.getInstance().getSelectedImages().size() == 0) {
                mCbCheck.setChecked(true);
                VMPictureBean VMPictureBean = mVMPictureBeans.get(mCurrentPosition);
                VMPicker.getInstance().addSelectedImageItem(mCurrentPosition, VMPictureBean, mCbCheck.isChecked());
            }
            Intent intent = new Intent();
            intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, VMPicker.getInstance().getSelectedImages());
            setResult(VMPicker.RESULT_CODE_ITEMS, intent);
            finish();
        } else if (id == R.id.vm_common_back_btn) {
            Intent intent = new Intent();
            intent.putExtra(VMPickPreviewActivity.ISORIGIN, isOrigin);
            setResult(VMPicker.RESULT_CODE_BACK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(VMPickPreviewActivity.ISORIGIN, isOrigin);
        setResult(VMPicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.cb_origin) {
            if (isChecked) {
                long size = 0;
                for (VMPictureBean item : selectedImages)
                    size += item.size;
                String fileSize = Formatter.formatFileSize(this, size);
                isOrigin = true;
                mCbOrigin.setText(getString(R.string.ip_origin_size, fileSize));
            } else {
                isOrigin = false;
                mCbOrigin.setText(getString(R.string.ip_origin));
            }
        }
    }

    @Override
    protected void onDestroy() {
        VMPicker.getInstance().removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    /**
     * 单击时，隐藏头和尾
     */
    @Override
    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_top_out));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_fade_out));
            topBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
            tintManager.setStatusBarTintResource(Color.TRANSPARENT);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
            //            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_top_in));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_fade_in));
            topBar.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            tintManager.setStatusBarTintResource(R.color.vm_white);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
            //            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
