package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var calScreen: String = ""
    
    private val buttons = arrayOfNulls<Button>(10)
    private var num1= 0.0
    private var punto: Boolean= false
    private val listNumber = mutableListOf<String>()
    private var fullOperation: String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView() {
        with(binding){
            buttons[0]=bt0
            buttons[1]=bt1
            buttons[2]=bt2
            buttons[3]=bt3
            buttons[4]=bt4
            buttons[5]=bt5
            buttons[6]=bt6
            buttons[7]=bt7
            buttons[8]=bt8
            buttons[9]=bt9

            // Asignacion de botones numerico

            buttons.forEachIndexed { index, button ->
                button?.setOnClickListener {
                    fullOperation += index.toString()
                    calScreen += index.toString()
                    binding.tvCalScreen.text=fullOperation
                }
            }
            btPunto.setOnClickListener {
                if (!punto) {
                    calScreen += "."
                    tvCalScreen.text = calScreen
                }
                punto=true
            }

            //Asignacion de boton clear
            btClear.setOnClickListener {
                tvCalResult.text=""
                tvCalScreen.text=""
                calScreen=""
                num1 = 0.0
                punto = false
            }
            //Asignacion de botones operacion
            btSuma.setOnClickListener {
                numberOnChangeListener(OperationCalculator.SUMA)
            }
            btResta.setOnClickListener {
                numberOnChangeListener(OperationCalculator.RESTA)
            }
            btMultiplicacion.setOnClickListener {
                numberOnChangeListener(OperationCalculator.MULTIPLICACION)
            }
            btDivision.setOnClickListener {
                numberOnChangeListener(OperationCalculator.DIVISION)
            }
            btIgual.setOnClickListener {
                listNumber.add(calScreen)
                calScreen = ""
                tvCalResult.text = buildResult()
            }

            // Asignacion resultado
        }

    }

    private fun numberOnChangeListener(operationCalculator: OperationCalculator) {

        listNumber.add(calScreen)
        calScreen = ""
        fullOperation += operationCalculator.value
        listNumber.add(operationCalculator.value)
        binding.tvCalScreen.text = fullOperation

//        when (operationCalculator) {
//            OperationCalculator.SUMA -> TODO()
//            OperationCalculator.RESTA -> TODO()
//            OperationCalculator.MULTIPLICACION -> TODO()
//            OperationCalculator.DIVISION -> TODO()
//        }



    }

    private fun buildResult(): String {
        while (listNumber.size > 1) {
            var index = listNumber.indexOf(OperationCalculator.MULTIPLICACION.value) // regresa la posicion del elemento que estas buscando
            var result = 0.0

            if (index < 0) {
                index = listNumber.indexOf(OperationCalculator.DIVISION.value)
                if(index < 0) {
                    index = listNumber.indexOf(OperationCalculator.SUMA.value)
                    if(index < 0) {
                        index = listNumber.indexOf(OperationCalculator.RESTA.value)
                        if(index < 0) {

                        } else {
                            result = listNumber[index-1].toDouble().minus(listNumber[index+1].toDouble())
                        }
                    } else {
                        result = listNumber[index-1].toDouble().plus(listNumber[index+1].toDouble())
                    }
                } else {
                    result = listNumber[index-1].toDouble().div(listNumber[index+1].toDouble())
                }
            } else {
                result = listNumber[index-1].toDouble().times(listNumber[index+1].toDouble())
            }

            listNumber[index-1] = result.toString()
            listNumber.removeAt(index + 1)
            listNumber.removeAt(index)
        }
        return listNumber.first()
    }


}

