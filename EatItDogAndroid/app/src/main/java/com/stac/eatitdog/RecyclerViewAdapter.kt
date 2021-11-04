package com.stac.eatitdog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerViewAdapter() : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> {
                Glide.with(holder.itemView).load(R.drawable.green).into(holder.imageView)
                holder.titleTextView.text = "초록동물병원"
                holder.walkingTimeTextView.text = "이동 시간: 약 1분"
                holder.contactTextView.text = "연락처: 053-638-1558"
                holder.addressTextView.text = "대구광역시 달서구 진천동 595-26"
            }
            1 -> {
                Glide.with(holder.itemView).load(R.drawable.hospital2).into(holder.imageView)
                holder.titleTextView.text = "어진종합동물병원"
                holder.walkingTimeTextView.text = "이동 시간: 약 3분"
                holder.contactTextView.text = "연락처: 053-638-8274"
                holder.addressTextView.text = "대구광역시 달서구 상인동 255-2"
            }
            2 -> {
                Glide.with(holder.itemView).load(R.drawable.hospital3).into(holder.imageView)
                holder.titleTextView.text = "해맑은동물병원"
                holder.walkingTimeTextView.text = "이동 시간: 약 10분"
                holder.contactTextView.text = "연락처: 053-638-8811"
                holder.addressTextView.text = "대구광역시 달서구 진천동 508"
            }
            3 -> {
                Glide.with(holder.itemView).load(R.drawable.hospital4).into(holder.imageView)
                holder.titleTextView.text = "비슬동물병원"
                holder.walkingTimeTextView.text = "이동 시간: 약 7분"
                holder.contactTextView.text = "연락처: 053-638-2480"
                holder.addressTextView.text = "대구광역시 달서구 상인동 155-2"
            }
            4 -> {
                Glide.with(holder.itemView).load(R.drawable.hospital4).into(holder.imageView)
                holder.titleTextView.text = "니얼굴동물병원"
                holder.walkingTimeTextView.text = "이동 시간: 약 999분"
                holder.contactTextView.text = "연락처: 053-638-2480"
                holder.addressTextView.text = "대구광역시 달서구 상인동 141-2"
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val walkingTimeTextView: TextView
        val contactTextView: TextView
        val addressTextView: TextView
        val imageView: ImageView

        init {
            titleTextView = view.findViewById(R.id.title_textView)
            walkingTimeTextView = view.findViewById(R.id.walking_time_textView)
            contactTextView = view.findViewById(R.id.contact_textView)
            addressTextView = view.findViewById(R.id.address_textView)
            imageView = view.findViewById(R.id.imageView)
        }

    }
}