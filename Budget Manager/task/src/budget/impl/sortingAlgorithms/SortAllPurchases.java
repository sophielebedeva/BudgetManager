package budget.impl.sortingAlgorithms;

import budget.api.SortingAlgorithm;
import budget.impl.TypesOfProducts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortAllPurchases implements SortingAlgorithm {
    @Override
    public void sort() {
        Comparator<String> comparatorByPrice = (purchase1, purchase2) -> {
            Double purchase1price = Double.parseDouble(purchase1.substring(purchase1.lastIndexOf("$") + 1).replace(",","."));
            Double purchase2price = Double.parseDouble(purchase2.substring(purchase2.lastIndexOf("$") + 1).replace(",","."));
            if (purchase1price.equals(purchase2price)) return 0;
            if (purchase1price > purchase2price) return -1;
            return 1;
        };
        ArrayList<String> allPurchasesForSorting = new ArrayList<>();
        Double totalSum = 0.0;
        DecimalFormat f = new DecimalFormat("0.00");
        for (TypesOfProducts product : TypesOfProducts.values()) {
            allPurchasesForSorting.addAll(product.getProducts());
            totalSum += product.getSum();
        }
        if (allPurchasesForSorting.isEmpty()) {
            System.out.println("The purchase list is empty!");
        } else {
            List<String> sortedPurchases = allPurchasesForSorting
                    .stream()
                    .sorted(comparatorByPrice)
                    .collect(Collectors.toList());
            System.out.println();
            System.out.println("All:");
            sortedPurchases.forEach(System.out::println);
            System.out.println("Total: $" + f.format(totalSum));
        }
    }
}