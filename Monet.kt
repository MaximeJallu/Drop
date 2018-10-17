package com.maximejallu.utils

import android.annotation.SuppressLint
import android.support.annotation.DrawableRes
import android.widget.ImageView

@SuppressLint("StaticFieldLeak")
var instance: Monet? = null

class Monet private constructor(val strategy: VanGogh) : VanGogh {
    override fun load(path: String, target: ImageView, error: Int) {
        strategy.load(path, target, error)
    }

    companion object {

        fun get(): Monet {
            if (instance == null) {
                throw IllegalArgumentException("use init before use get method")
            }
            return instance!!
        }

        fun init(strategy: VanGogh) {
            instance = Monet(strategy)
        }
    }
}

interface VanGogh {
    fun load(path: String, target: ImageView, @DrawableRes error: Int = 0)
}
