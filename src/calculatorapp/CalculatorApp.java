package calculatorapp;

import calculatorapp.controller.CalculatorController;

public class CalculatorApp {
    public static void main(String[] args) {
        CalculatorController controller = new CalculatorController();
        controller.showView();
    }
}