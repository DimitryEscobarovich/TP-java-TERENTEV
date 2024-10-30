import java.util.ArrayList;
import java.util.List;

class Transaction {
    private final String type;
    private final double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return type + ": " + amount;
    }
}

class Account {
    private final String accountNumber;
    private double balance;
    private final List<Transaction> transactionHistory;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("Депозит", amount));
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction("Вывод", amount));
        } else {
            System.out.println("Недостаточно средств!");
        }
    }

    public void transfer(Account targetAccount, double amount) {
        if (amount <= balance) {
            this.withdraw(amount);
            targetAccount.deposit(amount);
            transactionHistory.add(new Transaction("Перевод на " + targetAccount.getAccountNumber(), amount));
        } else {
            System.out.println("Недостаточно средств для перевода!");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

class Client {
    private final List<Account> accounts;

    public Client(String name) {
        this.accounts = new ArrayList<>();
    }

    public void openAccount(String accountNumber) {
        accounts.add(new Account(accountNumber));
    }

    public void closeAccount(Account account) {
        accounts.remove(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}

class Bank {
    private final List<Client> clients;

    public Bank() {
        clients = new ArrayList<>();
    }

    public void registerClient(String name) {
        clients.add(new Client(name));
    }

    public List<Client> getClients() {
        return clients;
    }

    public void printSummary() {
        for (Client client : clients) {
            System.out.println("Клиент: " + client.getAccounts());
            for (Account account : client.getAccounts()) {
                System.out.println("Аккаунт: " + account.getAccountNumber() + ", Баланс: " + account.getBalance());
                System.out.println("История транзакций: " + account.getTransactionHistory());
            }
        }
    }
}