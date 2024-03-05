package fintech.driver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fintech.model.Account;
import fintech.model.Transaction;

public class Driver2 {

    static Map<String, Account> accounts = new TreeMap<String, Account>();
    static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = null;

        while (true) {
            input = scanner.nextLine();
            if (input.equals("---")) {
                break;
            }

            String[] data = input.split("#");

            switch (data[0]) {
                case "create-account":
                    createAccount(data);
                    break;
                case "create-transaction":
                    createTransaction(data);
                    break;
                case "show-account":
                    showAccount(data);
                    break;
                case "show-accounts":
                    showAccounts(data);
                    break;
                case "find-account":
                    findAccount(data);
                    break;
            }
        }
        scanner.close();
    }

    private static void createAccount(String[] data) {
        Account newAccount = new Account(data[1], data[2]);
        if (accounts.get(newAccount.getName()) == null) {
            accounts.put(newAccount.getName(), newAccount);
            System.out.println(newAccount);
        }
    }

    private static void createTransaction(String[] data) {
        Account account = accounts.get(data[1]);
        if (account != null && data[1] != null
                && (Double.parseDouble(data[2]) < 0 || Double.parseDouble(data[2]) > 0)) {
            Transaction newTransaction = new Transaction(data[1], Double.parseDouble(data[2]), data[3], data[4]);
            account.addTransaction(newTransaction);
            transactions.add(newTransaction);
        }
    }

    private static void showAccount(String[] data) {
        String accountName = data[1];
        Account account = accounts.get(accountName);
        if (account != null) {
            System.out.println(account);
            List<Transaction> transactionsForAccount = transactions.stream()
                    .filter(t -> t.getName().equalsIgnoreCase(accountName)
                            || (t instanceof Transaction && ((Transaction) t).getRecipientName()
                                    .equalsIgnoreCase(accountName)))

                    .sorted(Comparator.comparing(Transaction::getPosted_at)).collect(Collectors.toList());
            for (Transaction t : transactionsForAccount) {
                System.out.println(t);
            }
        }

    }

    private static void showAccounts(String[] data) {
        List<Account> sortedAccounts = accounts.values().stream()
                .sorted(Comparator.comparing(Account::getName)).collect(Collectors.toList());

        if (sortedAccounts.isEmpty()) {
            // nothing to do
        } else {
            for (Account account : sortedAccounts) {
                List<Transaction> transactionsForAccount = transactions.stream()
                        .filter(t -> t.getName().equalsIgnoreCase(account.getName()))
                        .sorted(Comparator.comparing(Transaction::getPosted_at)).collect(Collectors.toList());

                System.out.println(account);

                if (transactionsForAccount.isEmpty()) {
                } else {
                    for (Transaction t : transactionsForAccount) {
                        System.out.println(t);
                    }
                }
            }
        }
    }

        private static void findAccount(String[] data) {
        String ownerName = data[1];
        Account account = accounts.values().stream().filter(a -> a.getName().equalsIgnoreCase(ownerName)).findFirst()
                .orElse(null);
        if (account != null) {
            System.out.println(account);
            List<Transaction> transactionsForAccount = transactions.stream()
                    .filter(t -> t.getName().equalsIgnoreCase(ownerName)
                            || (t instanceof Transaction
                                    && ((Transaction) t).getName().equalsIgnoreCase(ownerName)))
                    .sorted(Comparator.comparing(Transaction::getPosted_at))
                    .collect(Collectors.toList());
            for (Transaction t : transactionsForAccount) {
            }
        } else {
        }
    }
}