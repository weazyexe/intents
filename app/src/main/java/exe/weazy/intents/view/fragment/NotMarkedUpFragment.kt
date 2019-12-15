package exe.weazy.intents.view.fragment

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
import exe.weazy.intents.viewmodel.NotMarkedUpViewModel
import kotlinx.android.synthetic.main.fragment_uncategorized.*

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

        viewModel.state.postValue(State.Loading())
    }

    private fun setState(state: State) {
        when (state) {
            is State.Loaded -> {
                notMarkedUpRecyclerView.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE

                if (state.msg != null) {
                    showSnackbar(state.msg)
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
                    showSnackbar(state.msg)
                }

                //mainSwipeLayout.isEnabled = true
            }
        }
    }

    private fun initAdapter() {
        val onItemClick = View.OnClickListener {
            showSnackbar("tapped")
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

    private fun showSnackbar(msg: String) {
        Snackbar.make(mainLayout, msg, Snackbar.LENGTH_LONG).show()
    }
}