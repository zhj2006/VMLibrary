package com.vmloft.develop.library.example.ui.main.home

import android.Manifest
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseFragment
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.permission.VMPermissionBean
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.toast.VMToast

import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.widget_common_top_bar.*
import java.util.ArrayList

/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：首页
 */
class HomeFragment : BaseFragment() {

    // 定义动态添加的控件 Id
    private val barrage = "barrage"
    private val customView = "customView"
    private val floatMenu = "floatMenu"
    private val indicator = "indicator"
    private val loading = "loading"
    private val lottieAnim = "lottieAnim"
    private val mediaPlay = "mediaPlay"
    private val scheme = "scheme"
    private val style = "style"
    private val thread = "thread"
    private val web = "web"

    private val singlePermission = "singlePermission"
    private val multiPermission = "multiPermission"

    override fun layoutId() = R.layout.fragment_home

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_home)
        setTopSubtitle("这个是我的工具类库入口")
        commonTopBar.setCenter(true)
        commonTopBar.setEndBtnEnable(true)
        commonTopBar.setEndBtnTextStyle(R.style.AppText_TopBarEndBtn)
        commonTopBar.setEndBtnListener("测试(1/9)", View.OnClickListener {
            VMToast.make(activity, "测试自定义 VMTopBar 右侧按钮样式")
                .show()
            VMLog.json("{'app':'VMLibrary', 'version':'1.0.0', 'tag':['tools','kotlin','android']}")
        })

        addBtn("弹幕", barrage.hashCode())
        addBtn("自定义控件", customView.hashCode())
        addBtn("悬浮菜单", floatMenu.hashCode())
        addBtn("指示器", indicator.hashCode())
        addBtn("Loading", loading.hashCode())
        addBtn("Lottie 动画", lottieAnim.hashCode())
        addBtn("声音播放", mediaPlay.hashCode())
        addBtn("Scheme", scheme.hashCode())
        addBtn("按钮", style.hashCode())
        addBtn("线程测试", thread.hashCode())
        addBtn("Web 功能", web.hashCode())

        addBtn("单权限申请", singlePermission.hashCode())
        addBtn("多权限申请", multiPermission.hashCode())

    }

    /**
     * 添加一个按钮
     */
    private fun addBtn(title: String, id: Int) {
        val btn = Button(ContextThemeWrapper(activity, R.style.VMBtn_Flat))
        btn.text = title
        btn.id = id
        btn.setOnClickListener(viewListener)
        viewGroup.addView(btn)
    }

    override fun initData() {
    }

    private val viewListener = View.OnClickListener { v: View ->
        when (v.id) {
            barrage.hashCode() -> AppRouter.go("/VMLoft/Barrage")
            customView.hashCode() -> AppRouter.go("/VMLoft/CustomView")
            floatMenu.hashCode() -> AppRouter.go("/VMLoft/FloatMenu")
            indicator.hashCode() -> AppRouter.go("/VMLoft/Indicator")
            loading.hashCode() -> AppRouter.go("/VMLoft/Loading")
            lottieAnim.hashCode() -> AppRouter.go("/VMLoft/LottieAnim")
            mediaPlay.hashCode() -> AppRouter.go("/VMLoft/MediaPlay")
            scheme.hashCode() -> AppRouter.go("/VMLoft/Scheme")
            style.hashCode() -> AppRouter.go("/VMLoft/BtnStyle")
            thread.hashCode() -> AppRouter.go("/VMLoft/Thread")
            web.hashCode() -> AppRouter.go("/VMLoft/Web")

            singlePermission.hashCode() -> checkOnePermissions()
            multiPermission.hashCode() -> checkPermissions()
        }
    }

    /**
     * 检查权限
     */
    private fun checkOnePermissions() {
        val bean = VMPermissionBean(Manifest.permission.CAMERA, "访问相机", "扫描二维码需要使用到相机，请允许我们获取拍照权限", R.mipmap.ic_launcher_round)
        VMPermission.getInstance(activity)
            .setEnableDialog(true)
            .setTitle("权限申请")
            .setMessage("为保证应用的正常运行，请授予一下权限")
            .setPermission(bean)
            .requestPermission(object : VMPermission.PCallback {
                override fun onReject() {
                    VMToast.make(activity, "权限申请被拒绝").error()
                }

                override fun onComplete() {
                    VMToast.make(activity, "权限申请完成")
                        .done()
                }
            })
    }

    /**
     * 检查权限
     */
    private fun checkPermissions() {
        val permissions: MutableList<VMPermissionBean> = ArrayList()
        permissions.add(VMPermissionBean(Manifest.permission.CAMERA, "访问相机", "拍摄图片需要使用到相机，请允许我们获取访问相机", R.mipmap.ic_launcher_round))
        permissions.add(VMPermissionBean(Manifest.permission.READ_EXTERNAL_STORAGE, "读写存储", "我们需要将文件保存到你的设备，请允许我们获取存储权限", R.mipmap.ic_launcher_round))
        permissions.add(VMPermissionBean(Manifest.permission.RECORD_AUDIO, "访问麦克风", "发送语音消息需要录制声音，请允许我们访问设备麦克风", R.mipmap.ic_launcher_round))
        VMPermission.getInstance(activity)
            .setEnableDialog(true)
            .setTitle("权限申请")
            .setMessage("为保证应用的正常运行，请您授予以下权限")
            .setPermissionList(permissions)
            .requestPermission(object : VMPermission.PCallback {
                override fun onReject() {
                    VMToast.make(activity, "权限申请被拒绝")
                        .error()
                }

                override fun onComplete() {
                    VMToast.make(activity, "权限申请完成")
                        .done()
                }
            })
    }
}