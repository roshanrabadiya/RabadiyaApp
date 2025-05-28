package com.apps.rabadiyaparivarapp.di

import com.apps.rabadiyaparivarapp.viewModel.HomeViewModel
import com.apps.rabadiyaparivarapp.viewModel.NewApplicationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { NewApplicationViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}