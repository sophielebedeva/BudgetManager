package budget.impl;

import budget.api.Menu;
import budget.impl.sortingAlgorithms.SortAllPurchases;
import budget.impl.sortingAlgorithms.SortByType;
import budget.impl.sortingAlgorithms.SortCertainType;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import static budget.impl.TypesOfProducts.*;

public class BudgetManager implements Menu {
    private final int BACK = 5;
    private final int PURCHASES_FOOD = 1;
    private final int PURCHASES_CLOTHES = 2;
    private final int PURCHASES_ENTERTAINMENT = 3;
    private final int PURCHASES_OTHER = 4;
    private final int PURCHASES_ALL = 5;
    private HashMap<Integer, TypesOfProducts> purchases;
    private Double balance;
    Scanner scanner = new Scanner(System.in);
    int choice;
    int sortChoice;
    double incomeAdding;
    DecimalFormat f = new DecimalFormat("0.00");

    public BudgetManager() {
        this.purchases = new HashMap<>();
        this.balance = 0.0;
        this.purchases.put(PURCHASES_FOOD, FOOD);
        this.purchases.put(PURCHASES_CLOTHES, CLOTHES);
        this.purchases.put(PURCHASES_ENTERTAINMENT, ENTERTAINMENT);
        this.purchases.put(PURCHASES_OTHER, OTHER);
    }

    @Override
    public void addIncome() {
        System.out.println();
        System.out.println("Enter income:");
        incomeAdding = scanner.nextDouble();
        balance += incomeAdding;
        System.out.println("Income was added!");
        incomeAdding = 0;
    }

    @Override
    public void addPurchase() {
        System.out.println();
        printAddPurchaseMenu();
        int chooseApurchase = scanner.nextInt();
        while (chooseApurchase != BACK) {
            scanner.nextLine();
            System.out.println("Enter purchase name:");
            String name = scanner.nextLine();
            System.out.println("Enter its price:");
            double purchaseSum = Double.parseDouble(scanner.next());
            System.out.println("Purchase was added!");
            System.out.println();

            balance -= purchaseSum;
            purchases.get(chooseApurchase).addSum(purchaseSum);
            purchases.get(chooseApurchase).products.add(name + " $" + f.format(purchaseSum));
            printAddPurchaseMenu();
            chooseApurchase = scanner.nextInt();
        }
    }

    @Override
    public void showPurchases() {
        System.out.println();
        if (purchases.get(1).products.isEmpty() && purchases.get(2).products.isEmpty()
                && purchases.get(3).products.isEmpty() && purchases.get(4).products.isEmpty()) {
            System.out.println("The purchase list is empty!");
        } else {
            System.out.println("Choose the type of purchases");
            printMenuPurchasesType();
            int choiceForDisplayingPurchs = scanner.nextInt();
            while (choiceForDisplayingPurchs != 6) {
                if (choiceForDisplayingPurchs == PURCHASES_ALL) {
                    Double totalSum = 0.0;
                    System.out.println();
                    System.out.println("All:");
                    for (TypesOfProducts purchase : purchases.values()) {
                        purchase.products.forEach(System.out::println);
                        totalSum += purchase.sum;
                    }
                    System.out.println("Total sum: $" + f.format(totalSum));
                    System.out.println();
                    System.out.println("Choose the type of purchases");
                    printMenuPurchasesType();
                    choiceForDisplayingPurchs = scanner.nextInt();
                } else if (purchases.get(choiceForDisplayingPurchs).products.isEmpty()) {
                    System.out.println(purchases.get(choiceForDisplayingPurchs).name + ":");
                    System.out.println("The purchase list is empty!");
                    System.out.println();
                    System.out.println("Choose the type of purchases");
                    printMenuPurchasesType();
                    choiceForDisplayingPurchs = scanner.nextInt();
                } else {
                    System.out.println();
                    System.out.println(purchases.get(choiceForDisplayingPurchs).name + ":");
                    purchases.get(choiceForDisplayingPurchs).products.forEach(System.out::println);
                    System.out.println("Total sum: $" + f.format(purchases.get(choiceForDisplayingPurchs).sum));
                    System.out.println();

                    System.out.println("Choose the type of purchases");
                    printMenuPurchasesType();
                    choiceForDisplayingPurchs = scanner.nextInt();
                }
            }
        }
    }

    @Override
    public void showBalance() {
        System.out.println();
        System.out.println("Balance: $" + f.format(balance));
    }

