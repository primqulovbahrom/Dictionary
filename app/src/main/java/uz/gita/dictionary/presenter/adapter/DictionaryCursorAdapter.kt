package uz.gita.dictionary.presenter.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.dictionary.R
import uz.gita.dictionary.databinding.ItemWordBinding

class DictionaryCursorAdapter : RecyclerView.Adapter<DictionaryCursorAdapter.Holder>() {
    private var cursor: Cursor? = null
    private var onFavoriteClickListener: ((Int, Int) -> Unit)? = null
    private var onItemClickListener: ((Cursor, Int) -> Unit)? =
        null

    fun setOnFavoriteClickListener(listener: (Int, Int) -> Unit) {
        onFavoriteClickListener = listener
    }

    fun submitCursor(cursor: Cursor) {
        this.cursor = cursor
        notifyDataSetChanged()
    }
    fun setOnItemClickListener(listener: (Cursor, Int) -> Unit) {
        onItemClickListener = listener
    }

    inner class Holder(private val itemWordBinding: ItemWordBinding) :
        RecyclerView.ViewHolder(itemWordBinding.root) {
        init {
            itemWordBinding.btnFavorite.setOnClickListener {
                cursor?.let { cursor: Cursor ->
                    cursor.moveToPosition(adapterPosition)
                    val favoriteResult = if (cursor.getInt(6) == 0) 1 else 0
                    onFavoriteClickListener?.invoke(cursor.getInt(0), favoriteResult)
                }
            }
            itemWordBinding.root.setOnClickListener {
                onItemClickListener?.invoke(cursor!!,adapterPosition)
            }
        }

        fun bind() {
            cursor?.let { cursor: Cursor ->
                cursor.moveToPosition(adapterPosition)
                itemWordBinding.txtEng.text = cursor.getString(1)
                /*itemWordBinding.txtUz.text = cursor.getString(4)
                itemWordBinding.txtType.text = cursor.getString(2)
                itemWordBinding.txtCoutable.text = cursor.getString(5)
                itemWordBinding.txtTranscript.text = cursor.getString(3)*/
                itemWordBinding.btnFavorite.setImageResource(if (cursor.getInt(6) == 0) R.drawable.img_star3 else R.drawable.img_star2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        cursor?.let {
            return it.count
        }
        return 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind()


}