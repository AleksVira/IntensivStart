package ru.androidschool.intensiv.common

import android.os.SystemClock
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import timber.log.Timber

fun View.setThrottleClickListener(
    debounceTime: Long = 900L,
    action: () -> Unit
) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}
