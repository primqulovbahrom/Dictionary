package uz.gita.dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import uz.gita.dictionary.databinding.ActivityFavoritesBinding
import uz.gita.dictionary.model.local.room.DictionaryDatabase
import uz.gita.dictionary.presenter.adapter.DictionaryAdapter

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val adapter by lazy { DictionaryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.list.adapter = adapter

        val wordDao = DictionaryDatabase.getInstance().wordDao()

        adapter.submitItems(wordDao.favoriteDatabase(1))
        if (wordDao.favoriteDatabase(1).isEmpty()) {
            binding.viewHolder.visibility = View.VISIBLE
        } else binding.viewHolder.visibility = View.INVISIBLE

        adapter.setOnFavoriteClickListener { id, favorite, po ->
            wordDao.updateFav(id, favorite)
            adapter.submitItems(wordDao.favoriteDatabase(1))
            adapter.notifyItemRemoved(po)
            if (adapter.itemCount == 0) {
                binding.viewHolder.visibility = View.VISIBLE
            }else{
                binding.viewHolder.visibility = View.INVISIBLE
            }
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
            intent.putExtra("position", item.isFavourite)
            startActivity(intent)
        }
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.let { wordDao.favoritesDatabase(1, it).isEmpty() }!!) {
                    binding.viewHolder.visibility = View.VISIBLE
                } else binding.viewHolder.visibility = View.INVISIBLE

                query?.let {
                    adapter.submitItems(wordDao.favoriteDatabase(1))
                    adapter.setQuery(it!!.trim())
                    adapter.notifyDataSetChanged()
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText?.let { wordDao.favoritesDatabase(1, it).isEmpty() }!!) {
                    binding.viewHolder.visibility = View.VISIBLE
                } else binding.viewHolder.visibility = View.INVISIBLE


                newText?.let { wordDao.favoritesDatabase(1, newText) }
                    ?.let { adapter.submitItems(it) }
                adapter.setQuery(newText!!)
                adapter.notifyDataSetChanged()
                adapter.setOnFavoriteClickListener { id, favorite, po ->
                    wordDao.updateFav(id, favorite)
                    newText?.let { wordDao.favoritesDatabase(1, it) }
                        ?.let { adapter.submitItems(it) }
                    adapter.notifyDataSetChanged()
                    if (adapter.itemCount == 1) {
                        binding.viewHolder.visibility = View.VISIBLE
                    }else{
                        binding.viewHolder.visibility = View.INVISIBLE
                    }
                }
                return false
            }
        })
        binding.btnFavorite.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnChange.setOnClickListener {
            startActivity(Intent(this,InfoActivity::class.java))
        }

    }
}