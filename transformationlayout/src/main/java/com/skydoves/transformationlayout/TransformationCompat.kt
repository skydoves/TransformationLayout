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

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat

/** Helper for accessing features in starting activities with transformation animation. */
object TransformationCompat {

  /** A common definition of the activity's transition name. */
  internal const val activityTransitionName: String = "com.skydoves.transformationlayout"

  /** Invalidate the activity's options menu, if able. */
  fun startActivity(
    transformationLayout: TransformationLayout,
    intent: Intent
  ) {
    transformationLayout.startActivityWithBundleOptions(intent) { workedIntent, bundle ->
      ActivityCompat.startActivity(transformationLayout.context, workedIntent, bundle)
    }
  }

  /**
   * Start new activity with options, if able, for which you would like a
   * result when it finished.
   */
  fun startActivityForResult(
    transformationLayout: TransformationLayout,
    intent: Intent,
    requestCode: Int
  ) {
    val activity = transformationLayout.context.getActivity()
    if (activity != null) {
      transformationLayout.startActivityWithBundleOptions(intent) { workedIntent, bundle ->
        ActivityCompat.startActivityForResult(activity, workedIntent, requestCode, bundle)
      }
    }
  }

  private inline fun TransformationLayout.startActivityWithBundleOptions(
    intent: Intent,
    block: (Intent, Bundle) -> Unit
  ) {
    val now = System.currentTimeMillis()
    if (now - throttledTime > duration) {
      throttledTime = now
      val bundle = withView(this, activityTransitionName)
      intent.putExtra(activityTransitionName, getParcelableParams())
      block(intent, bundle)
    }
  }
}
