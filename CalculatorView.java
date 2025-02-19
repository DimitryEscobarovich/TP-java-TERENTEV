import java.util.Scanner;

public class CalculatorView {
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите математическое выражение: ");
        return scanner.nextLine();
    }

    public void displayResult(double result) {
        System.out.println("Результат: " + result);
    }

    public void displayError(String message) {
        System.err.println("Ошибка: " + message);
    }
}
