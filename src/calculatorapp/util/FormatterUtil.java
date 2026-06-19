package calculatorapp.util;

import java.math.BigDecimal;

public class FormatterUtil {
    public static String format(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}
