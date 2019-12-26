package exe.weazy.intents.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import exe.weazy.intents.R
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.recycler.adapter.CategoryAdapter
import exe.weazy.intents.state.State
import exe.weazy.intents.util.ERROR_RESULT_CODE
import exe.weazy.intents.util.SUCCESS_RESULT_CODE
import exe.weazy.intents.util.showSnackbar
import exe.weazy.intents.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : AppCompatActivity() {

    private lateinit var adapter : CategoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var viewModel : MainViewModel

    private lateinit var categories : MutableList<Pair<Boolean, CategoryEntity>>

    private var rawCategories : Array<Parcelable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.loadCategories()

        categories = mutableListOf()

        rawCategories = intent.getParcelableArrayExtra("categories")

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

        viewModel.state.observe(this, Observer {
            setState(it)
        })

        viewModel.categories.observe(this, Observer { categories ->

            setState(State.Loaded())

            val copy = rawCategories
            if (copy != null) {
                categories.forEach { category ->
                    val flag = copy.contains(category)
                    this.categories.add(Pair(flag, category))
                }
            }
        })
    }

    private fun setState(state: State) {
        when (state) {
            is State.Loaded -> {
                categoriesRecyclerView.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE

                if (state.msg != null) {
                    showSnackbar(mainLayout, state.msg)
                }

                mainSwipeLayout.isEnabled = true
            }

            is State.Loading -> {
                categoriesRecyclerView.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE

                mainSwipeLayout.isRefreshing = false
            }

            is State.Error -> {
                categoriesRecyclerView.visibility = View.GONE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE

                if (state.msg != null) {
                    showSnackbar(mainLayout, state.msg)
                }

                mainSwipeLayout.isEnabled = true
            }
        }
    }
}
