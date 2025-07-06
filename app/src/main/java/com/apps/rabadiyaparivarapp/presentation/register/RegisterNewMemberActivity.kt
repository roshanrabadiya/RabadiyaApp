package com.apps.rabadiyaparivarapp.presentation.register

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.RadioButton
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.apps.rabadiyaparivarapp.R
import com.apps.rabadiyaparivarapp.databinding.ActivityRegisterNewMemberBinding
import com.apps.rabadiyaparivarapp.presentation.new_application.view.AddNewApplicationActivity
import com.apps.rabadiyaparivarapp.presentation.new_application.viewmodel.NewApplicationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.rabadiya.base.activity.BaseActivity
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.common.PreferenceKey.KEY_TEMP_TOKEN
import com.rabadiya.base.common.Resource
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.permissions.PermissionManagerImpl
import com.rabadiya.base.utils.FileUtils
import com.rabadiya.base.utils.androidDeviceId
import com.rabadiya.base.utils.fetchDeviceId
import com.rabadiya.base.utils.isStringValid
import com.rabadiya.base.utils.isValidEmail
import com.rabadiya.base.utils.showSnackBar
import com.rabadiya.base.utils.showToast
import com.rabadiya.base.utils.trimString
import com.rabadiya.base.widget.bottomsheet.ChooseBottomSheetDialog
import com.rabadiya.base.widget.bottomsheet.ImagePickerBottomSheet
import com.rabadiya.base.widget.dialog.CommonDialog
import com.rabadiya.base.widget.dialog.TaskSuccessDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterNewMemberActivity :
    BaseActivity<ActivityRegisterNewMemberBinding>(ActivityRegisterNewMemberBinding::inflate) {

    private val newApplicationViewModel: NewApplicationViewModel by viewModel()
    private val permissionManager: PermissionManagerImpl by inject()
    private val commonDialog: CommonDialog by lazy {
        CommonDialog(this)
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                if (isProfileImage) {
                    isProfileSelected = true
                    selectedProfileUri = uri
                    binding.ivProfileImage.setImageURI(uri)
                } else {
                    isImageSelected = true
                    selectedImageUri = uri
                    binding.ctnIdProof.cardIdProof.visibility = View.VISIBLE
                    binding.ctnIdProof.imageIdProof.setImageURI(uri)
                }
            } else {
                showToast("No media selected")
            }
        }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                if (isProfileImage) {
                    isProfileSelected = true
                    selectedProfileUri = uri
                    binding.ivProfileImage.setImageURI(uri)
                } else {
                    isImageSelected = true
                    selectedImageUri = uri
                    binding.ctnIdProof.cardIdProof.visibility = View.VISIBLE
                    binding.ctnIdProof.imageIdProof.setImageURI(uri)
                }
            } else {
                showToast("No media selected")
            }
        }

    private val cameraPickerIntent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                if (isProfileImage) {
                    isProfileSelected = true
                    binding.ivProfileImage.setImageURI(selectedProfileUri)
                } else {
                    isImageSelected = true
                    binding.ctnIdProof.cardIdProof.visibility = View.VISIBLE
                    binding.ctnIdProof.imageIdProof.setImageURI(selectedImageUri)
                }
            }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                openCameraPermissionNeedDialog()
            }
        }

    private val openSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->

        }

    private var isImageSelected: Boolean = false
    private var selectedImageUri: Uri? = null

    private var isProfileSelected = false
    private var selectedProfileUri: Uri? = null

    private var isProfileImage: Boolean = false

    override fun setContent() {
        setActionBar()
        clickListeners()
        observeApiResponse()
    }

    private fun observeApiResponse() {
        lifecycleScope.launch {
            newApplicationViewModel.applicationState.collect { result ->
                when (result) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        hideLoading()
                        result.data?.let {
                            if (it.status) {
                                LOGI("TAG", "Api Token: ${it.data?.token}")
                                sessionManager.saveData(KEY_TEMP_TOKEN, it.data?.token ?: "")
                                showSuccess()
                            }
                        }
                    }

                    is Resource.Error -> {
                        hideLoading()
                        result.data?.errorMessage?.let {
                            showError(it)
                        }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        hideLoading()
        showToast(message)
    }

    private fun showSuccess() {
        TaskSuccessDialog(this@RegisterNewMemberActivity, onDialogDismiss = {
            startActivity(
                Intent(
                    this@RegisterNewMemberActivity,
                    AddNewApplicationActivity::class.java
                )
            )
            finish()
        }).showDialog(isCancelable = false)
    }


    private fun setActionBar() {
        setSupportActionBar(binding.viewToolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.rabadiya.base.R.drawable.ic_back)
            setHomeButtonEnabled(true)
        }
        binding.viewToolbar.toolbarTitle.text = getString(R.string.res_new_registration)
        binding.viewToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun clickListeners() {
        binding.apply {
            etBirthDate.setOnClickListener {
                openDatePickerDialog()
            }

            ctnIdProof.flUploadImage.setOnClickListener {
                isProfileImage = false
                openImagePickerBottomSheet()
            }

            etBusinessType.setOnClickListener {
                val businessItems = resources.getStringArray(R.array.array_business_type)
                openBottomSheet(
                    dataList = businessItems,
                    callBack = { data -> etBusinessType.setText(data) })
            }

            etEducation.setOnClickListener {
                val eductionItems = resources.getStringArray(R.array.array_eduction)
                openBottomSheet(
                    dataList = eductionItems,
                    callBack = { data -> etEducation.setText(data) })
            }

            etRelation.setOnClickListener {
                val relationItems = resources.getStringArray(R.array.array_relation)
                openBottomSheet(
                    dataList = relationItems,
                    callBack = { data -> etRelation.setText(data) })
            }

            etBloodGroup.setOnClickListener {
                val bloodGroupItems = resources.getStringArray(R.array.array_blood_group)
                openBottomSheet(
                    dataList = bloodGroupItems,
                    callBack = { data -> etBloodGroup.setText(data) })
            }

            etVillage.setOnClickListener {
                val villageItems = resources.getStringArray(R.array.array_village)
                openBottomSheet(
                    dataList = villageItems,
                    callBack = { data -> etVillage.setText(data) })
            }

            etResidence.setOnClickListener {
                val residenceItems = resources.getStringArray(R.array.array_residence)
                openBottomSheet(
                    dataList = residenceItems,
                    callBack = { data -> etResidence.setText(data) })
            }

            btnSubmitApplication.setOnClickListener {
                showLoading(getStrings(R.string.res_new_application_dialog_title))
                lifecycleScope.launch(Dispatchers.IO) {
                    validateFields { allFields ->
                        apiCall(allFields)
                    }
                }

            }

            ctnProfile.setOnClickListener {
                isProfileImage = true
                openImagePickerBottomSheet()
            }
        }
    }

    private fun apiCall(allFields: HashMap<String, String>) {
        newApplicationViewModel.newApplication(
            params = allFields,
            idProofFile = getFileFromUri(selectedImageUri!!),
            profileImage = getFileFromUri(selectedProfileUri!!)
        )
    }

    private fun openDatePickerDialog() {
        val materialDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setPositiveButtonText("Submit").setNegativeButtonText("Cancel").build()

        materialDatePicker.addOnPositiveButtonClickListener { timeInMillis ->
            val date = Date(timeInMillis)
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val formattedDate = simpleDateFormat.format(date)
            binding.etBirthDate.setText(formattedDate)
        }
        materialDatePicker.addOnNegativeButtonClickListener {
            materialDatePicker.clearOnCancelListeners()
        }
        materialDatePicker.show(supportFragmentManager, "Material 2 Date Picker")
    }

    private fun openBottomSheet(dataList: Array<String>, callBack: (String) -> Unit) {
        ChooseBottomSheetDialog(
            this@RegisterNewMemberActivity, dataList
        ) { data ->
            callBack(data)
        }
    }

    private fun openImagePickerBottomSheet() {
        ImagePickerBottomSheet(
            context = this@RegisterNewMemberActivity,
            imageSelected = if (isProfileImage) isProfileSelected else isImageSelected,
            cameraCallBack = {
                checkCameraPermission()
            },
            galleryCallBack = {
                openGalleryPicker()
            },
            removeCallBack = {
                if (isProfileImage) {
                    isProfileSelected = false
                    selectedProfileUri = null
                    binding.ivProfileImage.setImageResource(com.rabadiya.base.R.drawable.ic_user)
                } else {
                    isImageSelected = false
                    selectedImageUri = null
                    binding.ctnIdProof.cardIdProof.visibility = View.GONE
                }
            })
    }

    private fun checkCameraPermission() {
        if (permissionManager.hasCameraPermission()) {
            openCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            externalCacheDir
        )

        val imageUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            photoFile
        )
        if (isProfileImage) {
            selectedProfileUri = imageUri
        } else {
            selectedImageUri = imageUri
        }

        cameraPickerIntent.launch(imageUri)
    }

    private fun openGalleryPicker() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            imagePickerLauncher.launch("image/*")
        }
    }

    private fun validateFields(onComplete: (HashMap<String, String>) -> Unit) {
        val requireFields = HashMap<String, String>()
        with(binding) {
            requireFields["deviceId"] = androidDeviceId ?: ""
            if (etName.isStringValid()) {
                requireFields["firstName"] = etName.trimString()
            } else {
                main.showSnackBar("કૃપા કરીને નામ દાખલ કરો")
                hideLoading()
                return
            }

            if (etFatherHusbandName.isStringValid()) {
                requireFields["fatherHusbundName"] = etFatherHusbandName.trimString()
            } else {
                main.showSnackBar("કૃપા કરીને પિતા / પતિ નું નામ દાખલ કરો")
                hideLoading()
                return
            }

            if (etBirthDate.isStringValid()) {
                requireFields["birthDate"] = etBirthDate.trimString()
            } else {
                main.showSnackBar("કૃપા કરીને જન્મ તારીખ દાખલ કરો")
                hideLoading()
                return
            }

            if (etAddress.isStringValid()) {
                requireFields["address"] = etAddress.text.toString()
            } else {
                main.showSnackBar("કૃપા કરીને સરનામું દાખલ કરો")
                hideLoading()
                return
            }

            val selectedId = rgGender.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                val selectedText = radioButton.text.toString()
                requireFields["gender"] = selectedText
            } else {
                main.showSnackBar("કૃપા કરીને જાતિ પસંદ કરો")
                hideLoading()
                return
            }

            if (!etMobile.isStringValid()) {
                main.showSnackBar("કૃપા કરીને મોબાઇલ નંબર દાખલ કરો")
                hideLoading()
                return
            } else if (etMobile.trimString().length != 10) {
                main.showSnackBar("મોબાઇલ નંબર ૧૦ અંકનો હોવો જોઈએ")
                hideLoading()
                return
            } else {
                requireFields["mobileNo"] = etMobile.trimString()
            }

            if (!etEmail.isStringValid()) {
                main.showSnackBar("કૃપા કરીને ઈમેઇલ આઈડી દાખલ કરો")
                hideLoading()
                return
            } else if (!isValidEmail(etEmail.trimString())) {
                main.showSnackBar("સાચો ઈમેઇલ આઈડી દાખલ કરો")
                hideLoading()
                return
            } else {
                requireFields["emailId"] = etEmail.trimString()
            }

            if (etPassword.getText().trim().isEmpty() || etPassword.getText().trim()
                    .contains(" ")
            ) {
                main.showSnackBar("કૃપા કરીને પાસવર્ડ દાખલ કરો")
                hideLoading()
                return
            } else if (etPassword.getText().trim().length < 8) {
                main.showSnackBar("પાસવર્ડ ૭ અક્ષરોથી વધુ હોવો જોઈએ")
                hideLoading()
                return
            } else {
                requireFields["password"] = etPassword.getText().trim()
            }


            val businessId = rgBusiness.checkedRadioButtonId
            if (businessId != -1) {
                val radioButton = findViewById<RadioButton>(businessId)
                val selectedText = radioButton.text.toString()
                requireFields["occupation"] = selectedText
            } else {
                main.showSnackBar("કૃપા કરીને વ્યવસાય પસંદ કરો")
                hideLoading()
                return
            }

            if (etBusinessType.isStringValid()) {
                requireFields["businessType"] = etBusinessType.trimString()
            } else {
                main.showSnackBar("કૃપા કરીને વ્યવસાય ક્ષેત્રે પસંદ કરો")
                hideLoading()
                return
            }

            if (etEducation.text.toString().isEmpty()) {
                requireFields["eduction"] = "માહિતી દર્શાવેલ નથી"
            } else {
                requireFields["eduction"] = etEducation.trimString()
            }

            if (etRelation.text.toString().isEmpty()) {
                requireFields["relation"] = "માહિતી દર્શાવેલ નથી"
            } else {
                requireFields["relation"] = etRelation.trimString()
            }

            if (etBloodGroup.text.toString().isEmpty()) {
                requireFields["bloodGroup"] = "માહિતી દર્શાવેલ નથી"
            } else {
                requireFields["bloodGroup"] = etBloodGroup.trimString()
            }

            val maritalStatusId = rgMaritalStatus.checkedRadioButtonId
            if (maritalStatusId != -1) {
                val radioButton = findViewById<RadioButton>(maritalStatusId)
                val selectedText = radioButton.text.toString()
                requireFields["maritalStatus"] = selectedText
            } else {
                main.showSnackBar("કૃપા કરીને લગ્ન દરજ્જો પસંદ કરો")
                hideLoading()
                return
            }

            if (etVillage.isStringValid()) {
                requireFields["village"] = etVillage.trimString()
            } else {
                main.showSnackBar("કૃપા કરીને ગામ પસંદ કરો")
                hideLoading()
                return
            }

            if (etResidence.isStringValid()) {
                requireFields["currentCity"] = etResidence.trimString()
            } else {
                main.showSnackBar("કૃપા કરીને રહેણાંક પસંદ કરો")
                hideLoading()
                return
            }

            if (selectedImageUri == null) {
                main.showSnackBar("કૃપા કરીને આઈડી પ્રૂફ દર્શાવો")
                hideLoading()
                return
            }

            if (selectedProfileUri == null) {
                main.showSnackBar("કૃપા કરી ને પ્રોફાઇલ પસંદ કરો")
                hideLoading()
                return
            }

            if (!cbPpTc.isChecked) {
                main.showSnackBar("Terms and condition / privacy policy ટિક કરો")
                hideLoading()
                return
            }

            if (fetchDeviceId().isNotEmpty()) {
                requireFields["deviceId"] = fetchDeviceId()
            }

            onComplete(requireFields)

        }

    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
            parcelFileDescriptor?.let { pfd ->
                val inputStream = FileInputStream(pfd.fileDescriptor)
                val file = File.createTempFile("IMG_", ".jpg", externalCacheDir)
                file.outputStream().use { output ->
                    inputStream.copyTo(output)
                }
                if (file.length() > 300_000L) {
                    FileUtils.compressFile(file)
                } else {
                    file
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun openCameraPermissionNeedDialog() {
        commonDialog.showDialog(
            title = getString(com.rabadiya.base.R.string.permission_required),
            message = getString(R.string.res_camera_permission_rationale),
            isCancelable = false,
            positiveButtonText = getString(R.string.res_per_dialog_positive_button),
            negativeButtonText = getString(R.string.res_per_dialog_negative_button),
            handlePositiveButton = {
                openSettingLauncher.launch(permissionManager.openPermissionSetting())
            }
        )
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}