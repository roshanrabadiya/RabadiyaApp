package com.rabadiya.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.rabadiya.base.R
import com.rabadiya.base.common.AppPreference
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.utils.LOCAL_GUJARATI
import com.rabadiya.base.widget.dialog.ProgressDialog
import org.koin.android.ext.android.inject
import java.util.Locale

abstract class BaseActivity<VB : ViewBinding>(val inflate: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    protected lateinit var binding: VB
    protected val sessionManager: AppPreference by inject()

    private fun updateBaseContextLocale(context: Context): Context {
        val locale = Locale(LOCAL_GUJARATI)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        setContent()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    abstract fun setContent()

    fun showLoading(message: String = getStrings(R.string.common_progress_message)) {
        progressDialog.showProgressDialog(message)
    }

    fun hideLoading() {
        progressDialog.dismissProgressDialog()
    }

    open fun loadFragment(container: Int, fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.commit()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideLoading()
    }

    /*fun showBackButton() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }*/

    /*protected open fun setActionBarWithMenu(activity: Activity) {
        val viewStub: ViewStub = activity.findViewById(R.id.view_toolbar)
        val inflatedView: View = viewStub.inflate()
        val toolbar: Toolbar = inflatedView.findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarTitle: CommonTextView = inflatedView.findViewById(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.app_title)
    }

    protected open fun setActionBar(activity: Activity) {
        val viewStub: ViewStub = activity.findViewById(R.id.view_toolbar)
        val inflatedView: View = viewStub.inflate()
        val toolbar: Toolbar = inflatedView.findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        val toolbarTitle: CommonTextView = inflatedView.findViewById(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.app_title)
    }*/


}