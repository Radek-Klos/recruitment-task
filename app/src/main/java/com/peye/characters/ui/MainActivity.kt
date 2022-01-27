package com.peye.characters.ui

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.peye.characters.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigation()
    }

    private fun initNavigation() {
        findNavHostFragment().navController.setGraph(MAIN_NAVIGATION_GRAPH_ID)
    }

    private fun findNavHostFragment(): NavHostFragment =
        supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment

    companion object {
        @NavigationRes
        private const val MAIN_NAVIGATION_GRAPH_ID = R.navigation.main
    }
}
