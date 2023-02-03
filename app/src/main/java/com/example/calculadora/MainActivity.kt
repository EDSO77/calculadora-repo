package com.example.calculadora

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var calculationViewModel : CalculationViewModel



    private val buttons = arrayOfNulls<Button>(10)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        calculationViewModel = ViewModelProvider(this).get(CalculationViewModel::class.java)
        bindActionViewModel()

        //calculationViewModel.fulloperationLiveData.observe(this, Observer{
          //  binding.tvCalScreen.text= it
        //})
        //calculationViewModel.resultLiveData.observe(this, Observer{
          //  binding.tvCalResult.text= it
        //})
        calculationViewModel.message.observe(this, Observer{
            Toast.makeText(this, it , Toast.LENGTH_SHORT).show()

        })



        setUpView()


    }

    private fun bindActionViewModel() {
        calculationViewModel.getActionLiveData().observe(this, this::handleAction)
    }

    private fun handleAction(actions: CalculatorActions) {
        when (actions) {
           is CalculatorActions.OnShowResultOperation -> binding.tvCalResult.text = actions.result
           is CalculatorActions.OnShowFullOperation -> binding.tvCalScreen.text = actions.result

        }
    }

    private fun setUpView() {
        with(binding) {
            buttons[0] = bt0
            buttons[1] = bt1
            buttons[2] = bt2
            buttons[3] = bt3
            buttons[4] = bt4
            buttons[5] = bt5
            buttons[6] = bt6
            buttons[7] = bt7
            buttons[8] = bt8
            buttons[9] = bt9

            // Asignacion de botones numerico

            buttons.forEachIndexed { index, button ->

                if (button != null) {
                    calculationViewModel.setOnClickNumberButton(index,button)
                }


               }

            //Asignacion de boton point

            btPunto.setOnClickListener {
                calculationViewModel.setOnclickPoint()
               }

            //Asignacion de boton clear
            btClear.setOnClickListener {
                calculationViewModel.clearAll()
            }

            //Asignacion de botones operacion
            btSuma.setOnClickListener {
                calculationViewModel.numberOnChangeListener(OperationCalculator.SUMA)
            }
            btResta.setOnClickListener {
                calculationViewModel.numberOnChangeListener(OperationCalculator.RESTA)
            }
            btMultiplicacion.setOnClickListener {
                calculationViewModel.numberOnChangeListener(OperationCalculator.MULTIPLICACION)
            }
            btDivision.setOnClickListener {
                calculationViewModel.numberOnChangeListener(OperationCalculator.DIVISION)
            }

            // Asignacion resultado

            btIgual.setOnClickListener {

                calculationViewModel.setOnCLickIgual()
            }


        }

    }

}

