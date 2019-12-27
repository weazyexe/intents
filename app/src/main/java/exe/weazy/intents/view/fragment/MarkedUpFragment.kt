package exe.weazy.intents.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import exe.weazy.intents.R
import exe.weazy.intents.recycler.adapter.IntentAdapter
import exe.weazy.intents.recycler.diffutil.IntentDiffUtilItemCallback
import exe.weazy.intents.state.State
import exe.weazy.intents.util.MARK_UP_ACTIVITY_REQUEST_CODE
import exe.weazy.intents.util.NO_CONTENT_TAG
import exe.weazy.intents.util.showSnackbar
import exe.weazy.intents.view.activity.MarkUpActivity
import exe.weazy.intents.viewmodel.MarkedUpViewModel
import kotlinx.android.synthetic.main.fragment_categorized.*
import kotlinx.android.synthetic.main.toolbar_search.*

class MarkedUpFragment : Fragment() {

    private lateinit var viewModel : MarkedUpViewModel

    private lateinit var adapter : IntentAdapter
    private lateinit var layoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categorized, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MarkedUpViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        initListeners()

        viewModel.state.postValue(State.Loading())
    }

    private fun setState(state: State) {
        when (state) {
            is State.Loaded -> {
                markedUpRecyclerView.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE

                if (state.msg != null) {
                    if (state.msg != NO_CONTENT_TAG) {
                        showSnackbar(mainLayout, state.msg)
                    } else {
                        markedUpRecyclerView.visibility = View.GONE
                        withoutContentLayout.visibility = View.VISIBLE
                    }
                } else {
                    withoutContentLayout.visibility = View.GONE
                }

                mainSwipeLayout.isEnabled = true
            }

            is State.Loading -> {
                markedUpRecyclerView.visibility = View.GONE
                withoutContentLayout.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE

                mainSwipeLayout.isRefreshing = false
            }

            is State.Error -> {
                markedUpRecyclerView.visibility = View.GONE
                withoutContentLayout.visibility = View.GONE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE

                if (state.msg != null) {
                    showSnackbar(mainLayout, state.msg)
                }

                mainSwipeLayout.isEnabled = true
            }
        }
    }

    private fun initAdapter() {
        val onItemClick = View.OnClickListener {
            showSnackbar(mainLayout, "tapped")
        }

        adapter = IntentAdapter(IntentDiffUtilItemCallback(), onItemClick)
        layoutManager = LinearLayoutManager(activity)

        markedUpRecyclerView.adapter = adapter
        markedUpRecyclerView.layoutManager = layoutManager
    }

    private fun initListeners() {
        viewModel.intents.observe(this, Observer {
            adapter.submitList(it)

            if (it.isNotEmpty()) {
                viewModel.state.postValue(State.Loaded())
            } else if (mainSwipeLayout.isEnabled) {
                viewModel.state.postValue(State.Loaded(NO_CONTENT_TAG))
            }
        })

        viewModel.state.observe(this, Observer {
            setState(it)
        })

        mainSwipeLayout.setOnRefreshListener {
            viewModel.refresh()
            mainSwipeLayout.isEnabled = false
        }
    }
}