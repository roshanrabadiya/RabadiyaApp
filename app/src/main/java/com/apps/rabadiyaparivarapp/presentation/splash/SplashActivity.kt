package com.apps.rabadiyaparivarapp.presentation.splash

import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.apps.rabadiyaparivarapp.R
import com.apps.rabadiyaparivarapp.databinding.ActivitySplashBinding
import com.apps.rabadiyaparivarapp.presentation.home.view.HomeActivity
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.utils.launchActivity
import com.rabadiya.base.utils.launchActivityWithFinish
import com.rabadiya.parivar.network_module.common.TokenManager
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val tokenManager: TokenManager by inject()

    override fun setContent() {
        val iconAnimation = AnimationUtils.loadAnimation(this, R.anim.app_icon_anim)
        binding.appIcon.startAnimation(iconAnimation)
        if (tokenManager.checkToken()) {
            launchWithDelay()
        } else {
            tokenManager.fetchToken {
                launchActivity<HomeActivity>()
            }
        }
    }

    private fun launchWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            launchActivityWithFinish<HomeActivity>()
        }, 3000)
    }

}