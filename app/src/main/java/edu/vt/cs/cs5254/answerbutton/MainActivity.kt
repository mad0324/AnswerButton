package edu.vt.cs.cs5254.answerbutton

import Answer
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import edu.vt.cs.cs5254.answerbutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val DEFAULT_BUTTON_COLOR = "#00A2FF"
    private val SELECTED_BUTTON_COLOR = "#CB297B"

    lateinit var binding: ActivityMainBinding

    // view fields (only one)
    lateinit var answerButtonList: List<Button>

    // model fields (only one)
    private val answerList = listOf(
        Answer(R.string.australia_answer_brisbane, false),
        Answer(R.string.australia_answer_canberra, true),
        Answer(R.string.australia_answer_perth, false),
        Answer(R.string.australia_answer_sydney, false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ------------------------------------------------------
        // Create binding and content view
        // ------------------------------------------------------

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // ------------------------------------------------------
        // Initialize answer-button list
        // ------------------------------------------------------

        answerButtonList = binding.answerButtons
            .children
            .toList()
            .filterIsInstance<Button>()

        // ------------------------------------------------------
        // Set text of views
        // ------------------------------------------------------

        binding.questionTextView.setText(R.string.australia_question)

        answerList.zip(answerButtonList)
            .forEach { (answer, button) -> button.setText(answer.textResId) }

        binding.disableButton.setText(R.string.disable_button)
        binding.resetButton.setText(R.string.reset_button)

        // ------------------------------------------------------
        // Add listeners to buttons
        // ------------------------------------------------------

        answerList.zip(answerButtonList)
            .forEach { (answer, button) ->
                button.setOnClickListener {
                    processAnswerButtonClick(answer)
                }
            }

        binding.disableButton.setOnClickListener {
            processDisableButtonClick()
        }
        binding.resetButton.setOnClickListener {
            processResetButtonClick()
        }

        // ------------------------------------------------------
        // Refresh the view
        // ------------------------------------------------------

        refreshView()
    }

    private fun processAnswerButtonClick(clickedAnswer: Answer) {

        val origIsSelected = clickedAnswer.isSelected

        answerList.forEach { it.isSelected = false }

        clickedAnswer.isSelected = !origIsSelected

        refreshView()
    }

    private fun processDisableButtonClick() {

        answerList
            .filter { !it.isCorrect }
            .take(2)
            .forEach {
                it.isEnabled = false
                it.isSelected = false // deselect when answer is disabled
            }

        refreshView()
    }

    private fun processResetButtonClick() {

        answerList.forEach {
            it.isEnabled = true
            it.isSelected = false
        }

        refreshView()
    }

    private fun refreshView() {

        binding.disableButton.isEnabled = true

        answerList.zip(answerButtonList)
            .forEach { (answer, button) ->
                button.isEnabled = answer.isEnabled
                button.isSelected = answer.isSelected
                setButtonColor(
                    button,
                    if (answer.isSelected) SELECTED_BUTTON_COLOR else DEFAULT_BUTTON_COLOR
                )
                if (!answer.isEnabled) {
                    button.alpha = .5f
                }
            }
        // disable if any answers are disabled
        binding.disableButton.isEnabled = !answerList.filter { !it.isEnabled }.any()

    }

    private fun setButtonColor(button: Button, colorString: String) {
        button.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorString))
        button.setTextColor(Color.WHITE)
        button.alpha = 1f
    }
}
