package exe.weazy.intents.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.intents.R
import exe.weazy.intents.entity.CategoryEntity

class CategoryAdapter(private val categories : List<Pair<Boolean, CategoryEntity>>, private val onItemClick : View.OnClickListener, private val onCheckBoxClick : View.OnClickListener) : RecyclerView.Adapter<CategoryAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        view.setOnClickListener(onItemClick)

        val checkBox = view.findViewById<CheckBox>(R.id.categoryCheckBox)
        checkBox.setOnClickListener(onCheckBoxClick)

        return Holder(view)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(categories[position])
    }

    inner class Holder(view : View) : RecyclerView.ViewHolder(view) {

        private var categoryTextView = view.findViewById<TextView>(R.id.categoryTextView)
        private var categoryCheckBox = view.findViewById<CheckBox>(R.id.categoryCheckBox)

        fun bind(category : Pair<Boolean, CategoryEntity>) {
            categoryTextView.text = category.second.name
            categoryCheckBox.isChecked = category.first
        }
    }
}