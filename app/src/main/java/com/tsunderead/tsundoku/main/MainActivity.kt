package com.tsunderead.tsundoku.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ActivityMainBinding
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.manga_card_cell.mangaList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var library: Fragment = Library()
    private var updates: Fragment = Updates()
    private var search: Fragment = Search()
    private var history: Fragment = History()
    private var settings: Fragment = Settings()
    private var activeFragment: Fragment = library
    private var fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager.beginTransaction().add(R.id.frame_layout, updates).hide(updates).commit()
        fragmentManager.beginTransaction().add(R.id.frame_layout, search).hide(search).commit()
        fragmentManager.beginTransaction().add(R.id.frame_layout, history).hide(history).commit()
        fragmentManager.beginTransaction().add(R.id.frame_layout, settings).hide(settings).commit()
        fragmentManager.beginTransaction().add(R.id.frame_layout, library).commit()
//        replaceFragment(Library())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.library -> {
                    replaceFragment(Library())
                }
                R.id.updates -> {
                    replaceFragment(Updates())
                }
                R.id.search -> {
                    replaceFragment(Search())
                }
                R.id.history -> {
                    replaceFragment(History())
                }
                R.id.settings -> {
                    replaceFragment(Settings())
                }

                else ->{
                }
            }
            true
        }
//        populateLibrary()
    }

    private fun replaceFragment(fragment: Fragment){
        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
        Log.i("wtf", "changed")
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        fragmentTransaction.commit()
    }
}