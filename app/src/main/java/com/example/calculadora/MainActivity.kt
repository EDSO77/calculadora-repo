package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var calScreen: String = ""
    private val buttons = arrayOfNulls<Button>(10)
    private var punto: Boolean= false
    private val listNumber = mutableListOf<String>()
    private var fullOperation: String=""
    // empieza como true para que la funcion de verificacion, de inmediato indique al usuario que se requiere un numero
    private var signo: Boolean = true
    //cuando se esta ingresando un numero negativo
    private var subtractionOpen: Boolean=false


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
                    fullOperation += index.toString()
                    calScreen += index.toString()
                    tvCalScreen.text = fullOperation
                    signo = false
                }
            }
            btPunto.setOnClickListener {
                if (!punto) {
                    fullOperation += "."
                    calScreen += "."
                    tvCalScreen.text = fullOperation
                }
                punto = true
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

                 if(signo==false && tvCalResult.text != ""){
                     val aux:String
                   aux= tvCalResult.text as String
                   clearAll()
                   fullOperation += aux
                   tvCalScreen.text = fullOperation
                   listNumber.add(aux)
                     signo=false
                     Toast.makeText(this@MainActivity, "2", Toast.LENGTH_SHORT)
                         .show()


                }else if (!signo) {
                if (calScreen != ""){
                    listNumber.add(calScreen)
                    calScreen = ""}
                if (subtractionOpen){
                    subtractionOpen=false
                    fullOperation += ")"
                    tvCalScreen.text = fullOperation
                }
                tvCalResult.text = buildResult()
                Toast.makeText(this@MainActivity, "1", Toast.LENGTH_SHORT)
                    .show()

            } else {
                     Toast.makeText(this@MainActivity, "Se requiere un numero", Toast.LENGTH_SHORT)
                         .show()
                    }
                }


            }

        }


    private fun clearAll() {
        binding.tvCalResult.text = ""
        binding.tvCalScreen.text = ""
        fullOperation=""
        calScreen = ""
        listNumber.clear()
        punto = false
        signo=true
    }

    private fun numberOnChangeListener(operationCalculator: OperationCalculator) {
        if(signo==false) {
            binding.tvCalResult.text =""
            if (subtractionOpen==true){
                subtractionOpen=false
                fullOperation += ")"
                binding.tvCalScreen.text = fullOperation
            }
            if (calScreen != ""){
            listNumber.add(calScreen)
            calScreen = ""}
            fullOperation += operationCalculator.value
            listNumber.add(operationCalculator.value)
            binding.tvCalScreen.text = fullOperation
            punto = false
            signo=true
        }
        else if (operationCalculator==OperationCalculator.RESTA&&fullOperation==""){
            calScreen += "-"
            fullOperation += "-"
            binding.tvCalScreen.text = fullOperation
        }
        else if (operationCalculator==OperationCalculator.RESTA && subtractionOpen==false && listNumber.size > 0){//la ultima parte impide el redundante "..4-(-5)
           if (listNumber.last() != "-" ){
            subtractionOpen=true
            fullOperation += "(-"
            calScreen = "-"
            binding.tvCalScreen.text = fullOperation
           }
        }

    }

    private fun buildResult(): String {
        if (listNumber.size>=3){
        while (listNumber.size > 1) {
            var index = listNumber.indexOf(OperationCalculator.MULTIPLICACION.value) // regresa la posicion del elemento que estas buscando
            var result = 0.0

            if (index < 0) {
                index = listNumber.indexOf(OperationCalculator.DIVISION.value)
                if (listNumber[index+1]=="0"||listNumber[index+1]=="-0"){
                    //codigo que rompa el ciclo, resetea las variables y envia un mensaje de la division por 0,
                    // sepodria imprimir la listNumber en su estado actual
                }

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
        }
        return listNumber.first()
    }



}

