package exe.weazy.intents.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.intents.R
import exe.weazy.intents.entity.IntentEntity

class IntentAdapter(diffUtil : DiffUtil.ItemCallback<IntentEntity>, private val onItemClick : View.OnClickListener) : PagedListAdapter<IntentEntity, IntentAdapter.Holder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_intent, parent, false)
        view.setOnClickListener(onItemClick)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val intent = getItem(position)

        if (intent != null) {
            holder.bind(intent)
        }
    }

    inner class Holder(view : View) : RecyclerView.ViewHolder(view) {

        private var contentTextView : TextView = view.findViewById(R.id.contentTextView)
        private var categoriesTextView : TextView = view.findViewById(R.id.categoriesTextView)

        fun bind(intent : IntentEntity) {
            contentTextView.text = intent.content

            if (intent.categories != null) {
                val categories = StringBuilder()
                categoriesTextView.visibility = View.VISIBLE

                intent.categories.forEachIndexed { i, it ->
                    if (i != intent.categories.size - 1) {
                        categories.append("${it.name}, ")
                    } else {
                        categories.append(it.name)
                    }
                }

                categoriesTextView.text = categories.toString()
            } else {
                categoriesTextView.visibility = View.GONE
            }

        }
    }
}