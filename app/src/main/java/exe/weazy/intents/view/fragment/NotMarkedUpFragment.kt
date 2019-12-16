package exe.weazy.intents.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import exe.weazy.intents.util.showSnackbar
import exe.weazy.intents.view.activity.MarkUpActivity
import exe.weazy.intents.viewmodel.NotMarkedUpViewModel
import kotlinx.android.synthetic.main.fragment_uncategorized.*
import kotlinx.android.synthetic.main.toolbar_search.*

class NotMarkedUpFragment : Fragment() {

    private lateinit var viewModel : NotMarkedUpViewModel

    private lateinit var adapter : IntentAdapter
    private lateinit var layoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_uncategorized, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(NotMarkedUpViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        initListeners()
        initToolbar()

        viewModel.state.postValue(State.Loading())
    }

    private fun setState(state: State) {
        when (state) {
            is State.Loaded -> {
                notMarkedUpRecyclerView.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE

                if (state.msg != null) {
                    showSnackbar(mainLayout, state.msg)
                }

                //mainSwipeLayout.isEnabled = true
            }

            is State.Loading -> {
                notMarkedUpRecyclerView.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE

                //mainSwipeLayout.isRefreshing = false
            }

            is State.Error -> {
                notMarkedUpRecyclerView.visibility = View.GONE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE

                if (state.msg != null) {
                    showSnackbar(mainLayout, state.msg)
                }

                //mainSwipeLayout.isEnabled = true
            }
        }
    }

    private fun initAdapter() {
        val onItemClick = View.OnClickListener {
            val position = notMarkedUpRecyclerView.getChildAdapterPosition(it)
            val intents = viewModel.intents.value

            if (intents != null) {
                val intentForMarkUp = intents[position]

                val intentActivity = Intent(activity, MarkUpActivity::class.java)
                intentActivity.putExtra("intent", intentForMarkUp)

                startActivityForResult(intentActivity, MARK_UP_ACTIVITY_REQUEST_CODE)
            }
        }

        adapter = IntentAdapter(IntentDiffUtilItemCallback(), onItemClick)
        layoutManager = LinearLayoutManager(activity)

        notMarkedUpRecyclerView.adapter = adapter
        notMarkedUpRecyclerView.layoutManager = layoutManager
    }

    private fun initListeners() {
        viewModel.intents.observe(this, Observer {
            adapter.submitList(it)

            if (it.isNotEmpty()) {
                viewModel.state.postValue(State.Loaded())
            }
        })

        viewModel.state.observe(this, Observer {
            setState(it)
        })
    }

    private fun initToolbar() {
        searchEditText.hint = getString(R.string.uncategorized)
    }
}