package com.example.calculadora

sealed class CalculatorActions {
    data class OnShowResultOperation(val result: String): CalculatorActions()
    data class OnShowFullOperation(val result: String): CalculatorActions()
}
