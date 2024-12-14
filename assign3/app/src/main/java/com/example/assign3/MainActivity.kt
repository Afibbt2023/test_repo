package com.example.assign3

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var meetings: MutableList<Meeting> = mutableListOf()
    private lateinit var adapter: MeetingAdapter
    private val allTypes: Array<String> = arrayOf("Cultural", "Tech", "Politics", "Sport")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvMeeting = findViewById<RecyclerView>(R.id.rvMeetings)
        val resources =
            applicationContext.resources
        meetings = readAndParseCSV(resources)
        sortMeetingsByDateTimeAscending(meetings)
        adapter = MeetingAdapter(meetings)


        // Sort button actions
        val sortButton = findViewById<Button>(R.id.btSort)

        // Create a pop-up menu
        val popupMenu = PopupMenu(this, sortButton)
        popupMenu.menu.add(0, R.id.show_all, 0, "SHOW ALL")
        popupMenu.menu.add(0, R.id.filter_by_group, 0, "Filter by Group")
        popupMenu.menu.add(0, R.id.filter_by_location, 0, "Filter by Location")

        var currentType = 0
        var isOnline = true

        // Set click listener for the sort button
        sortButton.setOnClickListener {
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.show_all -> {
                        // show all elements of meetings
                        adapter.setData(meetings)
                        true
                    }

                    R.id.filter_by_group -> {
                        // Handle Filter by Group action

                        val filteredMeetings = meetings.filter { it.type == allTypes[currentType] }
                        adapter.setData(filteredMeetings) // Update the adapter with filtered data

                        currentType++
                        if (currentType > 3) currentType = 0

                        true
                    }

                    R.id.filter_by_location -> {
                        // Handle Filter by Type action

                        val (onlineMeetings, offlineMeetings) = meetings.partition { it.location == "Online" }
                        val filteredMeetings = if (isOnline) onlineMeetings else offlineMeetings
                        adapter.setData(filteredMeetings) // Update the adapter with filtered data

                        isOnline = !isOnline

                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

        rvMeeting.layoutManager = LinearLayoutManager(this)
        rvMeeting.adapter = adapter
    }

    private fun readAndParseCSV(resources: Resources): MutableList<Meeting> {
        val meetingList = mutableListOf<Meeting>()

        try {
            val inputStream = resources.openRawResource(R.raw.groups)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            reader.readLine() // Skip the header line if it exists

            while (reader.readLine().also { line = it } != null) {
                val parts = line?.split(",") ?: continue
                if (parts.size >= 5) {
                    val id = parts[0].toInt()
                    val group = parts[1]
                    val location = parts[2]
                    val type = parts[3]
                    val datetime = parts[4]

                    val meeting = Meeting(id, group, location, type, datetime)
                    meetingList.add(meeting)
                }
            }

            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return meetingList
    }

}

fun sortMeetingsByDateTimeAscending(meetings: MutableList<Meeting>) {
    meetings.sortWith(compareBy { meeting ->
        val dateTimeString = meeting.datetime
        val dateTimeFormat = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm", Locale.US)
        val dateTime = LocalDateTime.parse(dateTimeString, dateTimeFormat)
        dateTime
    })
}