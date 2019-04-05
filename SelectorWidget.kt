package com.maximej.android.widget

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.IntRange

class SelectorWidget : StateListDrawable() {

    fun addState(shape: ShapeComponent) {
        var shapeDrawable: Drawable = ShapeDrawable().apply {
            paint.color = shape.color
            setShape(
                    when (shape.shapeType) {
                        is ShapeType.Rounded -> {
                            RoundRectShape(shape.shapeType.corners.toArray(), null, null)
                        }
                        is ShapeType.Rect -> RectShape()
                        else -> OvalShape()
                    }
            )
        }.let {
            if (shape.rippleColor != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable(
                        ColorStateList.valueOf(shape.rippleColor),
                        it,
                        null
                )
            } else it
        }


        val stateArray = shape.state.map {
            it.getStateValue()
        }.toIntArray()

        addState(stateArray, shapeDrawable)
    }
}

private const val defaultRadius = 8

/**
 * state ids :
 * @attr ref android.R.styleable#StateListDrawable_visible
 * @attr ref android.R.styleable#StateListDrawable_variablePadding
 * @attr ref android.R.styleable#StateListDrawable_constantSize
 * @attr ref android.R.styleable#DrawableStates_state_focused
 * @attr ref android.R.styleable#DrawableStates_state_window_focused
 * @attr ref android.R.styleable#DrawableStates_state_enabled
 * @attr ref android.R.styleable#DrawableStates_state_checkable
 * @attr ref android.R.styleable#DrawableStates_state_checked
 * @attr ref android.R.styleable#DrawableStates_state_selected
 * @attr ref android.R.styleable#DrawableStates_state_activated
 * @attr ref android.R.styleable#DrawableStates_state_active
 * @attr ref android.R.styleable#DrawableStates_state_single
 * @attr ref android.R.styleable#DrawableStates_state_first
 * @attr ref android.R.styleable#DrawableStates_state_middle
 * @attr ref android.R.styleable#DrawableStates_state_last
 * @attr ref android.R.styleable#DrawableStates_state_pressed
 */

data class AndroidState(@AttrRes val state: Int, val activate: Boolean = true) {
    fun getStateValue(): Int {
        return if (activate) state else -state
    }
}

class RoundedShape(
        state: Array<AndroidState> = emptyArray(),
        shape: ShapeType.Rounded,
        @ColorInt color: Int,
        @ColorInt rippleColor: Int = 0
) : ShapeComponent(state, shape, color, rippleColor) {
    constructor(state: Array<AndroidState> = emptyArray(),
                radius: Int = 8,
                @ColorInt color: Int,
                @ColorInt rippleColor: Int = 0) : this(state, ShapeType.Rounded(Corners(radius)), color, rippleColor)

    constructor(state: Array<AndroidState> = emptyArray(),
                corners: Corners,
                @ColorInt color: Int,
                @ColorInt rippleColor: Int = 0) : this(state, ShapeType.Rounded(corners), color, rippleColor)
}

class RectShape(
        state: Array<AndroidState> = emptyArray(),
        @ColorInt color: Int,
        @ColorInt rippleColor: Int = 0
) : ShapeComponent(state, ShapeType.Rect, color, rippleColor)

class OvalShape(
        state: Array<AndroidState> = emptyArray(),
        @ColorInt color: Int,
        @ColorInt rippleColor: Int = 0
) : ShapeComponent(state, ShapeType.Rect, color, rippleColor)


@Suppress("ArrayInDataClass")
open class ShapeComponent(
        val state: Array<AndroidState> = emptyArray(),
        val shapeType: ShapeType,
        @ColorInt val color: Int,
        @ColorInt val rippleColor: Int = 0
)

data class Corners(val topStart: Float, val topEnd: Float, val bottomStart: Float, val bottomEnd: Float) {
    constructor(topStart: Int = 0, topEnd: Int = 0, bottomEnd: Int = 0, bottomStart: Int = 0) : this(topStart.toFloat(), topEnd.toFloat(), bottomEnd.toFloat(), bottomStart.toFloat())
    constructor(radius: Float = defaultRadius.toFloat()) : this(radius, radius, radius, radius)

    fun toArray(): FloatArray {
        return floatArrayOf(topStart, topStart, topEnd, topEnd, bottomStart, bottomStart, bottomEnd, bottomEnd)
    }

    companion object {
        @JvmStatic
        fun createStart(radius: Int = defaultRadius) = Corners(radius, 0, 0, radius)

        @JvmStatic
        fun createEnd(radius: Int = defaultRadius) = Corners(0, radius, radius, 0)

        @JvmStatic
        fun createTop(radius: Int = defaultRadius) = Corners(radius, radius, 0, 0)

        @JvmStatic
        fun createBottom(radius: Int = defaultRadius) = Corners(0, 0, radius, radius)
    }
}

sealed class ShapeType {
    data class Rounded(val corners: Corners = Corners()) : ShapeType()
    object Rect : ShapeType()
    object Oval : ShapeType()

    companion object {

        @JvmStatic
        fun roundedStart(radius: Int = defaultRadius) = Rounded(Corners(topStart = radius, bottomStart = radius))

        @JvmStatic
        fun roundedEnd(radius: Int = defaultRadius) = Rounded(Corners(topEnd = radius, bottomEnd = radius))

        @JvmStatic
        fun roundedTop(radius: Int = defaultRadius) = Rounded(Corners(topStart = radius, topEnd = radius))

        @JvmStatic
        fun roundedBottom(radius: Int = defaultRadius) = Rounded(Corners(bottomStart = radius, bottomEnd = radius))

        /**
         * 0 : topStart, 1: topEnd, 2: bottomEnd, 3: bottomStart
         */
        @JvmStatic
        fun singleRounded(@IntRange(from = 0, to = 3) position: Int, radius: Int = defaultRadius) =
                when (position) {
                    0 -> Rounded(Corners(topStart = radius))
                    1 -> Rounded(Corners(topEnd = radius))
                    2 -> Rounded(Corners(bottomEnd = radius))
                    else -> Rounded(Corners(bottomStart = radius))
                }

    }
}
