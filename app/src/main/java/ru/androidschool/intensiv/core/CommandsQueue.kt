package ru.androidschool.intensiv.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import java.util.*

class CommandsQueue<T> : MutableLiveData<Queue<T>>() {

    fun onNext(value: T) {
        val commands = getValue() ?: LinkedList()
        commands.add(value)
        setValue(commands)
    }
}

inline fun <T : Any> LifecycleOwner.observe(
    liveData: CommandsQueue<T>,
    crossinline block: (T) -> Unit
) {
    liveData.observe(this) { events ->
        val iterator = events.iterator()
        while (iterator.hasNext()) {
            block.invoke(iterator.next())
            iterator.remove()
        }
    }
}