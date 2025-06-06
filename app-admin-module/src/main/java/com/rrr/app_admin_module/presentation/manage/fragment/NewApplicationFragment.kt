package com.rrr.app_admin_module.presentation.manage.fragment

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rabadiya.base.activity.BaseFragment
import com.rabadiya.base.adapters.GenericAdapter
import com.rabadiya.base.common.Resource
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.model.new_application.Applications
import com.rabadiya.base.utils.Constants.EXTRA_DATA
import com.rabadiya.base.utils.afterTextChanged
import com.rabadiya.base.utils.formatDateToGujarati
import com.rabadiya.base.utils.hide
import com.rabadiya.base.utils.loadImage
import com.rabadiya.base.utils.setSafeOnClickListener
import com.rabadiya.base.utils.show
import com.rabadiya.base.utils.showErrorToast
import com.rabadiya.base.utils.showToast
import com.rrr.app_admin_module.R
import com.rrr.app_admin_module.databinding.FragmentNewApplicationBinding
import com.rrr.app_admin_module.databinding.ItemNewApplicationsBinding
import com.rrr.app_admin_module.presentation.login.viewmodel.NewApplicationViewModel
import com.rrr.app_admin_module.presentation.manage.view.ReviewApplicationActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewApplicationFragment : BaseFragment<FragmentNewApplicationBinding>() {

    private val viewModel: NewApplicationViewModel by viewModel()
    private var job: Job? = null
    private var isReview: Boolean = false
    private var page = 1
    private var limit = 10
    private var applications: ArrayList<Applications> = arrayListOf()
    private var adapter: GenericAdapter<Applications, *>? = null


    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleResult(result)
        }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewApplicationBinding {
        return FragmentNewApplicationBinding.inflate(inflater, container, false)
    }

    override fun setContent() {
        setupListeners()
        setAdapter()
        setupApiResponse()
    }

    private fun setupListeners() {
        binding.etSearch.afterTextChanged { s ->
            val query = s.toString().trim()
            if (query.isEmpty()) {
                adapter?.resetList()
                return@afterTextChanged
            }

            if (query.length > 2) {
                val found = adapter?.filter { user ->
                    "${user.firstName} ${user.fatherHusbundName}".contains(
                        query,
                        ignoreCase = true
                    ) ||
                            user.firstName?.contains(query, ignoreCase = true) == true ||
                            user.fatherHusbundName?.contains(query, ignoreCase = true) == true
                }

                if (found == false) {
                    requireActivity().showToast("No application found")
                }

            }
        }
    }

    private fun setupApiResponse() {
        viewModel.getApplications(
            isReview = isReview,
            page = page,
            limit = limit
        )
        setupApplicationsApiResponse()
    }

    private fun setupApplicationsApiResponse() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.allApplication.collect { states ->
                when (states) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        job?.cancel()
                        hideLoading()
                        states.data?.data?.let { mData ->
                            if (mData.applicationsList.isNotEmpty()) {
                                binding.incNoData.main.hide()
                                binding.rvcNewApplication.show()
                                binding.etSearch.show()
                                if (page == 1) {
                                    applications.clear()
                                }
                                applications.addAll(mData.applicationsList)
                                adapter?.updateList(applications)
                            } else {
                                binding.incNoData.tvMessage.text =
                                    requireContext().getStrings(R.string.res_no_new_applications)
                                binding.incNoData.main.show()
                                binding.rvcNewApplication.hide()
                                binding.etSearch.hide()
                            }
                        }
                    }

                    is Resource.Error -> {
                        job?.cancel()
                        hideLoading()
                        binding.incNoData.tvMessage.text =
                            requireContext().getStrings(R.string.res_no_new_applications)
                        binding.incNoData.main.show()
                        binding.rvcNewApplication.hide()
                        binding.etSearch.hide()
                        states.data?.errorMessage?.let { errorMessage ->
                            requireContext().showErrorToast(errorMessage)
                        }
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        adapter = GenericAdapter(
            items = applications,
            bindingInflater = { inflater, parent, attachToParent ->
                ItemNewApplicationsBinding.inflate(inflater, parent, attachToParent)
            },
        ) { binding, item, position ->
            item.profileImage?.let {
                binding.ivImage.loadImage(it)
            } ?: binding.ivImage.loadImage("")

            binding.tvName.text = buildString {
                append(item.firstName)
                append(" ")
                append(item.fatherHusbundName)
            }
            binding.tvVillageName.text = item.village
            binding.tvApplicationDate.text = formatDateToGujarati(item.createdAt.toString())
            binding.root.setSafeOnClickListener {
                val intent = Intent(requireContext(), ReviewApplicationActivity::class.java).also {
                    it.putExtra(EXTRA_DATA, item)
                }
                activityLauncher.launch(intent)
            }
        }

        binding.rvcNewApplication.layoutManager = LinearLayoutManager(context)
        binding.rvcNewApplication.adapter = adapter
    }

    private fun handleResult(result: ActivityResult) {
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.let { data ->
                    val applicationId = data.getStringExtra(EXTRA_DATA)
                    val application = applications.find { it._id == applicationId }
                    applications.remove(application)
                    adapter?.updateList(applications)
                }
            }
        }
    }

}