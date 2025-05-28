package com.apps.rabadiyaparivarapp.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.apps.rabadiyaparivarapp.databinding.SliderItemLayoutBinding
import com.rabadiya.base.model.home.SliderItem
import com.rabadiya.base.utils.showGlideImage

class SlideAdapter(
    private val context: Context,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    private var dataList: ArrayList<SliderItem> = arrayListOf()

    inner class SlideViewHolder(val binding: SliderItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        return SlideViewHolder(
            SliderItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            context.showGlideImage(item.image, ivSliderItem)
        }

        if (position == dataList.size - 2){
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setSliderData(dataList: ArrayList<SliderItem>){
        this.dataList = dataList
    }

    /*override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = SliderItemLayoutBinding.inflate(
            LayoutInflater.from(container.context), container, false
        )
        val item = dataList[position]
        context.showGlideImage(item.image, binding.ivSliderItem)

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return  dataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }*/

}