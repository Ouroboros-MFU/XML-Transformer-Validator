class LoanProduct {
    private int minAmount;
    private int maxAmount;
    private int minMonths;
    private int maxMonths;

    public LoanProduct(int minAmount, int maxAmount, int minMonths, int maxMonths) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minMonths = minMonths;
        this.maxMonths = maxMonths;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getMinMonths() {
        return minMonths;
    }

    public int getMaxMonths() {
        return maxMonths;
    }
}