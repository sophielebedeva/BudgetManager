package budget.impl;

import budget.api.SortingAlgorithm;

public class PurchasesSorter {
    private SortingAlgorithm sortingAlgorithm;

    public void setSortingAlgorithm(SortingAlgorithm sortingAlgorithm) {
        this.sortingAlgorithm = sortingAlgorithm;
    }
    public void sort() {
        this.sortingAlgorithm.sort();
    }
}