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

package com.skydoves.transformationlayoutdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.skydoves.transformationlayoutdemo.MockUtil.getMockPosters
import com.skydoves.transformationlayoutdemo.databinding.FragmentHomeBinding
import com.skydoves.transformationlayoutdemo.recycler.PosterAdapter
import com.skydoves.transformationlayoutdemo.recycler.PosterMenuAdapter

class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      recyclerView.adapter = PosterAdapter().apply { addPosterList(getMockPosters()) }
      recyclerViewMenu.adapter = PosterMenuAdapter().apply { addPosterList(getMockPosters()) }

      fab.setOnClickListener {
        if (!transformationLayout.isTransforming) {
          backgroundView.visibility = View.VISIBLE
        }
        transformationLayout.startTransform()
      }

      menuHome.setOnClickListener {
        if (!transformationLayout.isTransforming) {
          backgroundView.visibility = View.GONE
        }
        transformationLayout.finishTransform()
        Toast.makeText(context, "Compose New", Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
