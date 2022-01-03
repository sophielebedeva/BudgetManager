package budget;

import budget.impl.BudgetManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new BudgetManager().start();
    }
}