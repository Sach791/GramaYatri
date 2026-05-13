package com.example.gramayatri

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var database: DatabaseReference

    // ✅ Real Village Names
    val route = listOf(
        "Banashankari",
        "SouthEnd Circle",
        "Bit College",
        "KR Market",
        "Majestic Bus Stand"
    )

    val times = listOf(15, 15, 5,12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.stopSpinner)
        val button = findViewById<Button>(R.id.pingButton)
        val currentLocation = findViewById<TextView>(R.id.currentLocation)
        val etaText = findViewById<TextView>(R.id.etaText)

        // Firebase reference
        database = FirebaseDatabase.getInstance().getReference("live")

        // Spinner setup
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, route)
        spinner.adapter = adapter

        // 🔘 Button Click → Send Data
        button.setOnClickListener {
            val selectedStop = spinner.selectedItem.toString()

            val data = mapOf(
                "current_stop" to selectedStop,
                "timestamp" to System.currentTimeMillis()
            )

            database.setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Ping Sent!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error sending data", Toast.LENGTH_SHORT).show()
                }
        }

        // 📡 Firebase Listener
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    etaText.text = "No data yet"
                    return
                }

                val stop = snapshot.child("current_stop").value?.toString()

                if (stop.isNullOrEmpty()) {
                    etaText.text = "Waiting for update..."
                    return
                }

                currentLocation.text = "Current Location: $stop"

                val currentIndex = route.indexOf(stop)

                // ✅ Safety check
                if (currentIndex == -1) {
                    etaText.text = "Invalid data"
                    return
                }

                var etaDisplay = ""
                var totalTime = 0

                for (i in currentIndex until route.size - 1) {
                    totalTime += times[i]
                    etaDisplay += "${route[i + 1]} → $totalTime mins\n"
                }

                etaText.text = etaDisplay
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}