    @Override
    public void savePurchases() throws IOException {
        File fileOfPurchases = new File("purchases.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileOfPurchases));
        writer.write("Balance: $" + f.format(balance) + "\n");
        for (TypesOfProducts purchase : purchases.values()) {
            writer.write(purchase.name + ":" + "\n");
            purchase.products.forEach(prod -> {
                try {
                    writer.write(prod + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        writer.close();
        System.out.println("Purchases were saved!");
    }

    @Override
    public void sortPurchases() {
        PurchasesSorter sorter = new PurchasesSorter();
        printSortingMenu();
        sortChoice = scanner.nextInt();
        while (sortChoice != 4) {
            switch (sortChoice) {
                case 1:
                    System.out.println();
                    sorter.setSortingAlgorithm(new SortAllPurchases());
                    sorter.sort();
                    break;
                case 2:
                    sorter.setSortingAlgorithm(new SortByType());
                    sorter.sort();
                    break;
                case 3:
                    printSortingCertainTypeMenu();
                    int sortCertTypeChoice = scanner.nextInt();
                    System.out.println();
                    SortCertainType sortCertainType = new SortCertainType();
                    sortCertainType.setTypeNumber(sortCertTypeChoice);
                    sortCertainType.setPurchases(purchases);
                    sorter.setSortingAlgorithm(sortCertainType);
                    sorter.sort();
                    break;
                case 4:
                    return;
            }
            printSortingMenu();
            sortChoice = scanner.nextInt();
        }
    }

    @Override
    public void loadPurchases() throws IOException {
        File loadedPurchases = new File("purchases.txt");
        Scanner scanner = new Scanner(loadedPurchases);
        TypesOfProducts currentCategory = FOOD;
        Integer currentKey = 1;
        balance = 0.0;
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            if (str.endsWith(":")) {
                for (TypesOfProducts prod : TypesOfProducts.values()) {
                    if (prod.name.toLowerCase(Locale.ROOT).contains(str.substring(0, str.length() - 1).toLowerCase(Locale.ROOT))) {
                        currentCategory = prod;
                        currentKey = getKeyOfProduct(purchases, currentCategory).get();
                    }
                }
            } else if (str.toLowerCase(Locale.ROOT).contains("balance: $")) {
                balance = balance + Double.parseDouble(str.substring(str.lastIndexOf("$") + 1).replace(",","."));
            } else {
                purchases.get(currentKey).products.add(str);
                purchases.get(currentKey).addSum(Double.parseDouble(str.substring(str.lastIndexOf("$") + 1).replace(",",".")));
            }
        }
        System.out.println();
        System.out.println("Purchases were loaded!");
    }

    private static Optional<Integer> getKeyOfProduct(
            Map<Integer, TypesOfProducts> map, TypesOfProducts typesOfProducts) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), typesOfProducts))
                .map(Map.Entry::getKey)
                .findFirst();

    }

    @Override
    public void exit() {
        System.out.println();
        System.out.println("Bye!");
    }

    public void printMenu() {
        System.out.println("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
    }

    public void printAddPurchaseMenu() {
        int i = 1;
        System.out.println("Choose the type of purchase");
        for (TypesOfProducts value : values()) {
            System.out.println(i++ + ") " + value.getName());
        }
        System.out.println("5) Back");
    }

    public void printMenuPurchasesType() {
        System.out.println("Choose the type of purchases\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) All\n" +
                "6) Back");
    }

    public void printSortingMenu() {
        System.out.println();
        System.out.println("How do you want to sort?\n" +
                "1) Sort all purchases\n" +
                "2) Sort by type\n" +
                "3) Sort certain type\n" +
                "4) Back");
    }

    public void printSortingCertainTypeMenu(){
        System.out.println();
        System.out.println("Choose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other");
    }

    public void start() throws IOException {
        printMenu();
        choice = scanner.nextInt();
        while (choice != 0) {
            choiceProcessing();
            System.out.println();
            printMenu();
            choice = scanner.nextInt();
        }
        exit();
    }

    public void choiceProcessing() throws IOException {
        switch (choice) {
            case 1:
                addIncome();
                break;
            case 2:
                addPurchase();
                break;
            case 3:
                showPurchases();
                break;
            case 4:
                showBalance();
                break;
            case 5:
                savePurchases();
                break;
            case 6:
                loadPurchases();
                break;
            case 7:
                sortPurchases();
                break;
            case 0:
                exit();
        }
    }
}