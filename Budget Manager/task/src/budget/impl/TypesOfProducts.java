package budget.impl;

import java.util.ArrayList;

public enum TypesOfProducts {
    FOOD("Food"),
    CLOTHES("Clothes"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other");

    String name;
    double sum;
    ArrayList<String> products;

    public ArrayList<String> getProducts() {
        return products;
    }

    TypesOfProducts(String name) {
        this.name = name;
        this.sum = 0.0;
        this.products = new ArrayList<>();
    }

    public void addSum(double newSum) {
        this.sum += newSum;
    }

    public String getName() {
        return name;
    }
    public double getSum() {
        return sum;
    }
}