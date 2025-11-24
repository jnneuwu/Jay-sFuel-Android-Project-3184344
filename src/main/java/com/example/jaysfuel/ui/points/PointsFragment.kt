package com.example.jaysfuel.ui.points

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jaysfuel.R
import com.example.jaysfuel.adapter.RewardAdapter
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.model.UserManager
import com.example.jaysfuel.theme.ThemeManager

/**
 * Points / rewards screen.
 * Uses ThemeManager to apply day/night colors.
 */
class PointsFragment : Fragment() {

    private lateinit var rootLayout: View
    private lateinit var pointsHeaderCard: View
    private lateinit var rewardsCard: View

    private lateinit var pointsTitleTextView: TextView
    private lateinit var pointsValueTextView: TextView
    private lateinit var rewardsSectionTitleTextView: TextView
    private lateinit var rewardsRecyclerView: RecyclerView
    private lateinit var rewardAdapter: RewardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_points, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootLayout = view.findViewById(R.id.points_root)
        pointsHeaderCard = view.findViewById(R.id.points_header_card)
        rewardsCard = view.findViewById(R.id.points_rewards_card)

        pointsTitleTextView = view.findViewById(R.id.tvPointsTitle)
        pointsValueTextView = view.findViewById(R.id.tvPointsValue)
        rewardsSectionTitleTextView = view.findViewById(R.id.tvRewardsSectionTitle)
        rewardsRecyclerView = view.findViewById(R.id.rvRewards)

        updatePointsText()

        val rewardItems = createRewardItems()
        rewardAdapter = RewardAdapter(rewardItems) { item ->
            onRedeemClicked(item)
        }

        rewardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rewardsRecyclerView.adapter = rewardAdapter

        applyThemeToViews()
    }

    override fun onResume() {
        super.onResume()
        applyThemeToViews()
        updatePointsText()
    }

    private fun createRewardItems(): List<RewardItem> {
        return listOf(
            RewardItem(
                id = 1,
                name = "€5 Fuel Coupon",
                description = "Save €5 on your next fuel purchase.",
                pointsCost = 500,
                imageResId = R.drawable.ic_gas,
                category = "Fuel"
            ),
            RewardItem(
                id = 2,
                name = "€10 Fuel Coupon",
                description = "Save €10 on fuel at participating stations.",
                pointsCost = 900,
                imageResId = R.drawable.ic_gas,
                category = "Fuel"
            ),
            RewardItem(
                id = 3,
                name = "Free Coffee",
                description = "One free hot coffee at the shop.",
                pointsCost = 200,
                imageResId = R.drawable.ic_gift,
                category = "Drink"
            ),
            RewardItem(
                id = 4,
                name = "Cold Drink Combo",
                description = "Soft drink + small snack.",
                pointsCost = 350,
                imageResId = R.drawable.ic_gift,
                category = "Drink"
            ),
            RewardItem(
                id = 5,
                name = "Car Wash Voucher",
                description = "Standard car wash at the station.",
                pointsCost = 600,
                imageResId = R.drawable.ic_navigation,
                category = "Service"
            ),
            RewardItem(
                id = 6,
                name = "Snack Pack",
                description = "Chips and chocolate bar bundle.",
                pointsCost = 300,
                imageResId = R.drawable.ic_star,
                category = "Snack"
            ),
            RewardItem(
                id = 7,
                name = "Premium Fuel Upgrade",
                description = "Upgrade to premium fuel for one fill.",
                pointsCost = 750,
                imageResId = R.drawable.ic_star,
                category = "Fuel"
            ),
            RewardItem(
                id = 8,
                name = "Energy Drink",
                description = "One free energy drink can.",
                pointsCost = 250,
                imageResId = R.drawable.ic_gift,
                category = "Drink"
            )
        )
    }

    private fun onRedeemClicked(item: RewardItem) {
        val context = requireContext()
        val success = UserManager.redeem(item)

        if (success) {
            Toast.makeText(
                context,
                getString(R.string.redeem_success),
                Toast.LENGTH_SHORT
            ).show()
            updatePointsText()
        } else {
            Toast.makeText(
                context,
                getString(R.string.redeem_fail_not_enough),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updatePointsText() {
        pointsValueTextView.text = "${UserManager.points} pts"
    }

    private fun applyThemeToViews() {
        ThemeManager.applyBackground(rootLayout)
        ThemeManager.applyCardBackground(pointsHeaderCard, rewardsCard)

        ThemeManager.applyPrimaryText(
            pointsTitleTextView,
            rewardsSectionTitleTextView
        )

        // Points number is accent color
        ThemeManager.applyAccentText(pointsValueTextView)
    }
}
