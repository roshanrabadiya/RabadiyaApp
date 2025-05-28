package com.rabadiya.base.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.rabadiya.base.R
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.utils.TAG
import com.rabadiya.base.widget.dialog.ProgressDialog

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private var progressDialog: ProgressDialog? = null

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContent()
    }

    fun showLoading(message: String = requireContext().getStrings(R.string.common_progress_message)) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(requireContext())
        }

        // Check if fragment is still attached before showing dialog
        if (isAdded && !isDetached && activity != null && !activity?.isFinishing!!) {
            progressDialog?.showProgressDialog(message)
        }
    }

    fun hideLoading() {
        LOGI(TAG, "Hide loading: ${progressDialog == null}")
        if (isAdded && progressDialog != null) {
            progressDialog?.dismissProgressDialog()
        }
    }

    abstract fun setContent()

    override fun onDestroyView() {
        hideLoading()
        progressDialog = null
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideLoading()
        progressDialog = null
    }
}