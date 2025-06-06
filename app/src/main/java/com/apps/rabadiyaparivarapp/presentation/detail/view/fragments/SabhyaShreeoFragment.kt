package com.apps.rabadiyaparivarapp.presentation.detail.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.rabadiyaparivarapp.R
import com.apps.rabadiyaparivarapp.databinding.FragmentSabhyaShreeoBinding
import com.apps.rabadiyaparivarapp.databinding.ItemPaginationLoadingBinding
import com.apps.rabadiyaparivarapp.databinding.ItemSabhyaShreeoBinding
import com.apps.rabadiyaparivarapp.presentation.detail.viewmodel.SabhyaShreeoViewModel
import com.rabadiya.base.activity.BaseFragment
import com.rabadiya.base.adapters.GenericAdapter
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.common.PaginationScrollListener
import com.rabadiya.base.common.Resource
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.utils.TAG
import com.rabadiya.base.utils.afterTextChanged
import com.rabadiya.base.utils.hide
import com.rabadiya.base.utils.loadImage
import com.rabadiya.base.utils.show
import com.rabadiya.base.utils.showErrorToast
import com.rabadiya.base.utils.showToast
import com.rabadiya.parivar.network_module.model.sabhyoshree.SabhyaShree
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SabhyaShreeoFragment : BaseFragment<FragmentSabhyaShreeoBinding>() {

    private val viewModel: SabhyaShreeoViewModel by viewModel()
    private var sabhyaShreeAdapter: GenericAdapter<SabhyaShree, *>? = null
    private var page = 1
    private val limit = 8
    private var search = ""
    private var job: Job? = null
    private var hasMoreData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentSabhyaShreeoBinding {
        return FragmentSabhyaShreeoBinding.inflate(inflater, container, false)
    }

    override fun setContent() {
        setupAdapter()
        callApis()
        setupListeners()
    }


    private fun callApis() {
        viewModel.getSabhyaShreeo(search, page, limit)
        setupApiResponse()
    }

    private fun setupApiResponse(isPagination: Boolean = false) {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.sabhyaShreeoResponse.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        if (!isPagination) {
                            showLoading()
                        } else {
                            // sabhyaShreeAdapter?.showLoadingView()
                        }
                    }

                    is Resource.Success -> {
                        hideLoading()
                        job?.cancel()
                        resource.data?.data?.let {
                            if (!isPagination && it.sabhyaShree.isNotEmpty()) {
                                sabhyaShreeAdapter?.updateList(it.sabhyaShree)
                            } else {
                                if (isPagination) {
                                    sabhyaShreeAdapter?.updatePaginationList(it.sabhyaShree)
                                } else {
                                    binding.incNoData.main.show()
                                    binding.rvcSabhyaShreeo.hide()
                                }
                            }
                            hasMoreData = page <= it.pagination.totalPages
                            LOGI(TAG, "Api Call pages: ${page <= it.pagination.totalPages}}")
                        }
                    }

                    is Resource.Error -> {
                        hideLoading()
                        job?.cancel()
                        binding.incNoData.main.show()
                        binding.rvcSabhyaShreeo.hide()
                        resource.data?.errorMessage?.let {
                            requireActivity().showErrorToast(it)
                        }
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        sabhyaShreeAdapter = GenericAdapter(
            items = arrayListOf<SabhyaShree>(),
            bindingInflater = { inflater, parent, attachToParent ->
                ItemSabhyaShreeoBinding.inflate(inflater, parent, attachToParent)
            },
            loadingBindingInflater = { inflater, parent, attachToParent ->
                ItemPaginationLoadingBinding.inflate(inflater, parent, attachToParent)
            }) { binding, item, position ->
            binding.apply {
                item.profileImage?.let {
                    binding.ivImage.loadImage(it)
                } ?: binding.ivImage.loadImage("")

                tvName.text = buildString {
                    append(item.username)
                    append(" ")
                    append(item.fatherOrHusband)
                }

                tvSabhyaNo.text = requireContext().getStrings(R.string.sabhya_no, item.sabhyaNumber)
                tvVillageName.text = item.village
            }

        }

        val mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvcSabhyaShreeo.apply {
            adapter = sabhyaShreeAdapter
            layoutManager = mLayoutManager
            addOnScrollListener(
                PaginationScrollListener(
                    layoutManager = mLayoutManager,
                    hasMoreData = { hasMoreData },
                    loadMore = {
                        sabhyaShreeAdapter?.showLoadingView()
                        page++
                        viewModel.getSabhyaShreeo(search = search, page = page, limit = limit)
                        setupApiResponse(isPagination = true)
                    })
            )
        }

    }

    private fun setupListeners() {
        binding.etSearch.afterTextChanged { s ->
            val query = s.toString().trim()
            if (query.isEmpty()) {
                sabhyaShreeAdapter?.resetList()
                return@afterTextChanged
            }

            if (query.length > 2) {
                val found = sabhyaShreeAdapter?.filter { user ->
                    "${user.username} ${user.fatherOrHusband} ${user.village}".contains(
                        query, ignoreCase = true
                    ) || user.username.contains(
                        query, ignoreCase = true
                    ) == true || user.fatherOrHusband.contains(
                        query, ignoreCase = true
                    ) == true || user.village.contains(query, ignoreCase = true) == true
                }

                if (found == false) {
                    requireActivity().showToast("No application found")
                }

            }
        }
    }

}