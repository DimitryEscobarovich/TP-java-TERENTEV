import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BankApp extends Application {
    private final Bank bank = new Bank();
    private Client currentClient;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Банковское приложение");

        // Создание элементов интерфейса
        Label clientNameLabel = new Label("ФИО клиента:");
        TextField clientNameField = new TextField();
        Button registerButton = new Button("Зарегистрировать клиента");

        Label accountNumberLabel = new Label("Номер аккаунта:");
        TextField accountNumberField = new TextField();
        Button openAccountButton = new Button("Открыть аккаунт");

        Label amountLabel = new Label("Сумма:");
        TextField amountField = new TextField();
        Button depositButton = new Button("Пополнить");
        Button withdrawButton = new Button("Вывести");

        ComboBox<Account> accountComboBox = new ComboBox<>();
        accountComboBox.setPromptText("Выберите аккаунт");

        Button transferButton = new Button("Перевести на другой аккаунт");
        Text transferAccountNumberText = new Text("Номер аккаунта получателя:");
        TextField transferAccountNumberField = new TextField();

        Text summaryText = new Text();

        GridPane grid = new GridPane();
        grid.add(clientNameLabel, 0, 0);
        grid.add(clientNameField, 1, 0);
        grid.add(registerButton, 2, 0);

        grid.add(accountNumberLabel, 0, 1);
        grid.add(accountNumberField, 1, 1);
        grid.add(openAccountButton, 2, 1);

        grid.add(accountComboBox, 0, 2, 3, 1);
        grid.add(amountLabel, 0, 3);
        grid.add(amountField, 1, 3);
        grid.add(depositButton, 0, 4);
        grid.add(withdrawButton, 1, 4);

        grid.add(transferAccountNumberText, 0, 5);
        grid.add(transferAccountNumberField, 1, 5);
        grid.add(transferButton, 2, 5);

        grid.add(summaryText, 0, 6, 3, 1);

        Scene scene = new Scene(grid, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Обработчики событий
        registerButton.setOnAction(_ -> {
            String name = clientNameField.getText();
            bank.registerClient(name);
            currentClient = bank.getClients().getFirst(); // Простой способ для примера
            summaryText.setText("Клиент " + name + " зарегистрирован.");
        });

        openAccountButton.setOnAction(_ -> {
            if (currentClient != null) {
                String accountNumber = accountNumberField.getText();
                currentClient.openAccount(accountNumber);
                accountComboBox.getItems().add(currentClient.getAccounts().getLast());
                summaryText.setText("Аккаунт " + accountNumber + " открыт.");
            } else {
                summaryText.setText("Сначала зарегистрируйте клиента.");
            }
        });

        depositButton.setOnAction(_ -> {
            Account account = accountComboBox.getValue();
            if (account != null) {
                double amount = Double.parseDouble(amountField.getText());
                account.deposit(amount);
                summaryText.setText("На аккаунт " + account.getAccountNumber() + " пополнено на " + amount);
            } else {
                summaryText.setText("Выберите аккаунт для пополнения.");
            }
        });

        withdrawButton.setOnAction(_ -> {
            Account account = accountComboBox.getValue();

            if (account != null) {
                double amount = Double.parseDouble(amountField.getText());
                account.withdraw(amount);
                summaryText.setText("Со счета " + account.getAccountNumber() + " выведено " + amount);
            } else {
                summaryText.setText("Выберите аккаунт для вывода средств.");
            }
        });

        transferButton.setOnAction(_ -> {
            Account senderAccount = accountComboBox.getValue();
            if (senderAccount != null) {
                String targetAccountNumber = transferAccountNumberField.getText();
                Account targetAccount = findAccountByNumber(targetAccountNumber);
                double amount = Double.parseDouble(amountField.getText());

                if (targetAccount != null) {
                    senderAccount.transfer(targetAccount, amount);
                    summaryText.setText("Переведено " + amount + " со счета " + senderAccount.getAccountNumber() + " на счет " + targetAccount.getAccountNumber());
                } else {
                    summaryText.setText("Аккаунт " + targetAccountNumber + " не найден.");
                }
            } else {
                summaryText.setText("Выберите аккаунт для перевода.");
            }
        });
    }

    private Account findAccountByNumber(String accountNumber) {
        for (Client client : bank.getClients()) {
            for (Account account : client.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }
}