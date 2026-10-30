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
    private Operator currentOperator = null;
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
        if (state == InputState.READY
                || state == InputState.INPUT_OPERATOR) {

            currentInput = digit;
            state = InputState.INPUT_NUMBER;

            updateDisplay();
            return;
        }

        if (state == InputState.INPUT_NUMBER) {

            // 数字部分が8桁以上の時、入力しない
            if (currentInput.replace(".", "")
                    .replace("-", "")
                    .length() >= 8) {
                return;
            }

            if (currentInput.equals("0")) {
                currentInput = digit;

            } else if (currentInput.equals("-")) {
                currentInput += digit;

            } else {
                currentInput += digit;
            }

            updateDisplay();
        }
    }

    /**
     * 小数点を入力する。
     */
    public void appendDot() {

        if (state == InputState.ERROR) {
            return;
        }

        // 数値入力中でなければ入力できない
        if (state == InputState.READY) {
            return;
        }

        // 小数点がすでにある場合、入力しない
        if (state == InputState.INPUT_NUMBER) {

            if (currentInput.equals("-")) {
                currentInput += "0.";
            } else if (!currentInput.contains(".")) {

                currentInput += ".";
            }

            // 表示更新
            updateDisplay();
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

        /*
         * 初期状態で「-」が押された場合、
         * 演算子ではなく負号として入力開始
         */
        if (state == InputState.READY
                && operator.equals("-")) {

            currentInput = "-";
            state = InputState.INPUT_NUMBER;

            updateDisplay();
            return;
        }

        /*
         * ボタンから渡された文字列を
         * Operator型に変換する
         */
        Operator op;

        switch (operator) {
            case "+":
                op = Operator.ADD;
                break;

            case "-":
                op = Operator.SUB;
                break;

            case "×":
                op = Operator.MUL;
                break;

            case "÷":
                op = Operator.DIV;
                break;

            default:
                return;
        }

        // 数値入力中
        if (state == InputState.INPUT_NUMBER) {

            // すでに左辺と演算子がある場合
            // 左から順番に計算する
            if (leftValue != null && currentOperator != null) {

                BigDecimal rightValue = new BigDecimal(currentInput);

                leftValue = calculate(
                        leftValue,
                        rightValue,
                        currentOperator);

                if (state == InputState.ERROR) {
                    return;
                }

                currentInput = FormatterUtil.format(leftValue);

            } else {

                // 最初の演算子入力
                // 現在の数値を左辺として保存
                leftValue = new BigDecimal(currentInput);
            }

            // 新しい演算子を保存
            currentOperator = op;
            state = InputState.INPUT_OPERATOR;

            updateDisplay();
            return;
        }

        // 演算子入力直後なら、演算子を上書き
        if (state == InputState.INPUT_OPERATOR) {

            currentOperator = op;

            updateDisplay();
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

        updateDisplay();
    }

    /**
     * 電卓を初期状態に戻す。
     */
    public void clear() {
        currentInput = "0";
        leftValue = null;
        currentOperator = null;
        state = InputState.READY;

        updateDisplay();
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
            BigDecimal right, Operator operator) {

        switch (operator) {
            case ADD:
                return left.add(right);

            case SUB:
                return left.subtract(right);

            case MUL:
                return left.multiply(right);

            case DIV:

                // 0で割った時
                if (right.compareTo(BigDecimal.ZERO) == 0) {
                    state = InputState.ERROR;
                    currentInput = "エラー";
                    displayText = "エラー";

                    return BigDecimal.ZERO;
                }

                return left.divide(
                        right,
                        10,
                        RoundingMode.HALF_UP);

            default:
                return right;
        }
    }

    // 表示更新
    private void updateDisplay() {

        if (leftValue != null
                && currentOperator != null) {

            displayText = FormatterUtil.format(leftValue)
                    + " "
                    + getOperatorSymbol();

            if (state == InputState.INPUT_NUMBER) {

                displayText += " " + currentInput;
            }

        } else {

            displayText = currentInput;
        }
    }

    /**
     * Operatorを画面表示用の記号に変換
     * 
     * @return 演算子記号
     */
    private String getOperatorSymbol() {

        switch (currentOperator) {

            case ADD:
                return "+";

            case SUB:
                return "-";

            case MUL:
                return "×";

            case DIV:
                return "÷";

            default:
                return "";
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
