package com.apps.rabadiyaparivarapp.presentation.new_application.view

import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.apps.rabadiyaparivarapp.databinding.ActivityAddNewApplicationBinding
import com.apps.rabadiyaparivarapp.presentation.new_application.viewmodel.NewApplicationViewModel
import com.apps.rabadiyaparivarapp.presentation.register.RegisterNewMemberActivity
import com.rabadiya.base.R
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.adapters.GenericAdapter
import com.rabadiya.base.common.Resource
import com.rabadiya.base.enums.ApplicationStatus
import com.rabadiya.base.model.new_application.Applications
import com.rabadiya.base.utils.CommonUtils
import com.rabadiya.base.utils.Constants.APPROVE
import com.rabadiya.base.utils.Constants.PENDING
import com.rabadiya.base.utils.Constants.REJECT
import com.rabadiya.base.utils.androidDeviceId
import com.rabadiya.base.utils.formatDateToGujarati
import com.rabadiya.base.utils.getAndroidDeviceId
import com.rabadiya.base.utils.getColors
import com.rabadiya.base.utils.hide
import com.rabadiya.base.utils.launchActivity
import com.rabadiya.base.utils.loadImage
import com.rabadiya.base.utils.show
import com.rabadiya.base.utils.showErrorToast
import com.rrr.app_admin_module.databinding.ItemNewApplicationsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddNewApplicationActivity :
    BaseActivity<ActivityAddNewApplicationBinding>(ActivityAddNewApplicationBinding::inflate) {

    private val viewModel: NewApplicationViewModel by viewModel()
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
        lifecycleScope.launch {
            viewModel.checkApplication.collect {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
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
                            /*binding.rvcNewApplication.hide()
                            binding.tvNoData.show()*/
                        }
                    }

                    is Resource.Error -> {
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
            setHomeAsUpIndicator(R.drawable.ic_back)
            setHomeButtonEnabled(true)
        }
        binding.viewToolbar.toolbarTitle.text =
            getString(com.apps.rabadiyaparivarapp.R.string.res_new_registrations)
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

            binding.tvName.text =
                CommonUtils.buildString(item.firstName, " ", item.fatherHusbundName)
            binding.tvVillageName.text = item.village
            binding.tvApplicationDate.text = formatDateToGujarati(item.createdAt.toString())

            val status = when (item.applicationStatus) {
                APPROVE -> {
                    binding.tvStatus.setTextColor(getColors(R.color.colorSuccess))
                    ApplicationStatus.APPROVED.name
                }

                PENDING -> {
                    binding.tvStatus.setTextColor(getColors(R.color.colorPending))
                    ApplicationStatus.PENDING.name
                }

                REJECT -> {
                    binding.tvStatus.setTextColor(getColors(R.color.colorFail))
                    ApplicationStatus.REJECTED.name
                }

                else -> {
                    binding.tvStatus.setTextColor(getColors(R.color.colorPending))
                    ApplicationStatus.PENDING.name
                }
            }
            binding.tvStatus.text = status
        }

        binding.rvcNewApplication.adapter = adapter
    }
}