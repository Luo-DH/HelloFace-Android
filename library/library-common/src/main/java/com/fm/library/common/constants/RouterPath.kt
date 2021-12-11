package com.fm.library.common.constants

/**
 * 创建日期：2021/12/11 10:26 下午
 * @author vansingluo
 * 包名： com.fm.library.common.constants
 * @desc：路由
 */
object RouterPath {

    /**
     * 登陆模块
     */
    object Login {
        private const val LOGIN = "module_login"
        const val PAGE_LOGIN = "/login/$LOGIN"
    }

    /**
     * 主页导航模块
     */
    object Main {
        private const val MAIN = "module_main"
        const val PAGE_MAIN = "/main/$MAIN"
    }

    /**
     * 管理模块
     */
    object Manager {
        private const val MANAGER = "module_manager"
        const val PAGE_MANAGER = "/manager/$MANAGER"
    }

    /**
     * 记录模块
     */
    object Record {
        private const val RECORD = "module_record"
        const val PAGE_RECORD = "/record/$RECORD"
    }

    /**
     * 设置模块
     */
    object Setting {
        private const val SETTING = "module_setting"
        const val PAGE_SETTING = "/setting/$SETTING"
    }

}