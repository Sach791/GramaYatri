package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class BusListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_list)

        val listView = findViewById<ListView>(R.id.busListView)

        val buses = listOf(
            "🚌 City Express",
            "🚍 Village Link",
            "🚐 Metro Connect"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            buses
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->

            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra("busIndex", position)

            startActivity(intent)
        }
    }
}