package com.tsunderead.tsundoku.main

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ActivityMainBinding
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.manga_card_cell.mangaList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Library())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.library -> replaceFragment(Library())
                R.id.updates -> replaceFragment(Updates())
                R.id.search -> replaceFragment(Search())
                R.id.history -> replaceFragment(History())
                R.id.settings -> replaceFragment(Settings())

                else ->{
                }
            }
            true
        }
        populateLibrary()
    }

    private fun populateLibrary() {
        val book1 = Manga("https:\\/\\/uploads.mangadex.org\\/covers\\/f9b82990-7198-4131-84bb-c952830f5ea7\\/6754b3ba-a9cd-4f07-89a5-ff4145f24605.jpg", "Nisioisin", "Bakemonogatari", "1")
        mangaList.add(book1)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}