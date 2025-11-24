package com.example.jaysfuel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jaysfuel.R
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.theme.ThemeManager

/**
 * RecyclerView adapter for the rewards list.
 * Uses ThemeManager for card and text colors.
 */
class RewardAdapter(
    private val items: List<RewardItem>,
    private val onRedeemClicked: (RewardItem) -> Unit
) : RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {

    class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootCard: View = itemView.findViewById(R.id.rootRewardCard)
        val image: ImageView = itemView.findViewById(R.id.ivRewardImage)
        val name: TextView = itemView.findViewById(R.id.tvRewardName)
        val description: TextView = itemView.findViewById(R.id.tvRewardDescription)
        val cost: TextView = itemView.findViewById(R.id.tvRewardCost)
        val redeemButton: Button = itemView.findViewById(R.id.btnRedeem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val item = items[position]

        holder.image.setImageResource(item.imageResId)
        holder.name.text = item.name
        holder.description.text = item.description
        holder.cost.text = "${item.pointsCost} pts"

        // Apply theme to this card
        ThemeManager.applyCardBackground(holder.rootCard)
        ThemeManager.applyPrimaryText(holder.name)
        ThemeManager.applySecondaryText(holder.description)
        ThemeManager.applyAccentText(holder.cost)

        holder.redeemButton.setOnClickListener {
            onRedeemClicked(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
