package calculatorapp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * 表示用の数値フォーマットを行うユーティリティクラス。
 */
public class FormatterUtil {
    /**
     * BigDecimalを表示用文字列へ変換する。
     *
     * @param value 変換対象の数値
     * @return 表示用文字列
     */
    public static String format(BigDecimal value) {

        // 末尾の不要な０を削除
        BigDecimal formattedValue = value.stripTrailingZeros();

        // 指数表記を使わない通常の文字列に変換
        String result = formattedValue.toPlainString();

        // 符号と小数点を除いた数字の桁数を取得
        String digits = result
                .replace("-", "")
                .replace(".", "");

        // 8桁以内なら通常表示
        if (digits.length() <= 8) {
            return result;}


        // 指数表記の書式を設定
        DecimalFormatSymbols symbols =
                DecimalFormatSymbols.getInstance(Locale.US);

        DecimalFormat formatter =
                new DecimalFormat("0.0000000E0", symbols);

        // 指数表記に変換
        result = formatter.format(formattedValue);

        // Eを小文字のeに変換
        result = result.replace("E", "e");

        return result;
    }
}
