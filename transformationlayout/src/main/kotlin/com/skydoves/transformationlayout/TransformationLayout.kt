/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.skydoves.transformationlayout

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.os.SystemClock
import android.transition.PathMotion
import android.transition.Transition
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.parcelize.Parcelize

public class TransformationLayout : FrameLayout, TransformationParams {

  /** A target view that will be transformed from [TransformationLayout]. */
  private lateinit var targetView: View

  /** Presents [TransformationLayout] is already transformed or not.  */
  public var isTransformed: Boolean = false
    private set

  /** Presents [TransformationLayout] is during transforming or not. */
  public var isTransforming: Boolean = false
    private set

  /** Duration of the transformation. */
  override var duration: Long = DefaultParamValues.duration

  /** [Motion] of the transformation path. */
  override var pathMotion: Motion = DefaultParamValues.pathMotion

  /** The id of the View whose overlay TransitionDrawable will be added to. */
  override var zOrder: Int = DefaultParamValues.zOrder

  /** The container color to be used as the background of the morphing container. */
  @ColorInt
  override var containerColor: Int = DefaultParamValues.containerColor

  @ColorInt
  /** The all container colors (start and end) to be used as the background of the morphing container. */
  override var allContainerColors: Int = DefaultParamValues.allContainerColors

  /** The color to be drawn under the morphing container but within the bounds of the zOrder. */
  @ColorInt
  override var scrimColor: Int = DefaultParamValues.scrimColor

  /** The [Direction] to be used by this transform. */
  override var direction: Direction = DefaultParamValues.direction

  /** The [FadeMode] to be used to swap the content of the start View with that of the end View. */
  override var fadeMode: FadeMode = DefaultParamValues.fadeMode

  /** The [FitMode] to be used when scaling the incoming content of the end View. */
  override var fitMode: FitMode = DefaultParamValues.fitMode

  /** The elevation that will be used to render a shadow around the container at the start of the transition. */
  override var startElevation: Float = DefaultParamValues.startElevation

  /** The elevation that will be used to render a shadow around the container at the end of the transition. */
  override var endElevation: Float = DefaultParamValues.endElevation

  /** Whether shadows should be drawn around the container to approximate native elevation shadows on the start and end views. */
  override var elevationShadowEnabled: Boolean = DefaultParamValues.elevationShadowEnabled

  /** Whether to hold the last frame at the end of the animation. */
  override var holdAtEndEnabled: Boolean = DefaultParamValues.holdAtEndEnabled

  @JvmField
  public var onTransformFinishListener: OnTransformFinishListener? = null

  public var throttledTime: Long = SystemClock.elapsedRealtime()

  public constructor(context: Context) : super(context)

