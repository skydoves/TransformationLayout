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

package com.skydoves.transformationlayout

import android.graphics.Color
import com.google.android.material.transition.MaterialContainerTransform
import java.io.Serializable

/** Interface definition about the [TransformationLayout]'s parameters.  */
internal interface TransformationParams : Serializable {
  var duration: Long
  var pathMotion: TransformationLayout.Motion
  var zOrder: Int
  var containerColor: Int
  var scrimColor: Int
  var direction: TransformationLayout.Direction
  var fadeMode: TransformationLayout.FadeMode
  var fitMode: TransformationLayout.FitMode
}

internal object DefaultParamValues : TransformationParams {
  override var pathMotion: TransformationLayout.Motion = TransformationLayout.Motion.ARC
  override var duration = 450L
  override var zOrder: Int = android.R.id.content
  override var containerColor: Int = Color.TRANSPARENT
  override var scrimColor: Int = Color.TRANSPARENT
  override var direction: TransformationLayout.Direction = TransformationLayout.Direction.AUTO
  override var fadeMode: TransformationLayout.FadeMode = TransformationLayout.FadeMode.IN
  override var fitMode: TransformationLayout.FitMode = TransformationLayout.FitMode.AUTO
}

internal fun TransformationParams.getMaterialContainerTransform(): MaterialContainerTransform {
  val params = this
  return MaterialContainerTransform().apply {
    addTarget(android.R.id.content)
    duration = params.duration
    pathMotion = params.pathMotion.getPathMotion()
    drawingViewId = params.zOrder
    containerColor = params.containerColor
    scrimColor = params.scrimColor
    transitionDirection = params.direction.value
    fadeMode = params.fadeMode.value
    fitMode = params.fitMode.value
  }
}

internal fun TransformationParams.getMaterialFragmentTransform(): MaterialContainerTransform {
  val params = this
  return MaterialContainerTransform().apply {
    duration = params.duration
    pathMotion = params.pathMotion.getPathMotion()
    drawingViewId = params.zOrder
    containerColor = params.containerColor
    scrimColor = params.scrimColor
    transitionDirection = params.direction.value
    fadeMode = params.fadeMode.value
    fitMode = params.fitMode.value
  }
}
