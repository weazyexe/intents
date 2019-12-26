package exe.weazy.intents.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import exe.weazy.intents.R
import exe.weazy.intents.state.TestState
import exe.weazy.intents.viewmodel.TestViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : Fragment() {

    private lateinit var viewModel : TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TestViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.postValue(TestState.Input())
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    private fun initListeners() {
        viewModel.state.observe(this, Observer {
            setState(it)
        })

        viewModel.predicted.observe(this, Observer {
            val categories = StringBuilder()
            it.forEach { category ->
                categories.append(category.name)
                categories.append('\n')
            }

            categoriesTextView.text = categories.toString()
        })

        val fab = activity?.findViewById<FloatingActionButton>(R.id.actionFab)
        fab?.setOnClickListener {
            viewModel.predictCategory(sentenceTextView.text.toString())
        }
    }

    private fun setState(state: TestState) {
        when(state) {
            is TestState.Done -> {
                dataLayout.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE
            }

            is TestState.Input -> {
                dataLayout.visibility = View.GONE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE
            }

            is TestState.Loading -> {
                dataLayout.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE
            }

            is TestState.Error -> {
                dataLayout.visibility = View.GONE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
            }
        }
    }
}