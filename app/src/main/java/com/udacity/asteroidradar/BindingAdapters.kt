package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.main.AsteroidAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
fun recylerViewText(recyclerView: RecyclerView, list: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(list)
}

@BindingAdapter("imageurl")
fun imageUrl(imageView: ImageView, picture: PictureOfDay?) {
    picture?.let {
        val uri = it.url.toUri()
        Picasso.with(imageView.context).load(uri)
            .placeholder(R.drawable.default_day_of_image)
            .error(R.drawable.default_day_of_image)
            .into(imageView)
    }
}

@BindingAdapter("contentDescript")
fun imageOfDayContentDescription(imageView: ImageView, picture: PictureOfDay?) {
    picture?.let {
        imageView.contentDescription = picture.title
    }
}

//handle the visibility of the progressbar
@BindingAdapter("goneIfNotNull")
fun goneIfEmpty(view: View, it: LiveData<List<Asteroid>>) {
    view.visibility = if (it.value?.size != 0) View.GONE else View.VISIBLE
}
