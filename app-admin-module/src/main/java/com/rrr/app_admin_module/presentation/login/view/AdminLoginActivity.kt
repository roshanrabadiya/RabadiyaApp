package com.rrr.app_admin_module.presentation.login.view

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.common.PreferenceKey
import com.rabadiya.base.common.Resource
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.utils.afterTextChanged
import com.rabadiya.base.utils.isStringValid
import com.rabadiya.base.utils.launchActivityWithFinish
import com.rabadiya.base.utils.showToast
import com.rrr.app_admin_module.R
import com.rrr.app_admin_module.databinding.ActivityAdminLoginBinding
import com.rrr.app_admin_module.presentation.dashboard.view.AdminDashboardActivity
import com.rrr.app_admin_module.presentation.login.viewmodel.AdminLoginViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminLoginActivity :
    BaseActivity<ActivityAdminLoginBinding>(ActivityAdminLoginBinding::inflate),
    View.OnClickListener {
    private val adminViewModel: AdminLoginViewModel by viewModel()
    private var job: Job? = null

    override fun setContent() {
        setupUI()
    }

    private fun setupUI() {
        setActionBar()
        setupClicks()

        binding.etUsername.apply {
            afterTextChanged {
                if (it.isNotEmpty()) {
                    binding.etUsername.error = null
                }
            }
        }
        binding.etPassword.apply {
            afterTextChanged {
                if (it.isNotEmpty()) {
                    binding.etPassword.error = null
                }
            }
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.viewToolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.rabadiya.base.R.drawable.ic_back)
            setHomeButtonEnabled(true)
        }
        binding.viewToolbar.toolbarTitle.text = getString(R.string.admin_login)
        binding.viewToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupClicks() {
        with(binding) {
            btnLogin.setOnClickListener(this@AdminLoginActivity)
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> {
                if (verify) {
                    val username = binding.etUsername.text.toString().trim()
                    val password = binding.etPassword.text.toString().trim()
                    adminViewModel.adminLogin(username = username, password = password)
                    setupApiResponse()
                }
            }
        }
    }

    private fun setupApiResponse() {
        job?.cancel()
        job = lifecycleScope.launch {
            adminViewModel.adminLoginResponse.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(getStrings(R.string.admin_loading_message))
                    }

                    is Resource.Success -> {
                        job?.cancel()
                        hideLoading()
                        resource.data?.data.let {
                            sessionManager.saveData(PreferenceKey.KEY_ADMIN_ID, it?.id ?: "")
                            sessionManager.saveData(
                                PreferenceKey.KEY_ADMIN_USERNAME,
                                it?.username ?: ""
                            )
                            launchActivityWithFinish<AdminDashboardActivity>()
                            showToast(getString(R.string.login_success_message))
                        }
                    }

                    is Resource.Error -> {
                        job?.cancel()
                        hideLoading()
                        resource.message?.let {
                            showToast(it)
                        }
                    }
                }
            }
        }
    }

    private val verify: Boolean
        get() {
            when {
                !binding.etUsername.isStringValid() -> {
                    binding.etUsername.error = getStrings(R.string.error_username)
                    binding.etUsername.requestFocus()
                    return false
                }

                !binding.etPassword.isStringValid() -> {
                    binding.etPassword.error = getStrings(R.string.error_password)
                    binding.etPassword.requestFocus()
                    return false
                }
            }
            return true
        }

}