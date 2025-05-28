package com.rrr.app_admin_module.di

import com.rrr.app_admin_module.presentation.login.viewmodel.AdminLoginViewModel
import com.rrr.app_admin_module.presentation.login.viewmodel.NewApplicationViewModel
import com.rrr.app_admin_module.presentation.manage.viewmodel.ReviewApplicationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val adminModule = module {
    
    // viewmodel
    viewModel { AdminLoginViewModel(get()) }
    viewModel { NewApplicationViewModel(get()) }
    viewModel { ReviewApplicationViewModel(get()) }

}