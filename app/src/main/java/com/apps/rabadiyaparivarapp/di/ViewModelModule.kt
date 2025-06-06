package com.apps.rabadiyaparivarapp.di

import com.apps.rabadiyaparivarapp.presentation.detail.viewmodel.SabhyaShreeoViewModel
import com.apps.rabadiyaparivarapp.presentation.home.viewmodel.HomeViewModel
import com.apps.rabadiyaparivarapp.presentation.new_application.viewmodel.NewApplicationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { NewApplicationViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SabhyaShreeoViewModel(get()) }
}