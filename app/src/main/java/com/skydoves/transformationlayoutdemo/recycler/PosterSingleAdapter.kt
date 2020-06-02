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

package com.skydoves.transformationlayoutdemo.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayoutdemo.R
import kotlinx.android.synthetic.main.item_poster.view.*

class PosterSingleAdapter
constructor(
  private val delegate: PosterDelegate
) : RecyclerView.Adapter<PosterSingleAdapter.PosterViewHolder>() {

  private val items = mutableListOf<Poster>()
  private var previousTime = System.currentTimeMillis()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return PosterViewHolder(inflater.inflate(R.layout.item_poster, parent, false))
  }

  override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
    val item = items[position]
    holder.itemView.run {
      Glide.with(context)
        .load(item.poster)
        .into(item_poster_post)
      item_poster_title.text = item.name
      item_poster_running_time.text = item.playtime

      // sets a transition name to the transformation layout.
      // this code must not be in listener.
      item_poster_transformationLayout.transitionName = item.name

      setOnClickListener {
        val now = System.currentTimeMillis()
        if (previousTime - now >= item_poster_transformationLayout.duration) {
          delegate.onItemClick(item, item_poster_transformationLayout)
          previousTime = now
        }
      }
    }
  }

  fun addPosterList(list: List<Poster>) {
    items.clear()
    items.addAll(list)
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.size

  class PosterViewHolder(view: View) : RecyclerView.ViewHolder(view)

  interface PosterDelegate {
    fun onItemClick(poster: Poster, itemView: TransformationLayout)
  }
}
