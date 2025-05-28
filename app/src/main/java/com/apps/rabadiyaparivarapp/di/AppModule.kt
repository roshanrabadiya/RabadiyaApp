package com.apps.rabadiyaparivarapp.di

import com.rabadiya.base.customviews.customtextview.TextViewStyler
import com.rabadiya.base.common.AppPreference
import com.rabadiya.base.permissions.PermissionManagerImpl
import com.rabadiya.parivar.network_module.repository.home.HomeRepository
import org.koin.dsl.module

val appModule = module {

    single { AppPreference(get()) }

    single { TextViewStyler(get()) }

    single { PermissionManagerImpl(get()) }

    single { HomeRepository(get()) }
}