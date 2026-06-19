package calculatorapp.controller;

import calculatorapp.model.CalculatorModel;
import calculatorapp.view.CalculatorFrame;

public class CalculatorController {
    private CalculatorModel model;
    private CalculatorFrame view;

    public CalculatorController() {
        System.out.println("Controller 起動");
        model = new CalculatorModel();
        view = new CalculatorFrame(this);
    }

    public void showView() {
        System.out.println("view 表示");
        view.setVisible(true);// ウィンドウ表示指示
    }

    public void onButtonPressed(String value) {
        System.out.println("押された：" + value);
        if (value.matches("\\d")) {
            model.appendDigit(value);
        } else if (value.equals(".")) {
            model.appendDot();
        } else if (value.equals("=")) {
            model.equalsOp();
        } else if (value.equals("C")) {
            model.clear();
        } else {
            model.inputOperator(value);
        }
        view.updateDisplay(model.getDisplayText());// 押下数字更新ウィンドウ表示指示
    }
}