package com.vmloft.develop.library.tools.picker;

import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by lzan13 on 2019/05/16
 *
 * 新的DataHolder，使用单例和弱引用解决崩溃问题
 */
public class DataHolder {
    public static final String DH_CURRENT_IMAGE_FOLDER_ITEMS = "dh_current_image_folder_items";

    private static DataHolder mInstance;
    private Map<String, List<VMPictureBean>> data;

    public static DataHolder getInstance() {
        if (mInstance == null) {
            synchronized (DataHolder.class) {
                if (mInstance == null) {
                    mInstance = new DataHolder();
                }
            }
        }
        return mInstance;
    }

    private DataHolder() {
        data = new HashMap<>();
    }

    public void save(String id, List<VMPictureBean> object) {
        if (data != null) {
            data.put(id, object);
        }
    }

    public Object retrieve(String id) {
        if (data == null || mInstance == null) {
            throw new RuntimeException("你必须先初始化");
        }
        return data.get(id);
    }
}