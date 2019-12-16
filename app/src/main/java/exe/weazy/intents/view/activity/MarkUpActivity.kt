package exe.weazy.intents.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.chip.Chip
import exe.weazy.intents.R
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.entity.IntentEntity
import exe.weazy.intents.util.CATEGORIES_ACTIVITY_REQUEST_CODE
import exe.weazy.intents.util.ERROR_RESULT_CODE
import exe.weazy.intents.util.SUCCESS_RESULT_CODE
import kotlinx.android.synthetic.main.activity_mark_up.*

class MarkUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_up)

        val intentForMarkUp = intent.getSerializableExtra("intent") as IntentEntity?

        if (intentForMarkUp != null) {
            sentenceTextView.setText(intentForMarkUp.content, TextView.BufferType.EDITABLE)
        }

        initListeners()
    }

    private fun initListeners() {
        addChip.setOnClickListener {
            val intent = Intent(this, CategoriesActivity::class.java)

            startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CATEGORIES_ACTIVITY_REQUEST_CODE -> {
                when (resultCode) {
                    SUCCESS_RESULT_CODE -> {
                        if (data != null) {
                            val categories = data.extras?.get("categories") as List<CategoryEntity>?

                            categoriesChipGroup.removeAllViews()

                            categories?.forEach {
                                val chip = Chip(this)
                                chip.text = it.name

                                categoriesChipGroup.addView(chip)
                            }
                        }
                    }
                    ERROR_RESULT_CODE -> {

                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
