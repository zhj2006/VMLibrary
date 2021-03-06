package com.vmloft.develop.library.example.ui.settings

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.common.Constants
import com.vmloft.develop.library.example.common.SPManager
import com.vmloft.develop.library.example.utils.toast
import com.vmloft.develop.library.tools.utils.VMFile
import kotlinx.android.synthetic.main.activity_settings_picture.*
import java.io.File

/**
 * Create by lzan13 on 2020/05/02 22:56
 * 描述：图片资源设置
 */
@Route(path = "/VMLoft/SettingsPicture")
class PictureSettingsActivity : BaseActivity() {
    // 缓存地址
    private val cachePath = "${VMFile.cacheFromSDCard}${Constants.cacheImageDir}"

    override fun layoutId(): Int = R.layout.activity_settings_picture

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_debug)

        pictureAutoLoadLV.setOnClickListener {
            SPManager.instance.setAutoLoad(!SPManager.instance.getAutoLoad())
            pictureAutoLoadLV.isActivated = SPManager.instance.getAutoLoad()
        }

        pictureSaveDICMLV.setOnClickListener {
            SPManager.instance.setSaveDICM(!SPManager.instance.getSaveDICM())
            pictureSaveDICMLV.isActivated = SPManager.instance.getSaveDICM()
        }
        pictureClearCacheLV.setOnClickListener{
            VMFile.deleteFolder(cachePath, false)
            toast("缓存清空完成")
            bindCache()
        }

    }

    override fun initData() {
        pictureAutoLoadLV.isActivated = SPManager.instance.getAutoLoad()
        pictureSaveDICMLV.isActivated = SPManager.instance.getSaveDICM()

        bindCache()
    }

    private fun bindCache() {
        // 缓存大小
        var cacheStr = VMFile.formatSize(VMFile.getFolderSize(File(cachePath)))
        pictureClearCacheLV.setCaption(cacheStr)
    }

}