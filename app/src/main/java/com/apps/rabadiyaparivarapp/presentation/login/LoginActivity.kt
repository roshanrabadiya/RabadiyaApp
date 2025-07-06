package com.apps.rabadiyaparivarapp.presentation.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.apps.rabadiyaparivarapp.R
import com.apps.rabadiyaparivarapp.databinding.ActivityLoginBinding
import com.apps.rabadiyaparivarapp.presentation.register.RegisterNewMemberActivity
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.utils.CommonUtils.formatMobileNumberView
import com.rabadiya.base.utils.launchActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    override fun setContent() {
        setupUi()
    }

    private fun setupUi() {
        formatMobileNumberView(binding.etPhoneMobile)
        setClickListeners()
    }

    private fun setClickListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
               //
            }

            btnNewApplication.setOnClickListener {
                launchActivity<RegisterNewMemberActivity>()
            }
        }
    }
}