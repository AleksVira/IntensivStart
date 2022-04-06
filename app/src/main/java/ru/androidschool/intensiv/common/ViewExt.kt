package ru.androidschool.intensiv.common

import android.os.SystemClock
import android.view.View

fun View.setThrottleClickListener(
    debounceTime: Long = 900L,
    action: () -> Unit
) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            when {
                SystemClock.elapsedRealtime() - lastClickTime < debounceTime -> return
                else -> action()
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}
