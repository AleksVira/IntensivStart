package ru.androidschool.intensiv.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class CoreFragment<S : CoreViewState, E : CoreViewEvent, VM : CoreViewModel<S, E>>
    : Fragment {

    constructor() : super()
    constructor(@LayoutRes layoutResID: Int) : super(layoutResID)

    protected abstract val viewModel: VM

    protected abstract fun render(state: S)

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.state().observe(viewLifecycleOwner, ::render)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}