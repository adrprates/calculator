package com.dm.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private var currentExpression = "0"
    private var op1: Double? = null
    private var pendingOp: String = "="
    private var isNewOp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.result)
        inputField.setText(currentExpression)

        val buttonIds = mapOf(
            "0" to R.id.button0, "1" to R.id.button1, "2" to R.id.button2,
            "3" to R.id.button3, "4" to R.id.button4, "5" to R.id.button5,
            "6" to R.id.button6, "7" to R.id.button7, "8" to R.id.button8,
            "9" to R.id.button9, "." to R.id.buttonDot
        )

        buttonIds.forEach { (text, id) ->
            findViewById<Button>(id).setOnClickListener { appendToExpression(text) }
        }

        findViewById<Button>(R.id.buttonAdd).setOnClickListener { setPendingOperation("+", it as Button) }
        findViewById<Button>(R.id.buttonSubtract).setOnClickListener { setPendingOperation("-", it as Button) }
        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { setPendingOperation("*", it as Button) }
        findViewById<Button>(R.id.buttonDivide).setOnClickListener { setPendingOperation("/", it as Button) }

        findViewById<Button>(R.id.buttonEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.buttonClear).setOnClickListener { clear() }

        findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
            if (currentExpression.isNotEmpty()) {
                currentExpression = currentExpression.dropLast(1)
                if (currentExpression.isEmpty()) currentExpression = "0"
                inputField.setText(currentExpression)
            }
        }

        findViewById<Button>(R.id.buttonToggleSign).setOnClickListener { toggleSign() }
        findViewById<Button>(R.id.buttonPercentage).setOnClickListener { calculatePercentage() }
    }

    private fun appendToExpression(value: String) {
        if (isNewOp) {
            currentExpression = ""
            isNewOp = false
        }
        currentExpression = if (currentExpression == "0" && value != ".") value else currentExpression + value
        inputField.setText(currentExpression)
    }

    private fun setPendingOperation(op: String, button: Button) {
        if (op1 == null) op1 = currentExpression.toDoubleOrNull() ?: 0.0
        else calculateResult()
        pendingOp = op
        isNewOp = true
    }

    private fun calculateResult() {
        val value = currentExpression.toDoubleOrNull() ?: return
        val result = when (pendingOp) {
            "+" -> (op1 ?: 0.0) + value
            "-" -> (op1 ?: 0.0) - value
            "*" -> (op1 ?: 0.0) * value
            "/" -> if (value == 0.0) { inputField.setText("Error"); return } else (op1 ?: 0.0) / value
            else -> value
        }

        currentExpression = if (result == result.toInt().toDouble()) result.toInt().toString() else result.toString()
        inputField.setText(currentExpression)
        op1 = result
        isNewOp = true
    }

    private fun clear() {
        currentExpression = "0"
        op1 = null
        pendingOp = "="
        isNewOp = true
        inputField.setText(currentExpression)
    }

    private fun calculatePercentage() {
        val value = currentExpression.toDoubleOrNull() ?: return
        currentExpression = (value / 100).toString()
        inputField.setText(currentExpression)
    }

    private fun toggleSign() {
        val value = currentExpression.toDoubleOrNull() ?: return
        currentExpression = (-value).toString()
        inputField.setText(currentExpression)
    }
}