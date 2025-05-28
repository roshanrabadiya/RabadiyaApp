package com.rabadiya.base.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rabadiya.base.R
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.databinding.ActivityImagePreviewBinding
import com.rabadiya.base.utils.Constants.EXTRA_URL
import com.rabadiya.base.utils.TAG
import com.rabadiya.base.utils.loadImage

class ImagePreviewActivity : BaseActivity<ActivityImagePreviewBinding>(ActivityImagePreviewBinding::inflate) {

    override fun setContent() {
        LOGI(TAG, "ImagePreviewActivity")

        binding.ivPreview.loadImage(intent.getStringExtra(EXTRA_URL).toString())
        binding.ivClose.setOnClickListener {
            finish()
        }
    }
}