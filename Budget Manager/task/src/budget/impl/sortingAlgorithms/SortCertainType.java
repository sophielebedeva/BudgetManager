package budget.impl.sortingAlgorithms;

import budget.api.SortingAlgorithm;
import budget.impl.TypesOfProducts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SortCertainType implements SortingAlgorithm {
    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }

    int typeNumber = 0;
    HashMap<Integer, TypesOfProducts> purchases;

    public void setPurchases(HashMap<Integer, TypesOfProducts> purchases) {
        this.purchases = purchases;
    }

    @Override
    public void sort() {
        ArrayList<String> certainTypePurchases = new ArrayList<>();
        Double totalSum = 0.0;
        DecimalFormat f = new DecimalFormat("0.00");

        certainTypePurchases.addAll(purchases.get(typeNumber).getProducts());
        totalSum = purchases.get(typeNumber).getSum();
        if (certainTypePurchases.isEmpty()) {
            System.out.println("The purchase list is empty!");
        } else {
            Comparator<String> comparatorByCertType = (purchase1, purchase2) -> {
                Double purchase1sum = Double.parseDouble(purchase1.substring(purchase1.lastIndexOf("$") + 1).replace(",", "."));
                Double purchase2sum = Double.parseDouble(purchase2.substring(purchase2.lastIndexOf("$") + 1).replace(",", "."));
                if (purchase1sum.equals(purchase2sum)) return 0;
                if (purchase1sum > purchase2sum) return -1;
                return 1;
            };
            List<String> sortedSTPurchases = certainTypePurchases
                    .stream()
                    .sorted(comparatorByCertType)
                    .collect(Collectors.toList());
            System.out.println();
            System.out.println(purchases.get(typeNumber).getName() + ":");
            sortedSTPurchases.forEach(System.out::println);
            System.out.println("Total: $" + f.format(totalSum));
        }
    }
}