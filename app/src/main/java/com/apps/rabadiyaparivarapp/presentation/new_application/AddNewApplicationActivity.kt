package com.apps.rabadiyaparivarapp.presentation.new_application

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.apps.rabadiyaparivarapp.R
import com.apps.rabadiyaparivarapp.databinding.ActivityAddNewApplicationBinding
import com.apps.rabadiyaparivarapp.presentation.register.RegisterNewMemberActivity
import com.apps.rabadiyaparivarapp.viewModel.NewApplicationViewModel
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.adapters.GenericAdapter
import com.rabadiya.base.common.Resource
import com.rabadiya.base.model.new_application.Applications
import com.rabadiya.base.utils.Constants.EXTRA_DATA
import com.rabadiya.base.utils.androidDeviceId
import com.rabadiya.base.utils.formatDateToGujarati
import com.rabadiya.base.utils.getAndroidDeviceId
import com.rabadiya.base.utils.hide
import com.rabadiya.base.utils.launchActivity
import com.rabadiya.base.utils.loadImage
import com.rabadiya.base.utils.show
import com.rabadiya.base.utils.showErrorToast
import com.rabadiya.base.utils.showToast
import com.rrr.app_admin_module.databinding.ItemNewApplicationsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddNewApplicationActivity :
    BaseActivity<ActivityAddNewApplicationBinding>(ActivityAddNewApplicationBinding::inflate) {

    private val viewModel: NewApplicationViewModel by viewModel()
    private var job: Job? = null
    private var applications: ArrayList<Applications> = arrayListOf()
    private var adapter: GenericAdapter<Applications, *>? = null

    override fun setContent() {
        setupUI()
        checkApplications()
    }

    private fun setupUI() {
        setActionBar()
        setupClicks()
        setupAdapter()
    }

    private fun checkApplications() {
        val deviceId = androidDeviceId ?: getAndroidDeviceId(this@AddNewApplicationActivity)
        viewModel.checkApplications(deviceId)
        setupCheckApplicationObserver()
    }

    /**
     * open existing application list
     * or open new registration
     * */
    private fun setupCheckApplicationObserver() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.checkApplication.collect {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        job?.cancel()
                        hideLoading()
                        it.data?.data?.let { mData ->
                            if (mData.applicationsList.isNotEmpty()) {
                                binding.rvcNewApplication.show()
                                binding.tvNoData.hide()
                                applications.addAll(mData.applicationsList)
                                adapter?.updateList(applications)
                            } else {
                                binding.rvcNewApplication.hide()
                                binding.tvNoData.show()
                            }
                        } ?: {
                            binding.rvcNewApplication.hide()
                            binding.tvNoData.show()
                        }
                    }

                    is Resource.Error -> {
                        job?.cancel()
                        hideLoading()
                        binding.rvcNewApplication.hide()
                        binding.tvNoData.show()
                        it.data?.errorMessage?.let { errorMessage ->
                            showErrorToast(errorMessage)
                        }
                    }
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
        binding.viewToolbar.toolbarTitle.text = getString(R.string.res_new_registrations)
        binding.viewToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupClicks() {
        binding.btnRegisterNewMember.setOnClickListener {
            launchActivity<RegisterNewMemberActivity>()
        }
    }

    private fun setupAdapter() {
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
        }

        binding.rvcNewApplication.adapter = adapter
    }
}