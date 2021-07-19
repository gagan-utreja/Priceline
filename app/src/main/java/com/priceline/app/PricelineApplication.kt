package com.priceline.app

import android.app.Application
import android.content.Context
import com.priceline.app.PricelineApplication.Companion.appComponent
import com.priceline.app.di.AppComponent
import com.priceline.app.di.DaggerAppComponent
import com.priceline.app.di.module.AppModule
import com.priceline.app.di.module.NetModule
import com.priceline.app.utils.Constants

class PricelineApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        // Initialization Dagger
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).netModule(NetModule(Constants.base_url)).build()
        appComponent?.inject(this)
    }

    companion object {
        @JvmStatic
        var appComponent: AppComponent? = null
            private set

        @JvmField
        var appContext: Context? = null
        operator fun get(context: Context?): PricelineApplication {
            return context!!.applicationContext as PricelineApplication
        }

        val app: PricelineApplication
            get() = Companion[appContext]
    }
}