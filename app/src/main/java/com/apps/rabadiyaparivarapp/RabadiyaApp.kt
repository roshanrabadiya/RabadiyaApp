package com.apps.rabadiyaparivarapp

import android.app.Application
import com.apps.rabadiyaparivarapp.di.appModule
import com.apps.rabadiyaparivarapp.di.viewModelModule
import com.rabadiya.base.utils.androidDeviceId
import com.rabadiya.base.utils.getAndroidDeviceId
import com.rabadiya.parivar.network_module.di.networkModule
import com.rrr.app_admin_module.di.adminModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RabadiyaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        androidDeviceId = getAndroidDeviceId(this@RabadiyaApp)
        startKoin {
            androidContext(this@RabadiyaApp)
            modules(appModule, viewModelModule, networkModule, adminModule)
        }
    }
}