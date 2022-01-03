package budget.api;

import java.io.IOException;

public interface Menu {
    void addIncome();
    void addPurchase();
    void showPurchases();
    void showBalance();
    void savePurchases() throws IOException;
    void loadPurchases() throws IOException;
    void sortPurchases();
    void exit();
}