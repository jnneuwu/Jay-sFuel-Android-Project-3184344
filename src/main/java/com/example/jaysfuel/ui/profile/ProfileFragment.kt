package com.example.jaysfuel.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.jaysfuel.R
import com.example.jaysfuel.model.UserManager
import com.example.jaysfuel.theme.ThemeManager

/**
 * Profile screen.
 * Uses ThemeManager for day/night colors.
 */
class ProfileFragment : Fragment() {

    private lateinit var rootLayout: View
    private lateinit var headerCard: View
    private lateinit var pointsCard: View
    private lateinit var redeemedCard: View

    private lateinit var profileNameTextView: TextView
    private lateinit var profileSubtitleTextView: TextView
    private lateinit var pointsTitleTextView: TextView
    private lateinit var pointsValueTextView: TextView
    private lateinit var redeemedTitleTextView: TextView
    private lateinit var redeemedContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootLayout = view.findViewById(R.id.profile_root)
        headerCard = view.findViewById(R.id.profile_header_card)
        pointsCard = view.findViewById(R.id.profile_points_card)
        redeemedCard = view.findViewById(R.id.profile_redeemed_card)

        profileNameTextView = view.findViewById(R.id.tvProfileName)
        profileSubtitleTextView = view.findViewById(R.id.tvProfileSubtitle)
        pointsTitleTextView = view.findViewById(R.id.tvProfilePointsTitle)
        pointsValueTextView = view.findViewById(R.id.tvProfilePointsValue)
        redeemedTitleTextView = view.findViewById(R.id.tvRedeemedTitle)
        redeemedContainer = view.findViewById(R.id.layoutRedeemedList)

        updatePoints()
        renderRedeemedRewards()
        applyThemeToViews()
    }

    override fun onResume() {
        super.onResume()
        updatePoints()
        renderRedeemedRewards()
        applyThemeToViews()
    }

    private fun updatePoints() {
        pointsValueTextView.text = "${UserManager.points} pts"
    }

    private fun renderRedeemedRewards() {
        redeemedContainer.removeAllViews()

        if (UserManager.redeemedRewards.isEmpty()) {
            val emptyView = TextView(requireContext())
            emptyView.text = getString(R.string.no_redeemed_rewards)
            emptyView.textSize = 14f
            redeemedContainer.addView(emptyView)
            return
        }

        val inflater = LayoutInflater.from(requireContext())

        UserManager.redeemedRewards.forEach { reward ->
            val itemView = inflater.inflate(
                R.layout.item_redeemed_reward,
                redeemedContainer,
                false
            )

            val nameText: TextView = itemView.findViewById(R.id.tvRedeemedName)
            val detailText: TextView = itemView.findViewById(R.id.tvRedeemedDetail)

            nameText.text = reward.name
            detailText.text = "${reward.category} • ${reward.pointsCost} pts"

            // Apply theme to text inside the list item
            ThemeManager.applyPrimaryText(nameText)
            ThemeManager.applySecondaryText(detailText)

            redeemedContainer.addView(itemView)
        }
    }

    private fun applyThemeToViews() {
        ThemeManager.applyBackground(rootLayout)
        ThemeManager.applyCardBackground(headerCard, pointsCard, redeemedCard)

        ThemeManager.applyPrimaryText(
            profileNameTextView,
            pointsTitleTextView,
            redeemedTitleTextView
        )

        ThemeManager.applySecondaryText(
            profileSubtitleTextView
        )

        ThemeManager.applyAccentText(pointsValueTextView)
    }
}
