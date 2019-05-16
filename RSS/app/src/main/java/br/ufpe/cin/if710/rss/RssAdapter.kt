package br.ufpe.cin.if710.rss

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class RssAdapter(rssItems: Array<ItemRSS>) :
        RecyclerView.Adapter<RssAdapter.RssViewHolder>() {

    private var items: Array<ItemRSS> = rssItems

    class RssViewHolder(val _layout: View) : RecyclerView.ViewHolder(_layout) {
        val titleView: TextView = _layout.findViewById(R.id.item_titulo) as TextView
        val dataView: TextView = _layout.findViewById(R.id.item_data) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RssAdapter.RssViewHolder {
        val linearLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.itemlista, parent, false) as LinearLayout

        return RssViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
        // Seta o texto das views do titulo e data, alem de colocar uma data extra na view do titulo
        // (com o link que a noticia leva)
        val rss = this.items[position]
        holder.titleView.text = rss.title
        holder.titleView.tag = rss.link
        holder.dataView.text = rss.pubDate
    }

    override fun getItemCount() = this.items.size
}