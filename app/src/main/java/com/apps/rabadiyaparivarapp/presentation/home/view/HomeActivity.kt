package com.apps.rabadiyaparivarapp.presentation.home.view

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.apps.rabadiyaparivarapp.R
import com.apps.rabadiyaparivarapp.databinding.ActivityHomeBinding
import com.apps.rabadiyaparivarapp.presentation.detail.view.MenuDetailActivity
import com.apps.rabadiyaparivarapp.presentation.home.adapter.DrawerAdapter
import com.apps.rabadiyaparivarapp.presentation.home.adapter.HomeMenuAdapter
import com.apps.rabadiyaparivarapp.presentation.home.adapter.SlideAdapter
import com.apps.rabadiyaparivarapp.presentation.new_application.view.AddNewApplicationActivity
import com.apps.rabadiyaparivarapp.presentation.register.RegisterNewMemberActivity
import com.apps.rabadiyaparivarapp.presentation.home.viewmodel.HomeViewModel
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.common.PreferenceKey.KEY_ADMIN_ID
import com.rabadiya.base.common.PreferenceKey.KEY_ADMIN_USERNAME
import com.rabadiya.base.common.Resource
import com.rabadiya.base.customviews.customtextview.CommonTextView
import com.rabadiya.base.model.home.MenuData
import com.rabadiya.base.model.home.NavItem
import com.rabadiya.base.model.home.SliderItem
import com.rabadiya.base.utils.Constants
import com.rabadiya.base.utils.Constants.EXTRA_DATA
import com.rabadiya.base.utils.Constants.EXTRA_POSITION
import com.rabadiya.base.utils.HOME_SHABYON_SHREEO
import com.rabadiya.base.utils.androidDeviceId
import com.rabadiya.base.utils.getAndroidDeviceId
import com.rabadiya.base.utils.launchActivity
import com.rabadiya.base.utils.mainScope
import com.rabadiya.base.utils.showToast
import com.rrr.app_admin_module.presentation.dashboard.view.AdminDashboardActivity
import com.rrr.app_admin_module.presentation.login.view.AdminLoginActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs
import kotlin.math.min


