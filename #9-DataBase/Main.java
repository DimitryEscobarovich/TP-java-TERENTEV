public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.registerClient("Иван Иванов");

        Client client = bank.getClients().getFirst();
        client.openAccount("12345");

        Account account = client.getAccounts().getFirst();
        account.deposit(1000);
        account.withdraw(200);

        Account anotherAccount = new Account("67890");
        anotherAccount.deposit(500);

        account.transfer(anotherAccount, 300);

        bank.printSummary();
    }
}