package dev.alks.movieshow.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import dev.alks.movieshow.R
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport


fun TextView.rateColor(rate: Double): Int {
    when (rate) {
        in 0.0..4.0 -> {
            return ContextCompat.getColor(this.context, R.color.low_rate)
        }
        in 4.1..7.9 -> {
            return ContextCompat.getColor(this.context, R.color.mid_rate)
        }
        in 8.0..10.0 -> {
            return ContextCompat.getColor(this.context, R.color.high_rate)
        }
        else -> return ContextCompat.getColor(this.context, R.color.undefined_rate)
    }
}

fun TextView.setHtmlText(htmlText: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
    } else {
        this.text = Html.fromHtml(htmlText);
    }

}

fun ImageView.setImageUrl(imageUrl: String) {
    if (imageUrl.isBlank()) return
    Picasso.get().load(imageUrl).error(R.drawable.ic_placeholder_image).into(this)
}

fun TextView.setRate(rate: Double?) {
    rate?.let {
        this.text = it.toString()
        this.rateColor(rate = it) }?.let { this.setBackgroundColor(it) }

}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
    checkNotNull(clazz) { "clazz is null" }
    return filter { !clazz.isInstance(it) }
}


/**
 * Used to allow Singleton with arguments in Kotlin while keeping the code efficient and safe.
 *
 * See https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
 */
open class SingletonHolderSingleArg<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                created
            }
        }
    }
}