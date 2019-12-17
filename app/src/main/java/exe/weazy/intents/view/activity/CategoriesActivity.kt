package exe.weazy.intents.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import exe.weazy.intents.R
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.recycler.adapter.CategoryAdapter
import exe.weazy.intents.util.ERROR_RESULT_CODE
import exe.weazy.intents.util.SUCCESS_RESULT_CODE
import exe.weazy.intents.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : AppCompatActivity() {

    private lateinit var adapter : CategoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var viewModel : MainViewModel

    private lateinit var categories : MutableList<Pair<Boolean, CategoryEntity>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        categories = mutableListOf()

        val rawCategories = intent.getParcelableArrayExtra("categories")

        viewModel.categories.value?.forEach {
            val flag = rawCategories.contains(it)
            categories.add(Pair(flag, it))
        }

        initAdapter()
        initListeners()
    }

    private fun initAdapter() {

        val onItemClick = View.OnClickListener {
            val position = categoriesRecyclerView.getChildAdapterPosition(it)
            categories[position] = Pair(!categories[position].first, categories[position].second)

            val checkBox = it.findViewById<CheckBox>(R.id.categoryCheckBox)

            checkBox.isChecked = categories[position].first
        }

        val onCheckBoxClick = View.OnClickListener {
            val position = categoriesRecyclerView.getChildAdapterPosition(it.parent as View)
            categories[position] = Pair(!categories[position].first, categories[position].second)
        }

        adapter = CategoryAdapter(categories, onItemClick, onCheckBoxClick)
        layoutManager = LinearLayoutManager(this)

        categoriesRecyclerView.adapter = adapter
        categoriesRecyclerView.layoutManager = layoutManager
    }

    private fun initListeners() {
        setCategoriesButton.setOnClickListener {
            val intent = Intent()

            val checkedCategories = categories
                .filter { it.first }
                .map { it.second }.toTypedArray()

            intent.putExtra("categories", checkedCategories)

            setResult(SUCCESS_RESULT_CODE, intent)
            finish()
        }

        closeButton.setOnClickListener {
            setResult(ERROR_RESULT_CODE)
            finish()
        }
    }
}
