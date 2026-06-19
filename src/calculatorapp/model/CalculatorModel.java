package calculatorapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import calculatorapp.util.FormatterUtil;

public class CalculatorModel {

    private InputState state = InputState.READY;
    private String currentInput = "0";
    private BigDecimal leftValue = null;
    private String currentOperator = null;

    public CalculatorModel() {

        System.out.println("Model 起動");
    }

    public void appendDigit(String digit) {

        if (state == InputState.ERROR) {
            return;
        }
        if (state == InputState.READY || state == InputState.INPUT_OPERATOR) {
            currentInput = digit;
            state = InputState.INPUT_NUMBER;
            return;
        }
        if (state == InputState.INPUT_NUMBER) {
            if (currentInput.replace(".", "").replace("-", "").length() >= 8) {
                return;
            }
            if (currentInput.equals("0")) {
                currentInput = digit;
            } else if (currentInput.equals("-")) {
                currentInput += digit;
            } else {
                currentInput += digit;
            }
        }
    }

    public void appendDot() {

        if (state == InputState.ERROR) {
            return;
        }
        if (state == InputState.READY || state == InputState.INPUT_OPERATOR) {
            currentInput = "0.";
            state = InputState.INPUT_NUMBER;
            return;
        }
        if (state == InputState.INPUT_NUMBER) {
            if (!currentInput.contains(".")) {
                currentInput += ".";
            }
        }
    }

    public void inputOperator(String operator) {
        if (state == InputState.ERROR) {
            return;
        }
        if (currentInput.equals("-")) {
            return;
        }
        if (operator.equals("-")) {
            if (state == InputState.READY) {
                currentInput = "-";
                state = InputState.INPUT_NUMBER;
                return;
            }
            if (state == InputState.INPUT_OPERATOR) {
                currentInput = "-";
                state = InputState.INPUT_NUMBER;
                return;
            }
        }
        if (state == InputState.INPUT_NUMBER && leftValue != null) {
            BigDecimal rightValue = new BigDecimal(currentInput);
            leftValue = calculate(leftValue, rightValue, currentOperator);
            if (state == InputState.ERROR) {
                return;
            }
            currentInput = FormatterUtil.format(leftValue);
        }
        if (state == InputState.INPUT_NUMBER) {
            leftValue = new BigDecimal(currentInput);
            currentOperator = operator;
            state = InputState.INPUT_OPERATOR;
            return;
        }
        if (state == InputState.INPUT_OPERATOR) {
            currentOperator = operator;
        }
    }

    public void equalsOp() {
        if (state == InputState.ERROR) {
            return;
        }
        if (state == InputState.INPUT_OPERATOR) {
            return;
        }
        if (leftValue == null || currentOperator == null) {
            return;
        }
        if (currentInput.equals("-")) {
            return;
        }

        BigDecimal rightValue = new BigDecimal(currentInput);

        BigDecimal result = calculate(leftValue, rightValue, currentOperator);

        if (state == InputState.ERROR) {
            return;
        }

        currentInput = FormatterUtil.format(result);

        leftValue = null;
        currentOperator = null;
        state = InputState.READY;
    }

    public void clear() {
        currentInput = "0";
        leftValue = null;
        currentOperator = null;
        state = InputState.READY;
    }

    private BigDecimal calculate(BigDecimal left, BigDecimal right, String operator) {

        switch (operator) {
            case "+":
                return left.add(right);
            case "-":
                return left.subtract(right);
            case "×":
                return left.multiply(right);
            case "÷":
                if (right.compareTo(BigDecimal.ZERO) == 0) {
                    state = InputState.ERROR;
                    currentInput = "ERROR";
                    return BigDecimal.ZERO;
                }
                return left.divide(right, 10, RoundingMode.HALF_UP);
            default:
                return right;
        }
    }

    public String getDisplayText() {
        return currentInput;
    }
}
