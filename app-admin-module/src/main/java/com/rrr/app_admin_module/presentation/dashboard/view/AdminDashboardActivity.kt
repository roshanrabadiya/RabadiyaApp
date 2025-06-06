package com.rrr.app_admin_module.presentation.dashboard.view

import android.view.Menu
import android.view.MenuItem
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.common.PreferenceKey.KEY_ADMIN_ID
import com.rabadiya.base.common.PreferenceKey.KEY_ADMIN_USERNAME
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
import com.rabadiya.base.utils.launchActivity
import com.rrr.app_admin_module.R
import com.rrr.app_admin_module.databinding.ActivityAdminDashboardBinding
import com.rrr.app_admin_module.presentation.dashboard.adapters.DashboardMenuAdapter
import com.rrr.app_admin_module.presentation.dashboard.adapters.DashboardMenuModel
import com.rrr.app_admin_module.presentation.manage.view.ManageAppDataActivity
import kotlin.math.min

class AdminDashboardActivity :
    BaseActivity<ActivityAdminDashboardBinding>(ActivityAdminDashboardBinding::inflate) {
    private var dashboardMenuAdapter: DashboardMenuAdapter? = null

    override fun setContent() {
        setupUi()
    }

    private fun setupUi() {
        setActionBar()
        createDashboardMenu()
    }

    /**
     * set action bar
     * */
    private fun setActionBar() {
        setSupportActionBar(binding.viewToolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.rabadiya.base.R.drawable.ic_back)
            setHomeButtonEnabled(true)
        }
        binding.viewToolbar.toolbarTitle.text = sessionManager.getStringData(KEY_ADMIN_USERNAME)
        binding.viewToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * get data from intent
     * */
    private fun createDashboardMenu() {
        val dashboardMenuList = arrayListOf<DashboardMenuModel>()

        val dashboardIconList = resources.obtainTypedArray(R.array.dashboard_menu_icon)
        val dashboardTitleList = resources.obtainTypedArray(R.array.dashboard_menu_title)

        for (i in 0 until min(dashboardIconList.length(), dashboardTitleList.length())) {
            val iconResId = dashboardIconList.getResourceId(i, 0)
            val title = dashboardTitleList.getString(i) ?: ""

            val homeMenuItem = DashboardMenuModel(iconResId, title)
            dashboardMenuList.add(homeMenuItem)
        }

        dashboardMenuAdapter = DashboardMenuAdapter(dashboardMenuList) { position ->
            setDashboardMenuClicks(position)
        }
        binding.apply {
            rvcDashboardMenu.setHasFixedSize(true)
            rvcDashboardMenu.adapter = dashboardMenuAdapter
        }

        dashboardIconList.recycle()
        dashboardTitleList.recycle()
    }

    private fun setDashboardMenuClicks(position: Int) {
        when (position) {
            MENU_NEW_REGISTRATION -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_NEW_REGISTRATION
                )
            }

            MENU_SHABYON_SHREEO -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_SHABYON_SHREEO
                )
            }

            MENU_TRUSTEE_SHREEO -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_TRUSTEE_SHREEO
                )
            }

            MENU_DATA_SHREEO -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_DATA_SHREEO
                )
            }

            MENU_GAM_NI_YADI -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_GAM_NI_YADI
                )
            }

            MENU_MUKYA_SAMITI -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_MUKYA_SAMITI
                )
            }

            MENU_VIDHYARTHI_TRALAO -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_VIDHYARTHI_TRALAO
                )
            }

            MENU_MEETING -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_MEETING
                )
            }

            MENU_SIDDHIO -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_SIDDHIO
                )
            }

            MENU_GALLERY -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_GALLERY
                )
            }

            MENU_VYVSAY -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_VYVSAY
                )
            }

            MENU_AAMANTRAN -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_AAMANTRAN
                )
            }

            MENU_SMACHAR -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_SMACHAR
                )
            }

            MENU_JANMA_DIVAS -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_JANMA_DIVAS
                )
            }

            MENU_SAMPARK -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_SAMPARK
                )
            }

            MENU_AAPNA_SUCHANO -> launchActivity<ManageAppDataActivity> {
                putExtra(
                    EXTRA_ADMIN_MENU, MENU_AAPNA_SUCHANO
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.adminLogout -> {
                sessionManager.saveData(KEY_ADMIN_USERNAME, "")
                sessionManager.saveData(KEY_ADMIN_ID, "")
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}