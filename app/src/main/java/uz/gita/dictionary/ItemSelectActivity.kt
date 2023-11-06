package uz.gita.dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import uz.gita.dictionary.databinding.ActivityItemSelectBinding
import uz.gita.dictionary.model.local.room.DictionaryDatabase
import uz.gita.dictionary.presenter.adapter.DictionaryAdapter
import java.util.Locale

class ItemSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemSelectBinding
    private val adapter by lazy { DictionaryAdapter() }
    private var tts: TextToSpeech? = null
    private var word = ""
    private var pos = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        var isFavourite = intent.getIntExtra("isFavourite", 0)
        val position = intent.getIntExtra("position", 0)
        word = intent.getStringExtra("english")!!
        pos = intent.getIntExtra("pos", 0)


        binding.txtWordEng.text = word
        binding.txtWordUzbek.text = intent.getStringExtra("uzbek")
        binding.txtType.text = intent.getStringExtra("type")
        binding.txtWordCountable.text = intent.getStringExtra("countable")
        binding.txtWordTras.text = intent.getStringExtra("transcript")
        binding.favourite.setImageResource(if (isFavourite == 0) R.drawable.img_star3 else R.drawable.img_star2)

        binding.recycler.adapter = adapter


        val wordDao = DictionaryDatabase.getInstance().wordDao()

        var englishWords = ""


        intent.getStringExtra("english")?.let { englishWords = it }

        adapter.submitItems(wordDao.selectDatabase(englishWords))



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
            val poss = pos + 2
            intent.putExtra("pos", poss)


            if (pos != 1) {
                finish()
            }
            startActivity(intent)
        }


        adapter.setSpeechListener { word ->
            tts!!.setLanguage(Locale.US)
            tts!!.speak(word, TextToSpeech.QUEUE_ADD, null, "")
        }


        adapter.setOnFavoriteClickListener { id, favorite, po ->
            wordDao.updateFav(id, favorite)
            adapter.submitItems(wordDao.selectDatabase(englishWords))
            adapter.notifyItemChanged(po)
        }

        binding.favourite.setOnClickListener {
            wordDao.updateFav(position, isFavourite)
            if (isFavourite == 0) {
                isFavourite = 1
                binding.favourite.setImageResource(R.drawable.img_star2)
                wordDao.updateFav(position, isFavourite)
                adapter.submitItems(wordDao.selectDatabase(englishWords))
            } else {
                isFavourite = 0
                binding.favourite.setImageResource(R.drawable.img_star3)
                wordDao.updateFav(position, isFavourite)
                adapter.submitItems(wordDao.selectDatabase(englishWords))
            }
        }
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {})
        binding.btnAudio.setOnClickListener {
            tts!!.setLanguage(Locale.US)
            tts!!.speak(word, TextToSpeech.QUEUE_ADD, null, "")
        }




        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.let { wordDao.favoritesDatabase(1, it).size } == 0) {
                }
                query?.let {
                    adapter.submitItems(wordDao.searchDatabase(it.trim()))
                    adapter.setQuery(it!!.trim())
                    adapter.notifyDataSetChanged()
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {


                if (newText?.let { wordDao.favoritesDatabase(1, it).size } == 0) {
                    Toast.makeText(this@ItemSelectActivity, "EMPTY", Toast.LENGTH_SHORT).show()
                }
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




        binding.btnFavourite.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        binding.btnChange.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

    }
}



