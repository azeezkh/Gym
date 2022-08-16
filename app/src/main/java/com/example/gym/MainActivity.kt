package com.example.gym

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image_view_1 = findViewById(R.id.musicON) as ImageView
        val image_view_2 = findViewById(R.id.musicOFF) as ImageView


        image_view_1.setOnClickListener() {
            startService(Intent(this, MusicService::class.java))
        }

        image_view_2.setOnClickListener() {
            stopService(Intent(this, MusicService::class.java))
        }

        val button1: Button = findViewById(R.id.buttonR)
        val button2: Button = findViewById(R.id.buttonE)

        button1.setOnClickListener {
            val intent = Intent(this, MemberActivity::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var dialog_var = InfoDialogClass()
        when (item.itemId) {
            R.id.item1 -> dialog_var.show(supportFragmentManager, "Prices Information Dialog")
        }
        return true;
    }

}