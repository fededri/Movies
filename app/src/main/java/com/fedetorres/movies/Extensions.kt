package com.fedetorres.movies

import android.animation.*
import android.support.annotation.IdRes
import android.util.Property
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator


fun <T : View> BaseActivity.inflate(@IdRes res: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy { findViewById<T>(res) }
}

fun View.visible() {
    visibility = View.VISIBLE
}


fun View.gone() {
    visibility = View.GONE
}

fun View.animateView(
    property: Property<View, Float> = View.SCALE_X,
    finalViewVisibility: Int = View.GONE,
    duration: Long = 500,
    listener: Animator.AnimatorListener? = null,
    initialScale: Float = 1f,
    finalScale: Float = 0f,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
) {

    val animator = ObjectAnimator.ofFloat(this, property, initialScale, finalScale)


    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            visibility = finalViewVisibility
        }
    })


    if (listener != null)
        animator.addListener(listener)


    animator.duration = duration
    animator.interpolator = interpolator
    animator.start()
}


fun View.playAnimations(
    animators: List<ObjectAnimator>, duration: Long = 500,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
) {
    val set = AnimatorSet()
    set.playTogether(animators)
    set.duration = duration
    set.interpolator = interpolator
    set.start()
}
