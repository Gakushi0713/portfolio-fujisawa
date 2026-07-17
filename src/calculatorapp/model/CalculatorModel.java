package calculatorapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import calculatorapp.util.FormatterUtil;

/**
 * 電卓の計算処理と状態管理を行うモデルクラス。
 *
 * 数値入力、演算子入力、計算実行、
 * クリア処理などのロジックを担当する。
 */
public class CalculatorModel {

    private InputState state = InputState.READY;
    private String currentInput = "0";
    private BigDecimal leftValue = null;
    private String currentOperator = null;
    private String displayText = "0";

    /**
     * CalculatorModelを生成する。
     */
    public CalculatorModel() {

        System.out.println("Model 起動");
    }

    /**
     * 数字を入力する。
     *
     * @param digit 入力された数字
     */
    public void appendDigit(String digit) {

        if (state == InputState.ERROR) {
            return;
        }
        if (state == InputState.READY || state == InputState.INPUT_OPERATOR) {
            currentInput = digit;
            state = InputState.INPUT_NUMBER;
            displayText = currentInput;
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

            // 表示更新
            if (leftValue != null && currentOperator != null) {
                displayText = FormatterUtil.format(leftValue)
                        + " " + currentOperator + " " + currentInput;
            } else {
                displayText = currentInput;
            }
        }
    }

    /**
     * 小数点を入力する。
     */
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

            // 表示更新
            if (leftValue != null && currentOperator != null) {
                displayText = FormatterUtil.format(leftValue)
                        + " " + currentOperator + " " + currentInput;
            } else {
                displayText = currentInput;
            }
        }
    }

    /**
     * 演算子を入力する。
     *
     * @param operator 入力された演算子
     */
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
            displayText = FormatterUtil.format(leftValue) + " "
                    + currentOperator;

            if (state == InputState.ERROR) {
                return;
            }
            currentInput = FormatterUtil.format(leftValue);
        }
        if (state == InputState.INPUT_NUMBER) {
            leftValue = new BigDecimal(currentInput);
            currentOperator = operator;

            displayText = FormatterUtil.format(leftValue)
                    + " "
                    + currentOperator;
            state = InputState.INPUT_OPERATOR;
            return;
        }
        if (state == InputState.INPUT_OPERATOR) {
            currentOperator = operator;

            displayText = 
            FormatterUtil.format(leftValue)+" "+currentOperator
        }
    }

    /**
     * 計算を実行し結果を表示する。
     */
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

        displayText = currentInput;

    }

    /**
     * 電卓を初期状態に戻す。
     */
    public void clear() {
        currentInput = "0";
        leftValue = null;
        currentOperator = null;
        state = InputState.READY;

        displayText = "0";
    }

    /**
     * 四則演算を実行する。
     *
     * @param left     左辺値
     * @param right    右辺値
     * @param operator 演算子
     * @return 計算結果
     */
    private BigDecimal calculate(
            BigDecimal left,
            BigDecimal right, String operator) {

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
                    currentInput = "エラー";
                    displayText = "エラー";
                    return BigDecimal.ZERO;
                }
                return left.divide(right, 10, RoundingMode.HALF_UP);
            default:
                return right;
        }
    }

    /**
     * 表示用文字列を取得する。
     *
     * @return 現在の表示内容
     */
    public String getDisplayText() {
        return displayText;
    }
}
