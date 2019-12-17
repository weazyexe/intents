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
import exe.weazy.intents.util.showToast
import kotlinx.android.synthetic.main.activity_mark_up.*

class MarkUpActivity : AppCompatActivity() {

    private lateinit var intentForMarkUp : IntentEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_up)

        intentForMarkUp = intent.getParcelableExtra("intent") as IntentEntity
        sentenceTextView.setText(intentForMarkUp.content, TextView.BufferType.EDITABLE)

        intentForMarkUp.categories = mutableListOf()

        initListeners()
    }

    private fun initListeners() {
        addChip.setOnClickListener {
            val intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra("categories", intentForMarkUp.categories?.toTypedArray())
            startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST_CODE)
        }

        saveButton.setOnClickListener {
            val intent = Intent()

            intentForMarkUp.content = sentenceTextView.text.toString()

            if (!intentForMarkUp.categories.isNullOrEmpty()) {
                intentForMarkUp.isMarkedUp = true
            } else {
                intentForMarkUp.categories = null
            }

            intent.putExtra("intent", intentForMarkUp)
            setResult(SUCCESS_RESULT_CODE, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CATEGORIES_ACTIVITY_REQUEST_CODE -> {
                when (resultCode) {
                    SUCCESS_RESULT_CODE -> {
                        if (data != null) {
                            val categories = data.getParcelableArrayExtra("categories")

                            if (categories != null) {

                                intentForMarkUp.categories?.clear()

                                val add = addChip

                                categoriesChipGroup.removeAllViews()

                                categories.forEachIndexed {i, category ->
                                    val chip = Chip(this)

                                    chip.text = (category as CategoryEntity).name
                                    chip.isCloseIconVisible = true

                                    chip.setOnCloseIconClickListener {
                                        intentForMarkUp.categories?.remove(category)
                                        categoriesChipGroup.removeView(it)
                                    }

                                    categoriesChipGroup.addView(chip)
                                    intentForMarkUp.categories?.add(category)
                                }

                                categoriesChipGroup.addView(add)
                            } else {
                                showToast(this, "Что-то пошло не так")
                            }
                        }
                    }
                    ERROR_RESULT_CODE -> {
                        showToast(this, "Категории не были выбраны")
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
