package com.ws.simplesegmentcalculator

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ws.simplesegmentcalculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // actionBar?.setHomeButtonEnabled(true)
        // add in conteiner MainFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance()).commit()
       // actionBar?.setHomeButtonEnabled(true)
    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

 */
    /*
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun onBackStackChanged() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }

     */
}