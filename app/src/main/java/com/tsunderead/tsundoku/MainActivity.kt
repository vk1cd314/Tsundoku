package com.tsunderead.tsundoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.ActivityMainBinding

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
        val book1 = Manga(R.drawable.bakemonogatari, "Nisioisin", "Bakemonogatari")
        mangaList.add(book1)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}