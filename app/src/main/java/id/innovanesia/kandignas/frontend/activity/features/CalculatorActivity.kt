package id.innovanesia.kandignas.frontend.activity.features

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import id.innovanesia.kandignas.databinding.ActivityCalculatorBinding
import id.innovanesia.kandignas.backend.helpers.CalcOperatorHelper
import kotlin.properties.Delegates

class CalculatorActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityCalculatorBinding
    private var operator by Delegates.notNull<Char>()
    private var leftNum by Delegates.notNull<Double>()
    private var rightNum by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binds.root)

        binds.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }

            numberButtons()
            operatorButtons()

            clearButton.setOnClickListener {
                nextNumber.text = ""
                currentNumber.text = "0"
            }

            backspaceButton.setOnClickListener {
                currentNumber.text = currentNumber.text.substring(0, currentNumber.text.length - 1)
                if (currentNumber.text == "" || currentNumber.text == "0")
                {
                    currentNumber.text = "0"
                }
            }

            equalsButton.setOnClickListener {
                calculate()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                finish()
            }
        })
    }

    private fun numberButtons()
    {
        binds.apply {
            oneButton.setOnClickListener {
                setCurrentNumber(1)
            }

            twoButton.setOnClickListener {
                setCurrentNumber(2)
            }

            threeButton.setOnClickListener {
                setCurrentNumber(3)
            }

            fourButton.setOnClickListener {
                setCurrentNumber(4)
            }

            fiveButton.setOnClickListener {
                setCurrentNumber(5)
            }

            sixButton.setOnClickListener {
                setCurrentNumber(6)
            }

            sevenButton.setOnClickListener {
                setCurrentNumber(7)
            }

            eightButton.setOnClickListener {
                setCurrentNumber(8)
            }

            nineButton.setOnClickListener {
                setCurrentNumber(9)
            }

            zeroButton.setOnClickListener {
                setCurrentNumber(0)
            }
        }
    }

    private fun operatorButtons()
    {
        binds.apply {
            plusButton.setOnClickListener {
                selectOperation('+')
            }

            minusButton.setOnClickListener {
                selectOperation('-')
            }

            divideButton.setOnClickListener {
                selectOperation('/')
            }

            multiplyButton.setOnClickListener {
                selectOperation('*')
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun selectOperation(char: Char)
    {
        binds.apply {
            operator = char
            leftNum = currentNumber.text.toString().toDouble()
            when (operator)
            {
                '+' -> nextNumber.text = "${currentNumber.text}+"
                '-' -> nextNumber.text = "${currentNumber.text}-"
                '*' -> nextNumber.text = "${currentNumber.text}×"
                '/' -> nextNumber.text = "${currentNumber.text}÷"
            }
            currentNumber.text = "0"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculate()
    {
        binds.apply {
            rightNum = currentNumber.text.toString().toDouble()
            nextNumber.text = "${nextNumber.text}${currentNumber.text}"

            when (operator)
            {
                '+' -> currentNumber.text =
                    CalcOperatorHelper.add(leftNum, rightNum).toInt().toString()
                '-' -> currentNumber.text =
                    CalcOperatorHelper.subtract(leftNum, rightNum).toInt().toString()
                '*' -> currentNumber.text =
                    CalcOperatorHelper.multiply(leftNum, rightNum).toInt().toString()
                '/' -> currentNumber.text =
                    CalcOperatorHelper.divide(leftNum, rightNum).toInt().toString()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentNumber(num: Int)
    {
        binds.apply {
            if (currentNumber.text == "0")
                currentNumber.text = "$num"
            else
            {
                currentNumber.text = "${currentNumber.text}$num"
            }
        }
    }
}