package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.vmloft.develop.library.tools.R

import com.vmloft.develop.library.tools.R.color
import com.vmloft.develop.library.tools.R.drawable
import com.vmloft.develop.library.tools.R.layout
import com.vmloft.develop.library.tools.R.style
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMTheme

/**
 * Create by lzan13 on 2019/05/28 19:33
 *
 * 自定义实现悬浮菜单，可以跟随按下位置他拿出，同时适配屏幕大小
 */
class VMFloatMenu(private val mContext: Context) : PopupWindow(mContext) {
    //    private var mContentView: View? = null
    private var mItemContainer: LinearLayout
    private var showAtVertical = 0
    private var showAtOrientation = 0

    // 菜单数量
    private var mItemCount = 0
    private var mItemPadding = 0
    private var listener: IItemClickListener? = null

    /**
     * 初始化
     */
    init {
        contentView = LayoutInflater.from(mContext).inflate(layout.vm_widget_float_menu, null)

        mItemContainer = contentView.findViewById(R.id.vmFloatMenuContainer)

        mItemPadding = VMDimen.dp2px(16)
        width = LayoutParams.WRAP_CONTENT
        height = LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        inputMethodMode = INPUT_METHOD_NOT_NEEDED
        val drawable = ContextCompat.getDrawable(mContext, color.vm_transparent)
        setBackgroundDrawable(drawable)
        VMTheme.changeShadow(mItemContainer)
    }

    /**
     * 显示悬浮菜单在指定位置，显示之前需要根据菜单的高度进行重新计算菜单位置
     */
    fun showAtLocation(view: View?, positionX: Int, positionY: Int) {
        if (mItemCount == 0) {
            return
        }
        val screenW = VMDimen.screenWidth
        val screenH = VMDimen.screenHeight
        // 计算悬浮菜单显示区域
        contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val windowsHeight = contentView.measuredHeight
        val windowsWidth = contentView.measuredWidth
        // 菜单弹出显示的最终坐标
        var x = positionX
        var y = positionY
        if (screenH - positionY < windowsHeight) {
            // 向上弹出
            y = positionY - windowsHeight
            showAtVertical = SHOW_ON_UP
        } else {
            //向下弹出
            showAtVertical = SHOW_ON_DOWN
        }
        if (screenW - positionX < windowsWidth || positionX > screenW / 5 * 3) {
            // 左弹出
            x = positionX - windowsWidth
            showAtOrientation = SHOW_ON_LEFT
        } else {
            // 右弹出
            showAtOrientation = SHOW_ON_RIGHT
        }
        setMenuAnim()
        showAtLocation(view, Gravity.NO_GRAVITY, x, y)
    }

    /**
     * 清空之前添加的 Item
     */
    fun clearAllItem() {
        mItemContainer.removeAllViews()
        mItemCount = 0
    }

    /**
     * 直接添加一个集合
     */
    fun addItemList(items: List<ItemBean>) {
        for (bean in items) {
            addItem(bean)
        }
    }

    /**
     * 添加菜单项
     */
    fun addItem(bean: ItemBean) {
        val itemView = TextView(mContext)
        itemView.id = bean.itemId
        itemView.text = bean.itemTitle
        itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
        itemView.setPadding(mItemPadding, mItemPadding, mItemPadding * 2, mItemPadding)
        itemView.setTextColor(ContextCompat.getColor(mContext, bean.itemColor))
        itemView.setBackgroundResource(drawable.vm_click_full_transparent)
        mItemContainer.addView(itemView)
        mItemCount++
        setItemClick(itemView)
    }

    /**
     * 设置悬浮菜单在触摸位置弹出动画
     */
    private fun setMenuAnim() {
        if (showAtOrientation == SHOW_ON_RIGHT && showAtVertical == SHOW_ON_UP) {
            animationStyle = style.VMFloatMenuLB
        }
        if (showAtOrientation == SHOW_ON_RIGHT && showAtVertical == SHOW_ON_DOWN) {
            animationStyle = style.VMFloatMenuLT
        }
        if (showAtOrientation == SHOW_ON_LEFT && showAtVertical == SHOW_ON_UP) {
            animationStyle = style.VMFloatMenuRB
        }
        if (showAtOrientation == SHOW_ON_LEFT && showAtVertical == SHOW_ON_DOWN) {
            animationStyle = style.VMFloatMenuRT
        }
    }

    /**
     * 设置菜单项点击事件
     */
    private fun setItemClick(view: View) {
        view.setOnClickListener { v: View ->
            dismiss()
            if (listener != null) {
                listener!!.onItemClick(v.id)
            }
        }
    }

    /**
     * 菜单项的数据 Bean 类
     */
    class ItemBean {
        var itemId: Int
        var itemTitle: String
        var itemColor: Int

        constructor(id: Int, title: String) {
            itemId = id
            itemTitle = title
            itemColor = color.vm_menu
        }

        constructor(id: Int, title: String, color: Int) {
            itemId = id
            itemTitle = title
            itemColor = color
        }
    }

    /**
     * 定义悬浮菜单点击监听接口
     */
    abstract class IItemClickListener {
        abstract fun onItemClick(id: Int)
    }

    fun setItemClickListener(listener: IItemClickListener?) {
        this.listener = listener
    }

    companion object {
        private const val SHOW_ON_LEFT = 0X11
        private const val SHOW_ON_RIGHT = 0X12
        private const val SHOW_ON_UP = 0X13
        private const val SHOW_ON_DOWN = 0X14
    }

}