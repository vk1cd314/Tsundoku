package com.tsunderead.tsundoku.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ActivityMainBinding
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.manga_card_cell.mangaList
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val libraryDBHandler = LibraryDBHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addToDatabase()
        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, _, _ ->
            showBottomNav(bottomNavigationView, true)
        }
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun addToDatabase() {
        val manga1 = Manga("https://uploads.mangadex.org/covers/4265c437-7d57-4d31-9b1d-0e574a07b7b7/3ae9eed7-b8a7-47b4-945e-09fd55e267ec.jpg",
            "Nisio Isin", "Bakemonogatari", "4265c437-7d57-4d31-9b1d-0e574a07b7b7")
        val manga2 = Manga("https://uploads.mangadex.org/covers/3316f5cb-c828-4ad4-b350-5bb474da9542/159fe55f-260a-4597-9dbf-8ae06e786b29.jpg",
            "Arakawa Hiromu", "Silver Spoon", "3316f5cb-c828-4ad4-b350-5bb474da9542")
        val manga3 = Manga("https://uploads.mangadex.org/covers/801513ba-a712-498c-8f57-cae55b38cc92/2a61abcb-8e6e-460d-8551-1caa93e09e39.jpg",
            "Kentaro Miura", "Berserk", "801513ba-a712-498c-8f57-cae55b38cc92")
//        libraryDBHandler.insertManga(manga1)
//        libraryDBHandler.insertManga(manga2)
//        libraryDBHandler.insertManga(manga3)
//        libraryDBHandler.close()
    }

    private fun showBottomNav(bottomNavigationView: BottomNavigationView, isVisible: Boolean) {
        val layoutParams: ViewGroup.LayoutParams = bottomNavigationView.layoutParams
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            val behavior = layoutParams.behavior
            if (behavior is HideBottomViewOnScrollBehavior) {
                if (isVisible) {
                    behavior.slideUp(bottomNavigationView)
                } else {
                    behavior.slideDown(bottomNavigationView)
                }
            }
        }
    }
}
