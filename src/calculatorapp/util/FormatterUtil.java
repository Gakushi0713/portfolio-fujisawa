package calculatorapp.util;

import java.math.BigDecimal;

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
        return value.stripTrailingZeros().toPlainString();
    }
}
