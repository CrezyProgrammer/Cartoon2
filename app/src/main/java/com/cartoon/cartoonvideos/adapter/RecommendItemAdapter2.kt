package com.cartoon.cartoonvideos.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cartoon.cartoonvideos.PlayActivity
import com.cartoon.cartoonvideos.R
import com.cartoon.cartoonvideos.databinding.LayoutBinding
import com.cartoon.cartoonvideos.entity.Video
import com.facebook.ads.*
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.native_ad_layout.view.*

class FacebookAdsRecyclerviewAdapter2(
    private val activity: Activity,
    private val list: ArrayList<Video>,
    private val mNativeAdsManager: NativeAdsManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val ITEM_VIEW_TYPE = 0
    private val AD_ITEM_VIEW_TYPE = 1
    private var mAdItems: ArrayList<NativeAd> = ArrayList()
    private val AD_DISPLAY_FREQUENCY = 5


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AD_ITEM_VIEW_TYPE) {
            val inflate =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.native_ad_layout, parent, false)
            NativeAdViewHolder(inflate as NativeAdLayout, activity)
        } else {
            val inflate =
                LayoutInflater.from(parent.context).inflate(R.layout.layout, parent, false)
            ItemViewHolder(inflate)
        }

    }

    override fun getItemCount(): Int {
        return list.size + mAdItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if ( position % AD_DISPLAY_FREQUENCY == 0)
            AD_ITEM_VIEW_TYPE
        else
            ITEM_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_VIEW_TYPE) {
            val index = position - position / AD_DISPLAY_FREQUENCY
           if (index<list.size){
               val item = list[index]

               holder as ItemViewHolder
               item.let { holder.bind(item) }
           }
        }
        else {
            try {
                Log.d("123321", position.toString())
                val ad: NativeAd
                if (mAdItems.size > position / AD_DISPLAY_FREQUENCY) {
                    ad = mAdItems[position / AD_DISPLAY_FREQUENCY]
                } else {
                    ad = mNativeAdsManager.nextNativeAd()
                    if (!ad.isAdInvalidated) {
                        mAdItems.add(ad)
                    } else {
                        Log.i(
                            "123321",
                            "Ad is invalidated!"
                        )
                    }
                }
                holder as NativeAdViewHolder
                holder.bind(ad)
            } catch (e: Exception) {
                Log.i("123321", "85:${e.message}")
                // FirebaseCrashlytics.getInstance().recordException(e)
                holder as NativeAdViewHolder
                holder.bind(null)
            }
        }
    }

    class ItemViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {



        fun bind(item: Video) {


            val binding= LayoutBinding.bind(itemView)
            Glide.with(itemView.context).load("https://img.youtube.com/vi/${item.id}/hqdefault.jpg").into(
                binding.imageView
            )
            binding.textView.text=item.title
            binding.views.text="${item.views} views"
            binding.date.text="${item.date} ago"
            binding.duration.text=item.duration
            binding.linearLayout.setOnClickListener {
                (it.context as Activity).finish()
                it.context.startActivity(
                    Intent(it.context, PlayActivity::class.java).putExtra(
                        "title",
                        item.title
                    ).putExtra("id", item.id)
                )

            }
        }
    }


    class NativeAdViewHolder(
        private val nativeAdLayout: NativeAdLayout,
        val context: Activity
    ) :
        RecyclerView.ViewHolder(nativeAdLayout) {

        val nativeAdIcon = nativeAdLayout.native_ad_icon
        val nativeAdTitle = nativeAdLayout.native_ad_title
        val nativeAdSponsoredLabel = nativeAdLayout.native_ad_sponsored_label
        val adChoicesContainer = nativeAdLayout.ad_choices_container
        val nativeAdMedia = nativeAdLayout.native_ad_media
        val nativeAdSocialContext = nativeAdLayout.native_ad_social_context
        val nativeAdBody = nativeAdLayout.native_ad_body
        val nativeAdCallToAction = nativeAdLayout.native_ad_call_to_action
        val layout=nativeAdLayout.layout


        fun bind(ad: NativeAd?) {
           layout.visibility=View.GONE
            adChoicesContainer.removeAllViews()
            Log.i("123321", "125:${ad != null}")
            if (ad != null)
            {
               layout.visibility=View.VISIBLE
                nativeAdTitle.text = ad.advertiserName
                nativeAdBody.text = ad.adBodyText
                nativeAdSocialContext.text = ad.adSocialContext
                nativeAdSponsoredLabel.text = context.getString(R.string.sponsored)
                nativeAdCallToAction.text = ad.adCallToAction
                nativeAdCallToAction.visibility = adActionButtonVisible(ad.hasCallToAction())
                val adOptionsView =
                    AdOptionsView(context, ad, nativeAdLayout)
                adChoicesContainer.addView(adOptionsView, 0)

                val clickableViews = ArrayList<View>()
                clickableViews.add(nativeAdIcon)
                clickableViews.add(nativeAdMedia)
                clickableViews.add(nativeAdCallToAction)
                ad.registerViewForInteraction(
                    nativeAdLayout,
                    nativeAdMedia,
                    nativeAdIcon,
                    clickableViews
                )

            }

        }


        private fun adActionButtonVisible(boolean: Boolean): Int {
            if (boolean)
                return View.VISIBLE
            return View.INVISIBLE
        }

    }

}