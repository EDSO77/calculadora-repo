package com.example.calculadora

import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculationViewModel: ViewModel() {


//    val userModel = MutableLiveData<UserModel>()
//    fun randomUser(){
//    val currentUser = UserProvider.random()
//     userModel.postValue(currentUser)

    private val action = PaySingleLiveEvent<CalculatorActions>()
    fun getActionLiveData() = action as LiveData<CalculatorActions>



    //val fulloperationLiveData = MutableLiveData<String>()
    var fulloperation: String = ""
    //val resultLiveData = MutableLiveData<String>()
    var resultContext : String = ""
    val message = MutableLiveData<String>()

    private var listNumber: MutableList<String> = mutableListOf()
    private var numBuilder: String = ""
    private var subtractionOpen: Boolean = false // cuando se esta ingresando un numero negativo
    private var operatorRequired: Boolean = false
    private var previousOperator: Boolean = true // empieza como true para que la funcion de verificacion, de inmediato indique al usuario que se requiere un numero
    private var point: Boolean = false

    fun setOnClickNumberButton(index: Int, button: Button) {
        button.setOnClickListener {
            if (!operatorRequired) {
                upDateNumAndScreen(index.toString())
                previousOperator = false
            } else {
                message.value = "Se requiere signo"
            }

        }
    }

    fun setOnclickPoint(){
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
            message.value = "Se requiere signo"
        }

    }
    fun setOnCLickIgual(){

        if (!previousOperator && resultContext != "") {
            val aux = resultContext
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
            //resultLiveData.value = buildResult()
            action.value = CalculatorActions.OnShowResultOperation(
                result = buildResult()
            )


            if (fulloperation != "") {
                operatorRequired = true
            }


        } else {
            message.value = "Se requiere numero"
        }

    }



    fun upDateScreen(addedText: String) {
        fulloperation += addedText
        action.value = CalculatorActions.OnShowFullOperation(fulloperation)
       // action.value = CalculatorActions.OnShowResultOperation(
         //   result = buildResult()

    }

   fun upDateNumAndScreen(addedText: String) {
        numBuilder += (addedText)
        upDateScreen(addedText)

    }

   fun addNumBuilderToList() {
        if (numBuilder != "") {
            listNumber.add(numBuilder)
            numBuilder = ""
        }
    }

   fun clearAll() {
       //resultLiveData.value = ""
       action.value = CalculatorActions.OnShowResultOperation("")
       fulloperation = ""
       action.value = CalculatorActions.OnShowFullOperation("")
        numBuilder = ""
        listNumber.clear()
        point = false
        previousOperator = true
        operatorRequired = false
        subtractionOpen = false


    }

   fun numberOnChangeListener(operationCalculator: OperationCalculator) {

        operatorRequired = false

        if (!previousOperator) {
            if (subtractionOpen == true) {
                upDateScreen(")")
                subtractionOpen = false
            }
            if(resultContext!="") { // para resetear tvCalResult despues de haber presionado "="
                fulloperation="(${fulloperation})"
                action.value = CalculatorActions.OnShowFullOperation(fulloperation)
                //resultLiveData.value = ""
                action.value = CalculatorActions.OnShowResultOperation("")
            }

            addNumBuilderToList()
            listNumber.add(operationCalculator.value)
            upDateScreen(operationCalculator.value)
            previousOperator = true
            point = false

        } else if (operationCalculator == OperationCalculator.RESTA && fulloperation == "") {

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

   fun buildResult(): String {
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
                        message.value = msg
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











