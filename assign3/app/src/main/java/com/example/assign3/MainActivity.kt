package com.example.assign3

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var meetings: MutableList<Meeting> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resources = applicationContext.resources
        meetings = readAndParseCSV(resources)
        sortMeetingsByDateTimeAscending(meetings)

        // Log the parsed meetings (for debugging purposes)
        meetings.forEach {
            println(it)
        }
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
        LocalDateTime.parse(dateTimeString, dateTimeFormat)
    })
}
