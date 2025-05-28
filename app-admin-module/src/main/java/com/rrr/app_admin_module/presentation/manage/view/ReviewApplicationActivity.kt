package com.rrr.app_admin_module.presentation.manage.view

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.activity.ImagePreviewActivity
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.common.Resource
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.model.new_application.Applications
import com.rabadiya.base.utils.CommonUtils
import com.rabadiya.base.utils.CommonUtils.addSpace
import com.rabadiya.base.utils.Constants.EXTRA_DATA
import com.rabadiya.base.utils.Constants.EXTRA_URL
import com.rabadiya.base.utils.TAG
import com.rabadiya.base.utils.getParcelableExtraCompat
import com.rabadiya.base.utils.hide
import com.rabadiya.base.utils.hideKeyboard
import com.rabadiya.base.utils.launchActivity
import com.rabadiya.base.utils.loadImage
import com.rabadiya.base.utils.setSafeOnClickListener
import com.rabadiya.base.utils.show
import com.rabadiya.base.utils.showKeyBoard
import com.rabadiya.base.utils.showToast
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationRequest
import com.rrr.app_admin_module.R
import com.rrr.app_admin_module.databinding.ActivityReviewApplicationBinding
import com.rrr.app_admin_module.presentation.manage.viewmodel.ReviewApplicationViewModel
import com.rrr.app_admin_module.widget.bottomsheet.RejectApplicationMessageBottomSheet
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewApplicationActivity : BaseActivity<ActivityReviewApplicationBinding>(
    ActivityReviewApplicationBinding::inflate
) {
    private val viewModel: ReviewApplicationViewModel by viewModel()
    private var applications: Applications? = null
    private var isNameEdit = false
    private var isFatherEdit = false
    private var firstName: String = ""
    private var fatherOrHusband: String = ""
    private var job: Job? = null

    override fun setContent() {
        getIntentData()
        setupUI()
    }

    private fun getIntentData() {
        if (intent.hasExtra(EXTRA_DATA)) {
            applications = intent.getParcelableExtraCompat(EXTRA_DATA)
        }
    }

    private fun setupUI() {
        with(binding) {
            ivProfileImage.loadImage(
                applications?.profileImage.toString(),
                com.rabadiya.base.R.drawable.ic_user
            )

            tvName.text = applications?.firstName.toString()
            tvFatherHusbandName.text = applications?.fatherHusbundName.toString()
            birthDate.text = applications?.birthDate.toString()
            address.text = applications?.address.toString()
            gender.text = applications?.gender.toString()
            mobileNumber.text = applications?.mobileNo.toString()
            emailId.text = applications?.emailId.toString()
            business.text = applications?.occupation.toString()
            businessType.text = applications?.businessType.toString()
            eduction.text = applications?.eduction.toString()
            relation.text = applications?.relation.toString()
            bloodType.text = applications?.bloodGroup.toString()
            married.text = applications?.maritalStatus.toString()
            village.text = applications?.village.toString()
            residence.text = applications?.currentCity.toString()
            imageIdProof.loadImage(applications?.document.toString())
        }

        setActionBar()
        setupClicks()
    }

    private fun setupClicks() {
        with(binding) {
            binding.btnNameEditCancel.setOnClickListener {
                if (isNameEdit) {
                    isNameEdit = false
                    etName.hide()
                    hideKeyboard()
                    btnNameEditCancel.setImageResource(R.drawable.ic_edit)
                } else {
                    isNameEdit = true
                    etName.show()
                    etName.requestFocus()
                    showKeyBoard()
                    btnNameEditCancel.setImageResource(com.rabadiya.base.R.drawable.ic_close)
                }
            }

            binding.btnFatherEditCancel.setOnClickListener {
                if (isFatherEdit) {
                    isFatherEdit = false
                    etFatherHusbandName.hide()
                    hideKeyboard()
                    btnFatherEditCancel.setImageResource(R.drawable.ic_edit)
                } else {
                    isFatherEdit = true
                    etFatherHusbandName.show()
                    etFatherHusbandName.requestFocus()
                    showKeyBoard()
                    btnFatherEditCancel.setImageResource(com.rabadiya.base.R.drawable.ic_close)
                }
            }

            imageIdProof.setSafeOnClickListener {
                launchActivity<ImagePreviewActivity> {
                    putExtra(EXTRA_URL, applications?.document)
                }
            }

            ivProfileImage.setSafeOnClickListener {
                launchActivity<ImagePreviewActivity> {
                    putExtra(EXTRA_URL, applications?.profileImage)
                }
            }

            btnApprove.setSafeOnClickListener {
                firstName = etName.text.toString().trim()
                fatherOrHusband = etFatherHusbandName.text.toString().trim()

                if (isValid) {
                    reviewApplication(isApprove = true)
                }
            }

            btnReject.setSafeOnClickListener {
                RejectApplicationMessageBottomSheet(this@ReviewApplicationActivity) { rejectedString ->
                    LOGI(TAG, "rejectedString: $rejectedString")
                    val manageApplicationReq = ManageApplicationRequest(
                        applicationId = applications?._id.toString(),
                        isApprove = false,
                        disApproveReason = rejectedString
                    )
                    viewModel.manageApplication(manageApplicationReq)
                    manageApplicationResponse()
                }
            }
        }
    }

    private fun reviewApplication(isApprove: Boolean) {
        val manageApplicationReq = ManageApplicationRequest(
            applicationId = applications?._id.toString(),
            firstName = firstName,
            fatherOrHusband = fatherOrHusband,
            isApprove = isApprove,
            disApproveReason = ""
        )
        viewModel.manageApplication(manageApplicationReq)
        manageApplicationResponse()
    }

    private fun setActionBar() {
        setSupportActionBar(binding.viewToolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.rabadiya.base.R.drawable.ic_back)
            setHomeButtonEnabled(true)
        }
        binding.viewToolbar.toolbarTitle.text = CommonUtils.buildString(
            applications?.firstName.toString(),
            addSpace(),
            applications?.fatherHusbundName.toString()
        )
        binding.viewToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun manageApplicationResponse() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.manageApplicationResponse.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(getStrings(R.string.application_review_progress_message))
                    }

                    is Resource.Success -> {
                        hideLoading()
                        job?.cancel()
                        resource.data?.data?.let {
                            showToast(resource.data?.message.toString())
                            setBackIntent()
                        }
                    }

                    is Resource.Error -> {
                        hideLoading()
                        job?.cancel()
                        resource.data?.data?.let {
                            showToast(resource.data?.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setBackIntent() {
        val intent = Intent().also {
            it.putExtra(EXTRA_DATA, applications?._id)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    val isValid: Boolean
        get() {
            when {
                firstName.isEmpty() -> {
                    isNameEdit = true
                    binding.etName.show()
                    binding.etName.requestFocus()
                    binding.btnNameEditCancel.setImageResource(com.rabadiya.base.R.drawable.ic_close)
                    binding.scrollView.smoothScrollTo(0, 0)
                    showKeyBoard()
                    return false
                }

                fatherOrHusband.isEmpty() -> {
                    isFatherEdit = true
                    binding.etFatherHusbandName.show()
                    binding.etFatherHusbandName.requestFocus()
                    binding.btnFatherEditCancel.setImageResource(com.rabadiya.base.R.drawable.ic_close)
                    binding.scrollView.smoothScrollTo(0, 0)
                    showKeyBoard()
                    return false
                }
            }
            return true
        }
}