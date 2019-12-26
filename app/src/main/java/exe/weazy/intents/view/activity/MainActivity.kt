package exe.weazy.intents.view.activity

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import exe.weazy.intents.R
import exe.weazy.intents.view.fragment.MarkedUpFragment
import exe.weazy.intents.view.fragment.NotMarkedUpFragment
import exe.weazy.intents.view.fragment.TestFragment
import exe.weazy.intents.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var uncategorizedFragment: NotMarkedUpFragment
    private lateinit var categorizedFragment : MarkedUpFragment
    private lateinit var testFragment: TestFragment
    private var active = Fragment()

    private lateinit var viewModel : MainViewModel

    private var newPosition = 0
    private var startingPosition = 0

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.uncategorizedButton -> {
                newPosition = 0
                if (startingPosition != newPosition) {

                    if (startingPosition == 1) {
                        moveFabUp()
                    }

                    actionFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.done_to_add))
                    (actionFab.drawable as AnimatedVectorDrawable).start()

                    changeFragment(uncategorizedFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.categorizedButton -> {
                newPosition = 1
                if (startingPosition != newPosition) {
                    changeFragment(categorizedFragment)
                    moveFabDown()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.testButton -> {
                newPosition = 2
                if (startingPosition != newPosition) {

                    if (startingPosition == 1) {
                        moveFabUp()
                    }

                    actionFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add_to_done))
                    (actionFab.drawable as AnimatedVectorDrawable).start()

                    changeFragment(testFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // make placeholder data
        /*val repository = Repository()
        repository.putPlaceholderData()*/

        loadFragments()

        active = uncategorizedFragment

        if (savedInstanceState != null) {
            newPosition = savedInstanceState["new_position"] as Int
            startingPosition = savedInstanceState["starting_position"] as Int

            when (startingPosition) {
                0 -> {
                    changeFragment(uncategorizedFragment)
                    mainBottomNav.selectedItemId = R.id.uncategorizedButton
                }
                1 -> {
                    changeFragment(categorizedFragment)
                    mainBottomNav.selectedItemId = R.id.categorizedButton
                }
                2 -> {
                    changeFragment(testFragment)
                    mainBottomNav.selectedItemId = R.id.testButton
                }
            }
        }

        actionFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add))
    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
            .show(active)
            .commit()
    }

    override fun onStop() {
        supportFragmentManager.beginTransaction()
            .hide(active)
            .commit()

        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.clear()

        outState.putInt("starting_position", startingPosition)
        outState.putInt("new_position", newPosition)

        super.onSaveInstanceState(outState)
    }

    private fun loadFragments() {
        uncategorizedFragment = NotMarkedUpFragment()
        categorizedFragment = MarkedUpFragment()
        testFragment = TestFragment()

        supportFragmentManager.beginTransaction().add(R.id.fragmentLayout, categorizedFragment).hide(categorizedFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragmentLayout, testFragment).hide(testFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragmentLayout, uncategorizedFragment).commit()

        mainBottomNav.selectedItemId = R.id.uncategorizedButton
    }

    private fun changeFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.top_level_in, R.anim.top_level_out)*/.show(fragment).hide(active).commit()
        startingPosition = newPosition
        active = fragment
    }

    private fun moveFabDown() {
        actionFab.animate()
            .setDuration(100)
            .translationY(200f)
            .start()
    }

    private fun moveFabUp() {
        actionFab.animate()
            .setDuration(100)
            .translationY(-16f)
            .start()
    }
}
