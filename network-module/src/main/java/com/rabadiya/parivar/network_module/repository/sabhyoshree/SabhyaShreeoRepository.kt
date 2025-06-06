package com.rabadiya.parivar.network_module.repository.sabhyoshree

import com.rabadiya.parivar.network_module.domain.ApiHelper

class SabhyaShreeoRepository(
    private val apiHelper: ApiHelper
) {
    suspend fun getSabhyaShreeo(
        search: String,
        page: Int,
        limit: Int
    ) = apiHelper.getSabhyaShreeo(search, page, limit)
}