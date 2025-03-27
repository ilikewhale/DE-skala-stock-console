import java.util.Random;

class Stock {
    String stockName;
    int stockPrice;

    public Stock() {
    }

    public Stock(String name, int price) {
        this.stockName = name;
        this.stockPrice = price;
    }

    public String getStockName() {
        return stockName;
    }

    public int getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(int price) {
        this.stockPrice = price;
    }

    public void changeStockPrice() {
        Random random = new Random();
        int fluctuation = random.nextInt(21) - 10; // -10 ~ +10 사이의 랜덤한 값 생성
        this.stockPrice += fluctuation;

        // 가격이 0 이하로 내려가지 않도록 보정
        if (this.stockPrice < 0) {
            this.stockPrice = 0;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(stockName);
        sb.append(":");
        sb.append(stockPrice);
        return sb.toString();
    }
}
