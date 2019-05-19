package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class RssAdapter(currentActivity: Activity, rssItems: Array<ItemRSS>) :
        RecyclerView.Adapter<RssAdapter.RssViewHolder>() {

    private val currentActivity: Activity = currentActivity
    private var items: Array<ItemRSS> = rssItems

    class RssViewHolder(layout: View) : RecyclerView.ViewHolder(layout) {
        val titleView: TextView = layout.findViewById(R.id.item_titulo) as TextView
        val dataView: TextView = layout.findViewById(R.id.item_data) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RssAdapter.RssViewHolder {
        val linearLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.itemlista, parent, false) as LinearLayout

        return RssViewHolder(linearLayout)
    }

    // Abre uma pagina web atraves de uma intent implicita
    private fun openWebPage(url: String) {
        val webURI: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webURI)
        if (intent.resolveActivity(this.currentActivity.packageManager) != null) {
            this.currentActivity.startActivity(intent)
        }
    }

    override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
        // Seta o texto das views do titulo e data, alem de colocar uma data extra na view do titulo
        // (com o link que a noticia leva)
        val rss = this.items[position]
        holder.titleView.text = rss.title
        holder.titleView.setClickable(true)
        holder.titleView.setOnClickListener {
            run {
                this.openWebPage(rss.link)
            }
        }
        holder.dataView.text = rss.pubDate
    }

    override fun getItemCount() = this.items.size
}