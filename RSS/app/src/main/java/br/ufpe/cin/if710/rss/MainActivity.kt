package br.ufpe.cin.if710.rss

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.support.v7.preference.PreferenceManager


// Class for fetching the RSS content in a separated thread
class FetchRSS(_activity: MainActivity) : AsyncTask<Void, Void, String>() {
    private val activity: MainActivity = _activity

    override fun doInBackground(vararg params: Void): String {
        return this.activity.getRssFeed()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        val rssList = ParserRSS.parse(result)
        this.activity.changeFeed(rssList)
    }
}

// MainActivity responsible for fetching and displaying the RSS feed
class MainActivity : AppCompatActivity() {

    private var listItems: List<ItemRSS> = Collections.emptyList<ItemRSS>()

    // Metodo para mapear um valor da sharedPreference para a URL do feed
    // Idealmente linkar com o array de @array/feeds
    private fun getRssSource(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val feed: String = prefs.getString("feedRss", "not_set")
        return when (feed) {
            "Professor" -> "http://leopoldomt.com/if1001/g1brasil.xml"
            "Brasil" -> "http://pox.globo.com/rss/g1/brasil/"
            "Ciencia e Saude" -> "http://pox.globo.com/rss/g1/ciencia-e-saude/"
            "Tecnologia" -> "http://pox.globo.com/rss/g1/tecnologia/"
            else -> {
                return "http://leopoldomt.com/if1001/g1brasil.xml"
            }
        }
    }

    fun changeFeed(feed: List<ItemRSS>) {
        this.listItems = feed
        this.renderFeedFragment()
    }

    private fun renderFeedFragment() {
        val rssFeed = this.listItems
        val feedFragment = FeedFragment().apply {
            feed = rssFeed
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, feedFragment)
                .commit()
    }

    // Abre uma pagina web atraves de uma intent implicita
    private fun openWebPage(titleView: TextView) {
        val uriString = titleView.tag as String
        val webURI: Uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW, webURI)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    // Callback para o clique no titulo de uma noticia
    fun onRssTitleClick(view: View) = when (view) {
        is TextView -> this.openWebPage(view)
        else -> {
            System.out.println("Called onClick on wrong type of View!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carrega a preferencia do feed inicial e usa um fallback caso nao definida
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val feed: String? = prefs.getString("feedRss", "not_set")
        if (feed == "not_set") {
            prefs.edit().putString("feedRss", "Professor").commit()
        }

        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            this.renderFeedFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Renderiza a barra de acao no topo do aplicativo de acordo com o layout abaixo
        menuInflater.inflate(R.menu.main_activity_bar, menu)
        return true
    }

    override fun onStart() {
        super.onStart()

        try {
            FetchRSS(this).execute()
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Usuario clicou no icone para mudar preferencia
        R.id.choose_feed_icon -> {
            // Renderiza o fragmento responsavel por mudar preferencias
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, PreferencesFragment())
                    .commit()
            true
        }
        // Usuario clicou no icone para ver o feed
        R.id.view_feed_icon -> {
            // Primeiro, renderizamos o feed vazio enquanto o feed e buscado
            this.listItems = Collections.emptyList()
            val feedFragment = FeedFragment().apply {
                feed = listItems
            }

            // Dispara thread para buscar o feed na internet
            try {
                FetchRSS(this).execute()
            }
            catch(e: IOException) {
                e.printStackTrace()
            }

            // Renderiza o feed
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, feedFragment)
                    .commit()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Opcional - pesquise outros meios de obter arquivos da internet - bibliotecas, etc.
    @Throws(IOException::class)
    fun getRssFeed() : String {
        var inpStream: InputStream? = null
        var rssFeed: String
        try {
            var url = URL(this.getRssSource())
            var conn = url.openConnection() as HttpURLConnection
            inpStream = conn.inputStream
            var out = ByteArrayOutputStream()
            var buffer = ByteArray(size=1024)
            var count = inpStream.read(buffer)
            while(count != -1) {
                out.write(buffer, 0, count)
                count = inpStream.read(buffer)
            }
            var response = out.toByteArray()
            rssFeed = String(
                    response,
                    Charset.forName("UTF-8")
            )
        } finally {
            inpStream?.close()
        }
        return rssFeed
    }


}
