package com.vmloft.develop.library.tools.base

/**
 * Create by lzan13 on 2019/04/25
 *
 * 工具库的常量定义
 */
object VMConstant {
    /**
     * 权限传参的 key
     */
    const val VM_KEY_PERMISSION_ENABLE_DIALOG = "vm_key_permission_enable_dialog"
    const val VM_KEY_PERMISSION_TITLE = "vm_key_permission_title"
    const val VM_KEY_PERMISSION_MSG = "vm_key_permission_msg"
    const val VM_KEY_PERMISSION_LIST = "vm_key_permission_list"

    /**
     * 图片选择器相关静态变量
     */
    // 选择器请求入口
    const val VM_PICK_REQUEST_CODE = 10000

    // 请求拍照
    const val VM_PICK_REQUEST_CODE_TAKE = 10001

    // 剪切照片
    const val VM_PICK_REQUEST_CODE_CROP = 10002

    // 预览图片
    const val VM_PICK_REQUEST_CODE_PREVIEW = 10003

    // 选择图片返回
    const val VM_PICK_RESULT_CODE_BACK = 20000

    // 选择完成返回起始页
    const val VM_PICK_RESULT_CODE_PICTURES = 20001

    // 是否选中原图
    const val VM_KEY_PICK_IS_ORIGIN = "vm_key_pick_is_origin"

    // 选择器传参
    const val VM_KEY_PICK_PICTURES = "vm_key_pick_pictures"

    //public static final String VM_KEY_PICK_TAKE_PICTURE = "vm_key_pick_take_picture";
    // 选择结果
    const val KEY_PICK_RESULT_PICTURES = "key_pick_result_pictures"

    // 当前选择位置
    const val KEY_PICK_CURRENT_SELECTED_POSITION = "key_pick_current_selected_position"

    // 是否预览全部
    const val KEY_PICK_PREVIEW_ALL = "key_pick_preview_all"
}