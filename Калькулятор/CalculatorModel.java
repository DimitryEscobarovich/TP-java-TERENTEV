class CalculatorModel {
    private String expression;
    private double result;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void calculate() {
        try {
            expression = expression.replaceAll("//", "Math.floor(1.0*");
            expression = expression.replaceAll("Math\\.floor\\(1\\.0\\*(\\d+)/(\\d+)", "Math.floor(1.0*$1/$2)");
            expression = expression.replaceAll("(\\d+)\\^(\\d+)", "Math.pow($1,$2)");
            result = evaluate(expression);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public double getResult() {
        return result;
    }

    double evaluate(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        eat(')');
                    } else {
                        x = parseFactor();
                    }

                    x = switch (func) {
                        case "Math.pow" -> Math.pow(x, parseFactor());
                        case "Math.floor" -> Math.floor(x);
                        default -> throw new RuntimeException("Unknown function: " + func);
                    };
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}
