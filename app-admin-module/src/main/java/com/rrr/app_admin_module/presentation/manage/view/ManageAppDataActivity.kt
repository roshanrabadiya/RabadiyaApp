package com.rrr.app_admin_module.presentation.manage.view

import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.utils.Constants.EXTRA_ADMIN_MENU
import com.rabadiya.base.utils.MENU_AAMANTRAN
import com.rabadiya.base.utils.MENU_AAPNA_SUCHANO
import com.rabadiya.base.utils.MENU_DATA_SHREEO
import com.rabadiya.base.utils.MENU_GALLERY
import com.rabadiya.base.utils.MENU_GAM_NI_YADI
import com.rabadiya.base.utils.MENU_JANMA_DIVAS
import com.rabadiya.base.utils.MENU_MEETING
import com.rabadiya.base.utils.MENU_MUKYA_SAMITI
import com.rabadiya.base.utils.MENU_NEW_REGISTRATION
import com.rabadiya.base.utils.MENU_SAMPARK
import com.rabadiya.base.utils.MENU_SHABYON_SHREEO
import com.rabadiya.base.utils.MENU_SIDDHIO
import com.rabadiya.base.utils.MENU_SMACHAR
import com.rabadiya.base.utils.MENU_TRUSTEE_SHREEO
import com.rabadiya.base.utils.MENU_VIDHYARTHI_TRALAO
import com.rabadiya.base.utils.MENU_VYVSAY
import com.rrr.app_admin_module.R
import com.rrr.app_admin_module.databinding.ActivityManageAppDataBinding
import com.rrr.app_admin_module.presentation.manage.fragment.NewApplicationFragment

class ManageAppDataActivity :
    BaseActivity<ActivityManageAppDataBinding>(ActivityManageAppDataBinding::inflate) {
    private var selectedMenu: Int = -1

    override fun setContent() {
        setupUI()
    }

    private fun setupUI() {
        getIntentData()
        setActionBar()
        showFragment()
    }

    private fun showFragment() {
        when (selectedMenu) {
            MENU_NEW_REGISTRATION -> {
                loadFragment(R.id.fragmentContainer, NewApplicationFragment())
            }
        }
    }

    private fun getIntentData() {
        selectedMenu = intent.getIntExtra(EXTRA_ADMIN_MENU, -1)
    }

    private fun setActionBar() {
        setSupportActionBar(binding.viewToolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.rabadiya.base.R.drawable.ic_back)
            setHomeButtonEnabled(true)
        }
        binding.viewToolbar.toolbarTitle.text = toolbarTitle()
        binding.viewToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun toolbarTitle(): String {
        return when (selectedMenu) {
            MENU_NEW_REGISTRATION -> getStrings(com.rabadiya.base.R.string.res_new_registration)
            MENU_SHABYON_SHREEO -> getStrings(com.rabadiya.base.R.string.res_menu_1)
            MENU_TRUSTEE_SHREEO -> getStrings(com.rabadiya.base.R.string.res_menu_2)
            MENU_DATA_SHREEO -> getStrings(com.rabadiya.base.R.string.res_menu_3)
            MENU_GAM_NI_YADI -> getStrings(com.rabadiya.base.R.string.res_menu_4)
            MENU_MUKYA_SAMITI -> getStrings(com.rabadiya.base.R.string.res_menu_5)
            MENU_VIDHYARTHI_TRALAO -> getStrings(com.rabadiya.base.R.string.res_menu_6)
            MENU_MEETING -> getStrings(com.rabadiya.base.R.string.res_menu_7)
            MENU_SIDDHIO -> getStrings(com.rabadiya.base.R.string.res_menu_8)
            MENU_GALLERY -> getStrings(com.rabadiya.base.R.string.res_menu_9)
            MENU_VYVSAY -> getStrings(com.rabadiya.base.R.string.res_menu_10)
            MENU_AAMANTRAN -> getStrings(com.rabadiya.base.R.string.res_menu_11)
            MENU_SMACHAR -> getStrings(com.rabadiya.base.R.string.res_menu_12)
            MENU_JANMA_DIVAS -> getStrings(com.rabadiya.base.R.string.res_menu_13)
            MENU_SAMPARK -> getStrings(com.rabadiya.base.R.string.res_menu_14)
            MENU_AAPNA_SUCHANO -> getStrings(com.rabadiya.base.R.string.res_menu_15)
            else -> ""
        }
    }
}