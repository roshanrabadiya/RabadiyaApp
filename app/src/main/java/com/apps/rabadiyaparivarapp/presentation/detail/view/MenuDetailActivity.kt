package com.apps.rabadiyaparivarapp.presentation.detail.view

import com.apps.rabadiyaparivarapp.R
import com.apps.rabadiyaparivarapp.databinding.ActivityMenuDetailBinding
import com.apps.rabadiyaparivarapp.presentation.detail.view.fragments.SabhyaShreeoFragment
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.common.PreferenceKey.KEY_ADMIN_USERNAME
import com.rabadiya.base.utils.Constants
import com.rabadiya.base.utils.HOME_AAMANTRAN
import com.rabadiya.base.utils.HOME_AAPNA_SUCHANO
import com.rabadiya.base.utils.HOME_DATA_SHREEO
import com.rabadiya.base.utils.HOME_GALLERY
import com.rabadiya.base.utils.HOME_GAM_NI_YADI
import com.rabadiya.base.utils.HOME_JANMA_DIVAS
import com.rabadiya.base.utils.HOME_MEETING
import com.rabadiya.base.utils.HOME_MUKYA_SAMITI
import com.rabadiya.base.utils.HOME_SAMPARK
import com.rabadiya.base.utils.HOME_SHABYON_SHREEO
import com.rabadiya.base.utils.HOME_SIDDHIO
import com.rabadiya.base.utils.HOME_SMACHAR
import com.rabadiya.base.utils.HOME_TRUSTEE_SHREEO
import com.rabadiya.base.utils.HOME_VIDHYARTHI_TRALAO
import com.rabadiya.base.utils.HOME_VYVSAY

class MenuDetailActivity : BaseActivity<ActivityMenuDetailBinding>(ActivityMenuDetailBinding::inflate) {
    private var position = -1
    private var menuTitle = ""

    override fun setContent() {
        getIntentData()
        setupUi()
    }

    private fun setupUi() {
        setActionBar()
        handleFragments()
    }

    /**
     * set toolbar
     * */
    private fun setActionBar() {
        setSupportActionBar(binding.viewToolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.rabadiya.base.R.drawable.ic_back)
            setHomeButtonEnabled(true)
        }
        binding.viewToolbar.toolbarTitle.text = menuTitle
        binding.viewToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * get intent data
     * */
    private fun getIntentData() {
        intent.let {
            position = it.getIntExtra(Constants.EXTRA_POSITION, -1)
            menuTitle = it.getStringExtra(Constants.EXTRA_DATA) ?: ""
        }
    }

    /**
     * handle menu clicks open fragments
     * */
    private fun handleFragments() {
        when(position) {
            HOME_SHABYON_SHREEO -> {
                loadFragment(R.id.fragmentContainer, SabhyaShreeoFragment())
            }
            HOME_TRUSTEE_SHREEO -> {

            }
            HOME_DATA_SHREEO -> {

            }
            HOME_GAM_NI_YADI -> {

            }
            HOME_MUKYA_SAMITI -> {

            }
            HOME_VIDHYARTHI_TRALAO -> {

            }
            HOME_MEETING -> {

            }
            HOME_SIDDHIO -> {

            }
            HOME_GALLERY -> {

            }
            HOME_VYVSAY -> {

            }
            HOME_AAMANTRAN -> {

            }
            HOME_SMACHAR -> {

            }
            HOME_JANMA_DIVAS -> {

            }
            HOME_SAMPARK -> {

            }
            HOME_AAPNA_SUCHANO -> {

            } else -> {
            finish()
        }
        }
    }
}