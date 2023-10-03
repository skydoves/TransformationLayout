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

import android.app.Activity
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.transition.platform.Hold
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

/** sets an exit shared element callback to activity for implementing shared element transition. */
public fun Activity.onTransformationStartContainer() {
  window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
  setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
  window.sharedElementsUseOverlay = false
}

/** sets an enter shared element callback to activity for implementing shared element transition. */
public fun Activity.onTransformationEndContainer(
  params: TransformationLayout.Params?,
) {
  requireNotNull(
    params,
  ) { "TransformationLayout.Params must not be a null. check your intent key value is correct." }
  window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
  ViewCompat.setTransitionName(findViewById(android.R.id.content), params.transitionName)
  setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
  window.sharedElementEnterTransition = params.getMaterialContainerTransform()
  window.sharedElementReturnTransition = params.getMaterialContainerTransform()
}

/** sets an exit shared element callback to fragment for implementing shared element transition. */
public fun Fragment.onTransformationStartContainer() {
  exitTransition = Hold()
}

/** sets an enter shared element callback to fragment for implementing shared element transition. */
public fun Fragment.onTransformationEndContainer(
  params: TransformationLayout.Params?,
) {
  requireNotNull(
    params,
  ) { "TransformationLayout.Params must not be a null. check your intent key value is correct." }
  sharedElementEnterTransition = params.getMaterialFragmentTransform()
}

/** adds a shared element transformation to FragmentTransaction. */
@JvmOverloads
public fun FragmentTransaction.addTransformation(
  transformationLayout: TransformationLayout,
  transitionName: String? = null,
): FragmentTransaction {
  if (transitionName != null && transformationLayout.transitionName == null) {
    ViewCompat.setTransitionName(transformationLayout, transitionName)
  }
  addSharedElement(transformationLayout, transformationLayout.transitionName)
  return this
}
