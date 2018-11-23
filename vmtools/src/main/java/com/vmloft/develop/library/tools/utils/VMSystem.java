package com.vmloft.develop.library.tools.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.vmloft.develop.library.tools.VMTools;

/**
 * Create by lzan13 at 2018/11/23
 * 系统相关工具类
 */
public class VMSystem {

    /**
     * 根据系统可用处理器推荐默认线程池大小，{@link #getThreadPoolDefaultSize()} *
     */
    public static final int VM_THREAD_POOL_DEFAULT_SIZE = getThreadPoolDefaultSize();

    /**
     * 回调在UI线程中处理
     */
    private static Handler handler = new Handler(Looper.getMainLooper());

    private VMSystem() {
        throw new AssertionError();
    }

    /**
     * 在 UI 线程中执行
     */
    public static void runInUIThread(Runnable runnable) {
        runInUIThread(runnable, 0);
    }

    /**
     * 在 UI 线程中执行
     */
    public static void runInUIThread(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

    /**
     * 复制到剪贴板
     */
    public static boolean copyToClipboard(String content) {
        return copyToClipboard(VMTools.getContext(), content);
    }

    /**
     * 复制到剪贴板
     */
    public static boolean copyToClipboard(Context context, String content) {
        try {
            ClipboardManager c = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText(context.getPackageName(), content));
            return true;
        } catch (Exception e) {
            //need logger
            VMLog.e("has fail:" + e.getMessage());
        }
        return false;
    }


    /**
     * 获取线程池默认的大小
     */
    public static int getThreadPoolDefaultSize() {
        return getThreadPoolDefaultSize(8);
    }

    /**
     * 获取推荐的线程池大小
     *
     * @param max 线程池最大
     * @return 如果可用处理器*2 +1 小于最大线程数，则返回计算的线程池大小，否则返回传入的最大数
     */
    public static int getThreadPoolDefaultSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }
}
