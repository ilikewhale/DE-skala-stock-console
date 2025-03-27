// 클래스 상속
class PlayerStock extends Stock {
    private int stockQuantity;

    // 상속을 고려한 constructor
    public PlayerStock(Stock stock, int quantity) {
        this.stockName = stock.getStockName();
        this.stockPrice = stock.getStockPrice();
        this.stockQuantity = quantity;
    }

    // 파일에서 읽은 정보로 객체를 생성하기 위한 constructor
    public PlayerStock(String name, String price, String quantity) {
        this.stockName = name;
        this.stockPrice = Integer.parseInt(price);
        this.stockQuantity = Integer.parseInt(quantity);
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(int count) {
        this.stockQuantity = count;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(":");
        sb.append(this.stockQuantity);
        return sb.toString();
    }
}
