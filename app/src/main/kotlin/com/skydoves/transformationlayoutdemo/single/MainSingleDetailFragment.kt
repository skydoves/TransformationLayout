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

package com.skydoves.transformationlayoutdemo.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.skydoves.transformationlayoutdemo.databinding.ActivityDetailBinding
import com.skydoves.transformationlayoutdemo.recycler.Poster

class MainSingleDetailFragment : Fragment() {

  private var _binding: ActivityDetailBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = ActivityDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // [Step1]: apply onTransformationEndContainer using TransformationLayout.Params.
    val params = arguments?.getParcelable<TransformationLayout.Params>(paramsKey)
    onTransformationEndContainer(params)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val poster = arguments?.getParcelable<Poster>(posterKey)
    poster?.let {
      // [Step2]: sets a transition name to the target view.
      binding.detailContainer.transitionName = poster.name

      Glide.with(this)
        .load(it.poster)
        .into(binding.profileDetailBackground)
      binding.detailTitle.text = it.name
      binding.detailDescription.text = it.description
    }
  }

  companion object {
    const val TAG = "LibraryFragment"
    const val posterKey = "posterKey"
    const val paramsKey = "paramsKey"
  }
}