  public constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    getAttrs(attributeSet)
  }

  public constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
    context,
    attributeSet,
    defStyle,
  ) {
    getAttrs(attributeSet, defStyle)
  }

  private fun getAttrs(attributeSet: AttributeSet) {
    val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TransformationLayout)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun getAttrs(attributeSet: AttributeSet, defStyleAttr: Int) {
    val typedArray =
      context.obtainStyledAttributes(
        attributeSet,
        R.styleable.TransformationLayout,
        defStyleAttr,
        0,
      )
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun setTypeArray(a: TypedArray) {
    a.getResourceId(R.styleable.TransformationLayout_transformation_targetView, -1).also {
      if (it != -1) {
        post {
          bindTargetView(rootView.findViewById(it))
        }
      }
    }
    this.duration =
      a.getInteger(R.styleable.TransformationLayout_transformation_duration, duration.toInt())
        .toLong()
    this.pathMotion =
      when (a.getInteger(R.styleable.TransformationLayout_transformation_pathMode, 0)) {
        0 -> Motion.ARC
        else -> Motion.LINEAR
      }
    this.zOrder = a.getInteger(R.styleable.TransformationLayout_transformation_zOrder, zOrder)
    this.containerColor =
      a.getColor(R.styleable.TransformationLayout_transformation_containerColor, containerColor)
    this.allContainerColors =
      a.getColor(
        R.styleable.TransformationLayout_transformation_allContainerColor,
        allContainerColors,
      )
    this.scrimColor =
      a.getColor(R.styleable.TransformationLayout_transformation_scrimColor, scrimColor)
    this.direction =
      when (a.getInteger(R.styleable.TransformationLayout_transformation_direction, 0)) {
        0 -> Direction.AUTO
        1 -> Direction.ENTER
        else -> Direction.RETURN
      }
    this.fadeMode =
      when (a.getInteger(R.styleable.TransformationLayout_transformation_fadeMode, 0)) {
        0 -> FadeMode.IN
        1 -> FadeMode.OUT
        2 -> FadeMode.CROSS
        else -> FadeMode.THROUGH
      }
    this.fitMode =
      when (a.getInteger(R.styleable.TransformationLayout_transformation_fitMode, 0)) {
        0 -> FitMode.AUTO
        1 -> FitMode.WIDTH
        else -> FitMode.HEIGHT
      }
    this.startElevation =
      a.getFloat(R.styleable.TransformationLayout_transformation_startElevation, startElevation)
    this.endElevation =
      a.getFloat(R.styleable.TransformationLayout_transformation_endElevation, endElevation)
    this.elevationShadowEnabled =
      a.getBoolean(
        R.styleable.TransformationLayout_transformation_elevationShadowEnabled,
        elevationShadowEnabled,
      )
    this.holdAtEndEnabled =
      a.getBoolean(
        R.styleable.TransformationLayout_transformation_holdAtEndEnabled,
        holdAtEndEnabled,
      )
  }

  /** sets a callback method which invoked when the transforming is finished. */
  public fun setOnTransformFinishListener(onTransformFinishListener: OnTransformFinishListener) {
    this.onTransformFinishListener = onTransformFinishListener
  }

  /** sets a callback method which invoked when the transforming is finished. */
  @JvmSynthetic
  public fun setOnTransformFinishListener(action: (Boolean) -> Unit) {
    setOnTransformFinishListener(
      OnTransformFinishListener { isTransformed -> action(isTransformed) },
    )
  }

  /**
   * binds a target view for transforming [TransformationLayout] to the target view.
   * This function must be called before invoking [startTransform] and [finishTransform].
   */
  public fun bindTargetView(targetView: View) {
    this.targetView = targetView.apply { visible(false) }
  }

  /**
   * sets an activity of the transition and returns a bundle of the transition information.
   * The return value must be used in startActivity's parameter as a bundle.
   * */
  public fun withActivity(activity: Activity, transitionName: String): Bundle {
    setTransitionName(transitionName)
    return ActivityOptions.makeSceneTransitionAnimation(activity, this, transitionName).toBundle()
  }

  /**
   * sets a context of the transition and returns a bundle of the transition information.
   * The return value must be used in startActivity's parameter as a bundle.
   * */
  public fun withContext(context: Context, transitionName: String): Bundle {
    setTransitionName(transitionName)
    val activity = context.getActivity()
    requireNotNull(activity) { "The context parameter is must an activity's context!" }
    return ActivityOptions.makeSceneTransitionAnimation(activity, this, transitionName).toBundle()
  }

  /**
   * sets an view of the transition and returns a bundle of the transition information.
   * The return value must be used in startActivity's parameter as a bundle.
   * */
  public fun withView(view: View, transitionName: String): Bundle {
    setTransitionName(transitionName)
    val activity = view.context.getActivity()
    requireNotNull(activity) { "The context parameter is must an activity's context!" }
    return ActivityOptions.makeSceneTransitionAnimation(activity, this, transitionName).toBundle()
  }

  /** get a bundle with parcelable transformation params. */
  public fun getBundle(keyName: String, transitionName: String? = this.transitionName): Bundle {
    if (transitionName != null) {
      setTransitionName(transitionName)
    }
    return Bundle().apply { putParcelable(keyName, getParams()) }
  }

  /** gets parameters of the [TransformationLayout.Params]. */
  public fun getParams(): Params {
    return Params(
      duration = this@TransformationLayout.duration,
      pathMotion = this@TransformationLayout.pathMotion,
      zOrder = this@TransformationLayout.zOrder,
      containerColor = this@TransformationLayout.containerColor,
      allContainerColors = this@TransformationLayout.allContainerColors,
      scrimColor = this@TransformationLayout.scrimColor,
      direction = this@TransformationLayout.direction,
      fadeMode = this@TransformationLayout.fadeMode,
      fitMode = this@TransformationLayout.fitMode,
      startElevation = this@TransformationLayout.startElevation,
      endElevation = this@TransformationLayout.endElevation,
      elevationShadowEnabled = this@TransformationLayout.elevationShadowEnabled,
      holdAtEndEnabled = this@TransformationLayout.holdAtEndEnabled,
      transitionName = transitionName,
    )
  }

  /** gets a parcelable of the [TransformationLayout.Params]. */
  public fun getParcelableParams(): Parcelable {
    return getParams()
  }

  /** starts transforming the [TransformationLayout] into targetView with stopping container. */
  public fun startTransform(container: ViewGroup) {
    container.post {
      require(::targetView.isInitialized) {
        "You must set a targetView using bindTargetView() or transformation_targetView attribute." +
          "If you already set targetView, " +
          "check you use duplicated resource id to the TransformLayout."
      }
      if (!isTransformed && !isTransforming) {
        val now = SystemClock.elapsedRealtime()
        if (now - throttledTime >= duration) {
          throttledTime = now
          beginDelayingAndTransform(container, this, targetView)
        }
      }
    }
  }

  /** starts transforming the [TransformationLayout] into targetView with delaying. */
  public fun startTransformWithDelay(container: ViewGroup, delay: Long) {
    postDelayed(
      {
        startTransform(container)
      },
      delay,
    )
  }

  /** starts transforming the [TransformationLayout] into targetView with stopping parent. */
  public fun startTransform() {
    startTransform(parent as ViewGroup)
  }

  /** starts transforming the [TransformationLayout] into targetView with stopping parent with delaying. */
  public fun startTransformWithDelay(delay: Long) {
    startTransformWithDelay(parent as ViewGroup, delay)
  }

  /** transforming the targetView into [TransformationLayout]. */
  public fun finishTransform(container: ViewGroup) {
    container.post {
      require(::targetView.isInitialized) {
        "You must set a targetView using bindTargetView() or transformation_targetView attribute." +
          "If you already set targetView, " +
          "check you use duplicated resource id to the TransformLayout."
      }
      if (isTransformed && !isTransforming) {
        beginDelayingAndTransform(container, targetView, this)
      }
    }
  }

  /** transforming the targetView into [TransformationLayout] with delaying. */
  public fun finishTransformWithDelay(container: ViewGroup, delay: Long) {
    postDelayed(
      {
        finishTransform(container)
      },
      delay,
    )
  }

  /** transforming the targetView into [TransformationLayout] with stopping parent. */
  public fun finishTransform() {
    finishTransform(parent as ViewGroup)
  }

  /** transforming the targetView into [TransformationLayout] with stopping parent with delaying.. */
  public fun finishTransformWithDelay(delay: Long) {
    finishTransformWithDelay(parent as ViewGroup, delay)
  }

  private fun beginDelayingAndTransform(container: ViewGroup, mStartView: View, mEndView: View) {
    isTransforming = true
    mStartView.visible(false)
    mEndView.visible(true)
    TransitionManager.beginDelayedTransition(container, getTransform(mStartView, mEndView))
  }

  private fun getTransform(mStartView: View, mEndView: View): MaterialContainerTransform {
    return MaterialContainerTransform().apply {
      startView = mStartView
      endView = mEndView
      addTarget(mEndView)
      duration = this@TransformationLayout.duration
      pathMotion = this@TransformationLayout.pathMotion.getPathMotion()
      drawingViewId = this@TransformationLayout.zOrder
      containerColor = this@TransformationLayout.containerColor
      setAllContainerColors(this@TransformationLayout.allContainerColors)
      scrimColor = this@TransformationLayout.scrimColor
      transitionDirection = this@TransformationLayout.direction.value
      fadeMode = this@TransformationLayout.fadeMode.value
      fitMode = this@TransformationLayout.fitMode.value
      startElevation = this@TransformationLayout.startElevation
      endElevation = this@TransformationLayout.endElevation
      isElevationShadowEnabled = this@TransformationLayout.elevationShadowEnabled
      isHoldAtEndEnabled = this@TransformationLayout.holdAtEndEnabled
      addListener(
        object : Transition.TransitionListener {
          override fun onTransitionPause(p0: Transition?) = Unit
          override fun onTransitionStart(p0: Transition?) = Unit
          override fun onTransitionResume(p0: Transition?) = Unit
          override fun onTransitionCancel(p0: Transition?) = onFinishTransformation()
          override fun onTransitionEnd(p0: Transition?) {
            onFinishTransformation()
            onTransformFinishListener?.run { onFinish(isTransformed) }
          }
        },
      )
    }
  }

  private fun onFinishTransformation() {
    isTransformed = !isTransformed
    isTransforming = false
  }

  @Parcelize
  public data class Params(
    override var duration: Long,
    override var pathMotion: Motion,
    override var zOrder: Int,
    override var containerColor: Int,
    override var allContainerColors: Int,
    override var scrimColor: Int,
    override var direction: Direction,
    override var fadeMode: FadeMode,
    override var fitMode: FitMode,
    override var startElevation: Float,
    override var endElevation: Float,
    override var elevationShadowEnabled: Boolean,
    override var holdAtEndEnabled: Boolean,
    var transitionName: String,
  ) : Parcelable, TransformationParams

  /** The [Direction] to be used by this transform. */
  public enum class Direction(public val value: Int) {
    /**
     * Indicates that this transition should use automatic detection to determine whether it is an
     * Enter or a Return. If the end container has a larger area than the start container then it is
     * considered an Enter transition, otherwise it is a Return transition.
     */
    AUTO(MaterialContainerTransform.TRANSITION_DIRECTION_AUTO),

    /** Indicates that this is an Enter transition, i.e., when elements are entering the scene. */
    ENTER(MaterialContainerTransform.TRANSITION_DIRECTION_ENTER),

    /** Indicates that this is a Return transition, i.e., when elements are exiting the scene. */
    RETURN(MaterialContainerTransform.TRANSITION_DIRECTION_RETURN),
  }

  /** The [FadeMode] to be used to swap the content of the start View with that of the end View. */
  public enum class FadeMode(public val value: Int) {
    /**
     * Indicates that this transition should only fade in the incoming content, without changing the
     * opacity of the outgoing content.
     */
    IN(MaterialContainerTransform.FADE_MODE_IN),

    /**
     * Indicates that this transition should only fade out the outgoing content, without changing the
     * opacity of the incoming content.
     */
    OUT(MaterialContainerTransform.FADE_MODE_OUT),

    /** Indicates that this transition should cross fade the outgoing and incoming content. */
    CROSS(MaterialContainerTransform.FADE_MODE_CROSS),

    /**
     * Indicates that this transition should sequentially fade out the outgoing content and fade in
     * the incoming content.
     */
    THROUGH(MaterialContainerTransform.FADE_MODE_THROUGH),
  }

  /** The [FitMode] to be used when scaling the incoming content of the end View. */
  public enum class FitMode(public val value: Int) {
    /**
     * Indicates that this transition should automatically choose whether to use {@link
     * #FIT_MODE_WIDTH} or {@link #FIT_MODE_HEIGHT}.
     */
    AUTO(MaterialContainerTransform.FIT_MODE_AUTO),

    /**
     * Indicates that this transition should fit the incoming content to the width of the outgoing
     * content during the scale animation.
     */
    WIDTH(MaterialContainerTransform.FIT_MODE_WIDTH),

    /**
     * Indicates that this transition should fit the incoming content to the height of the outgoing
     * content during the scale animation.
     */
    HEIGHT(MaterialContainerTransform.FIT_MODE_HEIGHT),
  }

  public enum class Motion(private val value: Int) {
    /** Indicates that this transition should be drawn as the arc path. */
    ARC(0),

    /** Indicates that this transition should be drawn as the linear path. */
    LINEAR(1),
    ;

    public fun getPathMotion(): PathMotion? {
      if (value == 0) return MaterialArcMotion()
      return null
    }
  }

  /** Builder class for creating a new instance of the [TransformationLayout]. */
  public class Builder(context: Context) {
    private val transformationLayout = TransformationLayout(context)

    public fun setDuration(value: Long): Builder = apply { transformationLayout.duration = value }
    public fun setPathMode(value: Motion): Builder =
      apply { transformationLayout.pathMotion = value }

    public fun setZOrder(value: Int): Builder = apply { transformationLayout.zOrder = value }
    public fun setContainerColor(@ColorInt value: Int): Builder = apply {
      transformationLayout.containerColor = value
    }

    public fun setScrimColor(@ColorInt value: Int): Builder =
      apply { transformationLayout.scrimColor = value }

    public fun setDirection(value: Direction): Builder =
      apply { transformationLayout.direction = value }

    public fun setFadeMode(value: FadeMode): Builder =
      apply { transformationLayout.fadeMode = value }

    public fun setFitMode(value: FitMode): Builder = apply { transformationLayout.fitMode = value }
    public fun setOnTransformFinishListener(value: OnTransformFinishListener): Builder = apply {
      transformationLayout.onTransformFinishListener = value
    }

    @JvmSynthetic
    public fun setOnTransformFinishListener(action: (Boolean) -> Unit) {
      transformationLayout.onTransformFinishListener = OnTransformFinishListener { action(it) }
    }

    public fun build(): TransformationLayout = transformationLayout
  }
}
