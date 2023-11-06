package uz.gita.dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import uz.gita.dictionary.databinding.ActivityMainBinding
import uz.gita.dictionary.model.local.room.DictionaryDatabase
import uz.gita.dictionary.model.local.room.dao.WordDao
import uz.gita.dictionary.presenter.adapter.DictionaryAdapter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var wordDao: WordDao

    private val adapter by lazy { DictionaryAdapter() }
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.list.adapter = adapter

        wordDao = DictionaryDatabase.getInstance().wordDao()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        adapter.submitItems(wordDao.getAllWords())
        adapter.notifyDataSetChanged()

        adapter.setOnFavoriteClickListener { id, favorite, po ->
            wordDao.updateFav(id, favorite)
            adapter.submitItems(wordDao.getAllWords())
            adapter.notifyItemChanged(po)
        }
        adapter.setOnItemClickListener { data, position ->
            val item = data[position]
            val intent = Intent(this, ItemSelectActivity::class.java)
            intent.putExtra("english", item.english)
            intent.putExtra("uzbek", item.uzbek)
            intent.putExtra("transcript", item.transcript)
            intent.putExtra("type", item.type)
            intent.putExtra("countable", item.countable)
            intent.putExtra("english", item.english)
            intent.putExtra("isFavourite", item.isFavourite)
            intent.putExtra("position", item.id)
            startActivity(intent)

        }
        activityMainBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.let { wordDao.searchDatabase(it.trim()).isEmpty() }!!) {
                    activityMainBinding.viewHolder.visibility = View.VISIBLE
                } else activityMainBinding.viewHolder.visibility = View.INVISIBLE

                query?.let {
                    adapter.submitItems(wordDao.searchDatabase(it.trim()))
                    adapter.setQuery(it!!.trim())
                    adapter.notifyDataSetChanged()
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText?.let { wordDao.searchDatabase(it.trim()).isEmpty() }!!) {
                    activityMainBinding.viewHolder.visibility = View.VISIBLE
                } else activityMainBinding.viewHolder.visibility = View.INVISIBLE


                newText?.let { wordDao.searchDatabase(it) }?.let { adapter.submitItems(it) }
                adapter.setQuery(newText!!)
                adapter.notifyDataSetChanged()
                adapter.setOnFavoriteClickListener { id, favorite, po ->
                    wordDao.updateFav(id, favorite)
                    newText?.let { wordDao.searchDatabase(it) }?.let { adapter.submitItems(it) }
                    adapter.notifyDataSetChanged()
                }
                return false
            }
        })

        activityMainBinding.btnFavorite.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {})
        adapter.setSpeechListener { word ->
            tts!!.setLanguage(Locale.US)
            tts!!.speak(word, TextToSpeech.QUEUE_ADD, null, "")
        }
        activityMainBinding.btnChange.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.submitItems(wordDao.getAllWords())

    }

}