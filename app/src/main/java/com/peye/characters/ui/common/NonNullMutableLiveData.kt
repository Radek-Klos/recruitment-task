package com.peye.characters.ui.common

import androidx.lifecycle.MutableLiveData

class NonNullMutableLiveData<T>(private val default: T) : MutableLiveData<T>() {

    override fun getValue(): T {
        return super.getValue() ?: default
    }
}
