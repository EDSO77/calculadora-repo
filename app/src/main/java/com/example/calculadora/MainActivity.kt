package com.example.calculadora

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val buttons = arrayOfNulls<Button>(10)
    private val listNumber = mutableListOf<String>()

    private var fullOperation: String = ""
    private var numBuilder: String = ""

    private var subtractionOpen: Boolean = false // cuando se esta ingresando un numero negativo
    private var operatorRequired: Boolean = false
    private var previousOperator: Boolean = true // empieza como true para que la funcion de verificacion, de inmediato indique al usuario que se requiere un numero
    private var point: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpView()

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
                button?.setOnClickListener {
                    if (!operatorRequired) {
                        upDateNumAndScreen(index.toString())
                        previousOperator = false
                    } else {
                        Toast.makeText(this@MainActivity, "Se requiere signo", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            //Asignacion de boton point

            btPunto.setOnClickListener {
                if (!operatorRequired) {
                    if (!point) {
                        var pointText: String = ""
                        if (numBuilder == "" || numBuilder == "-") {
                            pointText += "0."
                        } else {
                            pointText += "."
                        }
                        upDateNumAndScreen(pointText)
                        point = true
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Se requiere signo", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            //Asignacion parentesis
            btOpen.setOnClickListener {
                ///....

            }
            btClouse.setOnClickListener {
                ///....
            }

            //Asignacion de boton clear
            btClear.setOnClickListener {
                clearAll()
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

            // Asignacion resultado

            btIgual.setOnClickListener {

                if (previousOperator == false && tvCalResult.text != "") {
                    val aux = tvCalResult.text as String
                    clearAll()
                    upDateScreen(aux)
                    listNumber.add(aux)
                    operatorRequired = true
                    previousOperator = false


                } else if (!previousOperator) {
                    addNumBuilderToList()
                    if (subtractionOpen) {
                        upDateScreen(")")
                        subtractionOpen = false
                    }
                    tvCalResult.text = buildResult()
                    if (fullOperation != "") {
                        operatorRequired = true
                    }


                } else {
                    Toast.makeText(this@MainActivity, "Se requiere un numero", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }

    }

    private fun upDateScreen(addedText: String) {
        fullOperation += addedText
        binding.tvCalScreen.text = fullOperation
    }

    private fun upDateNumAndScreen(addedText: String) {
        numBuilder += (addedText)
        upDateScreen(addedText)

    }

    private fun addNumBuilderToList() {
        if (numBuilder != "") {
            listNumber.add(numBuilder)
            numBuilder = ""
        }
    }

    private fun clearAll() {
        binding.tvCalResult.text = ""
        binding.tvCalScreen.text = ""
        fullOperation = ""
        numBuilder = ""
        listNumber.clear()
        point = false
        previousOperator = true
        operatorRequired = false
        subtractionOpen = false
    }

    private fun numberOnChangeListener(operationCalculator: OperationCalculator) {

        operatorRequired = false

        if (!previousOperator) {
            if (subtractionOpen == true) {
                upDateScreen(")")
                subtractionOpen = false
            }
            if((binding.tvCalResult.text as String)!="") { // para resetear tvCalResult despues de haber presionado "="
                fullOperation="($fullOperation)"
                binding.tvCalScreen.text = fullOperation
                binding.tvCalResult.text = ""
            }

            addNumBuilderToList()
            listNumber.add(operationCalculator.value)
            upDateScreen(operationCalculator.value)
            previousOperator = true
            point = false

        } else if (operationCalculator == OperationCalculator.RESTA && fullOperation == "") {

            upDateNumAndScreen("-")

        } else if (operationCalculator == OperationCalculator.RESTA
            && listNumber.last() != "-"
            && !subtractionOpen
            && listNumber.size > 0) {//la ultima parte impide el redundante "..4-(-5)

                numBuilder = "-"
                upDateScreen("(-")
                subtractionOpen = true

        }

    }

    private fun buildResult(): String {
        if (listNumber.size > 1) {

            while (listNumber.size > 1) {
                var index = listNumber.indexOf(OperationCalculator.MULTIPLICACION.value) // regresa la posicion del elemento que estas buscando
                var result = 0.0

                if (index < 0) {
                    index = listNumber.indexOf(OperationCalculator.DIVISION.value)
                    if (listNumber[index + 1].toDouble() == 0.0) {
                        var msg = ""
                        listNumber.forEach { msg += it }
                        msg = "No es posible dividir entre cero:\n" + msg
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        clearAll()
                        return ""
                    }

                    if (index < 0) {
                        index = listNumber.indexOf(OperationCalculator.SUMA.value)
                        if (index < 0) {
                            index = listNumber.indexOf(OperationCalculator.RESTA.value)
                            if (index < 0) {

                            } else {
                                result = listNumber[index - 1].toDouble()
                                    .minus(listNumber[index + 1].toDouble())
                            }
                        } else {
                            result = listNumber[index - 1].toDouble()
                                .plus(listNumber[index + 1].toDouble())
                        }
                    } else {
                        result = listNumber[index - 1].toDouble()
                                .div(listNumber[index + 1].toDouble())
                    }
                } else {
                    result = listNumber[index - 1].toDouble()
                        .times(listNumber[index + 1].toDouble())
                }

                listNumber[index - 1] = result.toString()
                listNumber.removeAt(index + 1)
                listNumber.removeAt(index)
            }
        }
        return listNumber.first()
    }


}

