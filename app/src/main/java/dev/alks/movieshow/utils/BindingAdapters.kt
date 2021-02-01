package dev.alks.movieshow.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.squareup.picasso.Picasso
import dev.alks.movieshow.R
import io.reactivex.Observable

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("setImageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
        if(url.isNullOrEmpty())return
        Picasso.get().load(url).error(R.drawable.ic_placeholder_image).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("setRate")
    fun setRate(view: TextView, rate: ObservableField<Double?>) {
        rate.get()?.let { view.rateColor(rate = it) }?.let { view.setBackgroundColor(it) }
    }

    @JvmStatic
    @BindingAdapter("setDescription")
    fun setDescription(view: TextView, text: ObservableField<String?>) {
        text.get()?.let { view.setHtmlText(it) }
    }

}