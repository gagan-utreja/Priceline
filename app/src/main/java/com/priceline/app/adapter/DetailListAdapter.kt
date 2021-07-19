package com.priceline.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priceline.app.R
import com.priceline.app.model.BookListDetailModel
import java.util.*

/**
 *  Book Details Adapter to show the title, author, price etc
 */
class DetailListAdapter(private var items: List<BookListDetailModel>,
                        private val listener: (BookListDetailModel) -> Unit) : RecyclerView.Adapter<NameListViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): NameListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_detail_list_item, parent, false)
        return NameListViewHolder(v)
    }

    override fun onBindViewHolder(
            holder: NameListViewHolder,
            position: Int
    ) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(arrayList: ArrayList<BookListDetailModel>) {
        items = arrayList
        notifyDataSetChanged()
    }

}

class NameListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val author = itemView.findViewById<TextView>(R.id.author)
    private val description = itemView.findViewById<TextView>(R.id.description)
    private val price = itemView.findViewById<TextView>(R.id.price)
    fun bind(item: BookListDetailModel) {
        title.text = item.title
        author.text = item.author
        description.text = item.description
        price.text = item.price.toString()
    }

}