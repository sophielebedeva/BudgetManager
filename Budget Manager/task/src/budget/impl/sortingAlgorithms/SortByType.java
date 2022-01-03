package budget.impl.sortingAlgorithms;

import budget.api.SortingAlgorithm;
import budget.impl.TypesOfProducts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortByType implements SortingAlgorithm {
    @Override
    public void sort() {
        ArrayList<String> allTypesForSorting = new ArrayList<>();
        Double totalSum = 0.0;
        DecimalFormat f = new DecimalFormat("0.00");
        for (TypesOfProducts type : TypesOfProducts.values()) {
            allTypesForSorting.add(type.getName() + " - $" + f.format(type.getSum()));
            totalSum += type.getSum();
        }
        Comparator<String> comparatorByPrice = (type1, type2) -> {
            Double type1sum = Double.parseDouble(type1.substring(type1.lastIndexOf("$") + 1).replace(",", "."));
            Double type2sum = Double.parseDouble(type2.substring(type2.lastIndexOf("$") + 1).replace(",", "."));
            if (type1sum.equals(type2sum)) return 0;
            if (type1sum > type2sum) return -1;
            return 1;
        };
        List<String> sortedTypes = allTypesForSorting
                .stream()
                .sorted(comparatorByPrice)
                .collect(Collectors.toList());
        System.out.println();
        System.out.println("Types:");
        sortedTypes.forEach(System.out::println);
        System.out.println("Total: $" + f.format(totalSum));
        }
    }