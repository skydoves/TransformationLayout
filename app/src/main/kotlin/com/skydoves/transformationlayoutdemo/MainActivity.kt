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
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.skydoves.transformationlayout.onTransformationStartContainer
import com.skydoves.transformationlayoutdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    onTransformationStartContainer()
    super.onCreate(savedInstanceState)

    val binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    with(binding.mainViewpager) {
      adapter = MainPagerAdapter(supportFragmentManager)
      offscreenPageLimit = 3
      addOnPageChangeListener(
        object : ViewPager.OnPageChangeListener {
          override fun onPageScrollStateChanged(state: Int) = Unit
          override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int,
          ) = Unit

          override fun onPageSelected(position: Int) {
            binding.mainBottomNavigation.menu.getItem(position).isChecked = true
          }
        },
      )
    }

    binding.mainBottomNavigation.setOnItemSelectedListener {
      when (it.itemId) {
        R.id.action_one -> binding.mainViewpager.currentItem = 0
        R.id.action_two -> binding.mainViewpager.currentItem = 1
        R.id.action_three -> binding.mainViewpager.currentItem = 2
      }
      true
    }
  }
}
