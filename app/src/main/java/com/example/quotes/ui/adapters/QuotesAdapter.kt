package com.example.quotes.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.R
import com.example.quotes.data.quotes.Quote

class QuotesAdapter(private val quotes: ArrayList<Quote>, private val onCLick: (Quote) -> Unit) :
    RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(itemView: View, val onCLick: (Quote) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val quoteText: TextView = itemView.findViewById(R.id.quote_text)
        private val quoteAuthor: TextView = itemView.findViewById(R.id.quote_author)
        private var currentQuote: Quote? = null

        init {
            itemView.setOnClickListener {
                currentQuote?.let {
                    onCLick(it)
                }
            }
        }

        fun bind(quote: Quote) {
            currentQuote = quote

            quoteText.text = quote.text
            quoteAuthor.text = quote.author
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.quote_item, parent, false)
            .also {
                return QuoteViewHolder(it, onCLick)
            }
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position])
    }

    override fun getItemCount(): Int = quotes.size

    fun updateQuotes(quotes: List<Quote>) {
        this.quotes.clear()
        this.quotes.addAll(quotes)
        notifyDataSetChanged()
    }

}