package br.ufpe.cin.if710.rss

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.*
import android.view.LayoutInflater
import android.view.ViewGroup

// Fragmento responsavel por renderizar o RecyclerView com o feed
class FeedFragment : Fragment() {

    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var viewManager: RecyclerView.LayoutManager

    var feed: List<ItemRSS> = Collections.emptyList<ItemRSS>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.feed_fragment, container, false)

        viewManager = LinearLayoutManager(this.activity)
        viewAdapter = RssAdapter(this.activity!!, feed.toTypedArray<ItemRSS>())

        root.findViewById<RecyclerView>(R.id.conteudoRSS).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return root
    }
}