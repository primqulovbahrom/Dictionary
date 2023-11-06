package uz.gita.dictionary.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.dictionary.R
import uz.gita.dictionary.databinding.ItemWordBinding
import uz.gita.dictionary.model.local.room.entity.WordEntity
import uz.gita.dictionary.utils.spannable

class DictionaryAdapter : RecyclerView.Adapter<DictionaryAdapter.Holder>() {
    private val data: ArrayList<WordEntity> = arrayListOf()
    private var query = ""
    private var onFavoriteClickListener: ((Int, Int, Int) -> Unit)? = null
    private var speechListener: ((String) -> Unit)? = null
    private var onItemClickListener: ((ArrayList<WordEntity>, Int) -> Unit)? =
        null

    fun setOnFavoriteClickListener(listener: (Int, Int, Int) -> Unit) {
        onFavoriteClickListener = listener
    }

    fun setQuery(q: String) {
        query = q;
    }

    fun setOnItemClickListener(listener: (ArrayList<WordEntity>, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setSpeechListener(block: (String) -> Unit) {
        speechListener = block
    }

    fun submitItems(items: List<WordEntity>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }


    inner class Holder(private val itemWordBinding: ItemWordBinding) :
        RecyclerView.ViewHolder(itemWordBinding.root) {
        init {
            itemWordBinding.btnAudio.setOnClickListener {
                speechListener?.invoke(data[adapterPosition].english!!)
            }

            itemWordBinding.btnFavorite.setOnClickListener {
                val item = data[adapterPosition]
                onFavoriteClickListener?.invoke(
                    item.id,
                    if (item.isFavourite == 0) 1 else 0,
                    adapterPosition
                )
            }
            itemWordBinding.root.setOnClickListener {
                onItemClickListener?.invoke(data, adapterPosition)
            }
        }

        fun bind() {
            val item = data[adapterPosition]
            if (query.isEmpty()) {
                itemWordBinding.txtEng.text = item.english
            } else {
                itemWordBinding.txtEng.text = item.english?.spannable(query, itemView.context)
            }
            /*itemWordBinding.txtUz.text = item.uzbek
            itemWordBinding.txtType.text = item.type
            itemWordBinding.txtCoutable.text = item.countable
            itemWordBinding.txtTranscript.text = item.transcript*/
            itemWordBinding.btnFavorite.setImageResource(if (item.isFavourite == 0) R.drawable.img_star3 else R.drawable.img_star2)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind()

}