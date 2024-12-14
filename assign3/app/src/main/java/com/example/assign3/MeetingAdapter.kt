package com.example.assign3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MeetingAdapter(private var meetingList: List<Meeting>) :
    RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_meeting, parent, false)
        return MeetingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetingList[position]

        holder.groupTextView.text = meeting.group
        holder.locationTextView.text = meeting.location
        if (meeting.location == "Online") holder.ivOnline.setImageResource(R.drawable.onlinemeeting)
        else holder.ivOnline.setImageResource(R.drawable.offlinemeeting)
        holder.typeTextView.text = meeting.type
        holder.datetimeTextView.text = meeting.datetime
    }

    override fun getItemCount(): Int {
        return meetingList.size
    }

    inner class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupTextView: TextView = itemView.findViewById(R.id.tvGroup)
        val locationTextView: TextView = itemView.findViewById(R.id.tvLocation)
        val typeTextView: TextView = itemView.findViewById(R.id.tvType)
        val ivOnline: ImageView = itemView.findViewById(R.id.ivOnline)
        val datetimeTextView: TextView = itemView.findViewById(R.id.tvDate)
    }

    // Add a method to update the dataset
    fun setData(newData: List<Meeting>) {
        meetingList = newData
        notifyDataSetChanged()
    }
}