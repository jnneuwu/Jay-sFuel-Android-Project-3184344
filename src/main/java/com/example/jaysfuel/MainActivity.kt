package com.example.jaysfuel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 找到 XML 里的 NavHostFragment
        // Find NavHostFragment from FragmentContainerView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)

        // 设置底部四个按钮的点击逻辑
        // Handle bottom navigation item clicks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // 首页 / 油价
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }

                // 积分
                R.id.navigation_points -> {
                    navController.navigate(R.id.navigation_points)
                    true
                }

                // 用户
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }

                // 导航到加油站：单独打开 Activity，不走 nav_graph
                R.id.navigation_station -> {
                    val intent = Intent(this, GasStationActivity::class.java)
                    startActivity(intent)
                    false   // 返回 false：不把这个 tab 设为选中
                }

                else -> false
            }
        }

        // 默认选中首页
        // Default selected tab is home
        bottomNav.selectedItemId = R.id.navigation_home
    }
}
