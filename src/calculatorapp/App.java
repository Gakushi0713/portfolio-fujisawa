package calculatorapp;

import calculatorapp.controller.CalculatorController;

public class App {
    public static void main(String[] args) {
        CalculatorController controller = new CalculatorController();
        controller.showView();
    }
}