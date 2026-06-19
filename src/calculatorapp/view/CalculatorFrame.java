package calculatorapp.view;

import javax.swing.*;
import java.awt.*;

import calculatorapp.controller.CalculatorController;

public class CalculatorFrame extends JFrame {

    private CalculatorController controller;
    private JLabel display;

    public CalculatorFrame(CalculatorController controller) {
        this.controller = controller;

        setTitle("Calculator");// ウィンドウタイトル
        setSize(300, 400);// ウィンドウサイズ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 右上閉じるボタンの設定
        setLayout(new BorderLayout());

        createDisplay();
        createButtonPanel();
    }

    // 電卓表示パネル
    private void createDisplay() {
        display = new JLabel("0", SwingConstants.RIGHT);
        // JLabelインスタンスを新しく作成、引数（括弧内の値）で初期値（"0"を右寄せ）の配置を渡す。display変数へ保存
        display.setFont(new Font("Arial", Font.BOLD, 24));
        // displayの文字設定、新設定（フォント名、文字スタイル、文字サイズ）
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // displayの枠設定、空のボーダー,10px四方に余白
        add(display, BorderLayout.NORTH);
        // 表示（displayの「上下左右中央」の5つに分けたうち、上に配置）
    }

    // 電卓数字ボタン
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        // buttonを乗せる為のbuttonPanel(土台)オブジェクトを作成
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        // buttonPanelに整列、格子状5行,4列,5px横間隔,5px縦間隔
        String[] buttons = {
                "7", "8", "9", "÷",
                "4", "5", "6", "×",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C"
        };// JButton(ボタン)オブジェクトに表示したい文字を、順番に並べて配列にする
        for (String text : buttons) {
            // buttonsという配列内の文字分text変数に入れて最後まで繰り返す（拡張for文）
            JButton button = new JButton(text);
            // text変数をラベルとして表示する新しいbutton(ボタン)オブジェクトを作成
            button.setFont(new Font("Arial", Font.BOLD, 14));
            // buttonの文字設定、新設定（フォント名、文字スタイル、文字サイズ）
            button.addActionListener(e -> {
                // buttonにクリック時のイベントリスナーを設置（ラムダ式）
                controller.onButtonPressed(text);
                // controllerへ押したボタンの（text）内容を渡す
            });
            buttonPanel.add(button);
            // 作成したbuttonをbuttonPanel（土台）上に配置
        }
        add(buttonPanel, BorderLayout.CENTER);
        // 表示（buttonPanel（土台）の「上下左右中央」の5つに分けたうち、中央に配置）
    }

    public void updateDisplay(String text) {
        display.setText(text);
        // 引数（text）を画面上のラベル（display）に上書き表示
    }
}