class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private lateinit var toolbar: Toolbar

    private val homeViewModel: HomeViewModel by viewModel()

    private val bannerSlider: ViewPager2 by lazy {
        binding.bannerSlider
    }

    private var homeMenuAdapter: HomeMenuAdapter? = null

    private var sliderAdapter: SlideAdapter? = null
    private val sliderHandler = Handler(Looper.getMainLooper())


    private val toggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            0
        )
    }

    override fun setContent() {
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setActionBar()
        setNavBar()
        setHomeSlider()
        createMainMenu()
    }

    /**
     * set action bar
     * */
    private fun setActionBar() {
        val viewStub: ViewStub = findViewById(R.id.view_toolbar)
        val inflatedView: View = viewStub.inflate()
        toolbar = inflatedView.findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarTitle: CommonTextView =
            inflatedView.findViewById(com.rabadiya.base.R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.app_title)
    }

    /**
     * set navigation bar
     * */
    private fun setNavBar() {
        toggle.isDrawerIndicatorEnabled = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_burger)
        toggle.syncState()
        setNavBarAdapter()

        toggle.setToolbarNavigationClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    /**
     * create a navbar menu
     * */
    private fun setNavBarAdapter() {
        val navItemIconList = resources.obtainTypedArray(R.array.nav_menu_icon)
        val navItemTitleList = resources.obtainTypedArray(R.array.nav_menu_title)

        val navItemList = mutableListOf<NavItem>()
        for (i in 0 until min(navItemIconList.length(), navItemTitleList.length())) {
            val iconResId = navItemIconList.getResourceId(i, 0)
            val title = navItemTitleList.getString(i) ?: ""

            val navItem = NavItem(iconResId, title)
            navItemList.add(navItem)
        }

        val drawerAdapter = DrawerAdapter(navItemList) { menuPosition, _ ->
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            setNavMenuClickEvent(menuPosition)
        }
        binding.rvNavBar.setHasFixedSize(true)
        binding.rvNavBar.layoutManager = LinearLayoutManager(this)
        binding.rvNavBar.adapter = drawerAdapter

        navItemIconList.recycle()
        navItemTitleList.recycle()
    }

    private fun setNavMenuClickEvent(menuPosition: Int) {
        when (menuPosition) {
            MENU_NEW_REGISTRATION -> {
                launchActivity<AddNewApplicationActivity>()
            }

            MENU_ADVERTISE_REGISTRATION -> {

            }

            MENU_SETTINGS -> {

            }

            MENU_ABOUT_APP -> {

            }
        }
    }

    /**
     * create a home sliders
     * */
    private fun setHomeSlider() {
        val sliderList = arrayListOf<SliderItem>()
        for (i in 1..5) {
            sliderList.add(SliderItem(R.drawable.boold_certi_banner))
        }

        bannerSlider.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            sliderAdapter = SlideAdapter(this@HomeActivity, this).apply {
                setSliderData(sliderList)
            }

            adapter = sliderAdapter

            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }

            setPageTransformer(compositePageTransformer)

            val dotLayout = binding.dotContainer
            dotLayout.orientation = LinearLayout.HORIZONTAL
            dotLayout.gravity = Gravity.CENTER_HORIZONTAL
            createDots(dotLayout, adapter?.itemCount ?: 0)

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 5000)
                    updateDots(dotLayout, position)
                }
            })
        }
    }

    /**
     * create a slider dots
     * */
    private fun createDots(dotLayout: LinearLayout, count: Int) {
        dotLayout.removeAllViews()
        (0 until count).forEach { i ->
            val dot = ImageView(this@HomeActivity)
            dot.setImageResource(R.drawable.slider_dot_inactive)
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0)
            dotLayout.addView(dot, layoutParams)
        }
        if (count > 0) {
            (dotLayout.getChildAt(0) as ImageView).setImageResource(R.drawable.slider_dot_active)
        }
    }

    /**
     * update a slider dots
     * */
    private fun updateDots(dotLayout: LinearLayout, position: Int) {
        val dotCount = dotLayout.childCount
        for (i in 0 until dotCount) {
            val dot = dotLayout.getChildAt(i) as ImageView
            if (i == (position % dotCount)) {
                dot.setImageResource(R.drawable.slider_dot_active)
            } else {
                dot.setImageResource(R.drawable.slider_dot_inactive)
            }
        }
    }

    private fun createMainMenu() {
        val homeMenuList = arrayListOf<MenuData>()

        val homeIconList = resources.obtainTypedArray(R.array.home_menu_icon)
        val homeTitleList = resources.obtainTypedArray(R.array.home_menu_title)

        for (i in 0 until min(homeIconList.length(), homeTitleList.length())) {
            val iconResId = homeIconList.getResourceId(i, 0)
            val title = homeTitleList.getString(i) ?: ""

            val homeMenuItem = MenuData(iconResId, title)
            homeMenuList.add(homeMenuItem)
        }

        binding.apply {
            rvMainMenu.layoutManager = GridLayoutManager(this@HomeActivity, 3)
            homeMenuAdapter = HomeMenuAdapter(this@HomeActivity, homeMenuList) { position, dataItem ->
                launchActivity<MenuDetailActivity> {
                    putExtra(EXTRA_POSITION, position)
                    putExtra(EXTRA_DATA, dataItem.menuTitle)
                }
            }
            rvMainMenu.adapter = homeMenuAdapter
        }

        homeIconList.recycle()
        homeTitleList.recycle()
    }

    /**
     * Runnable for slider handler
     * */
    private val sliderRunnable =
        Runnable { bannerSlider.currentItem += 1 }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.adminLogin -> {
                val adminUsername = sessionManager.getStringData(KEY_ADMIN_USERNAME)
                val adminId = sessionManager.getStringData(KEY_ADMIN_ID)
                if (adminUsername.isNotEmpty() && adminId.isNotEmpty()) {
                    launchActivity<AdminDashboardActivity>()
                } else {
                    launchActivity<AdminLoginActivity>()
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }

    companion object {
        private const val MENU_NEW_REGISTRATION = 0
        private const val MENU_ADVERTISE_REGISTRATION = 1
        private const val MENU_SETTINGS = 2
        private const val MENU_ABOUT_APP = 3
    }
}