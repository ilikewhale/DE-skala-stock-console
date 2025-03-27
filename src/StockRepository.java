import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class StockRepository {

    // 주식 정보를 저장할 파일 (형식 - "주식명,주가")
    private final String STOCK_FILE = "data/stocks.txt";

    // 주식 정보 목록 (메모리)
    private ArrayList<Stock> stockList = new ArrayList<>();

    public ArrayList<Stock> getStockList() {
        return stockList;
    }

    // 주식 정보를 파일에서 읽어옴
    public void loadStockList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Stock stock = parseLineToStock(line);
                if (stock != null) {
                    stockList.add(stock);
                }
            }
        } catch (IOException e) {
            System.out.println("파일이 없거나 파일을 불러오는 중 오류가 발생했습니다. 주식 정보를 초기화 합니다.");
            // 파일이 없으면 기본 데이터 추가
            stockList.add(new Stock("TechCorp", 100));
            stockList.add(new Stock("GreenEnergy", 80));
            stockList.add(new Stock("HealthPlus", 120));
        }
    }

    // 주식 목록을 파일에 저장
    public void saveStockList() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE))) {
            for (Stock stock : stockList) {
                writer.write(stock.getStockName() + "," + stock.getStockPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("파일에 저장하는 중 오류가 발생했습니다.");
        }
    }

    // 파일 라인을 Stock 객체로 변환
    private Stock parseLineToStock(String line) {
        String[] fields = line.split(",");
        if (fields.length > 1) {
            return new Stock(fields[0], Integer.parseInt(fields[1]));
        } else {
            System.out.println("파일 라인을 분석할 수 없습니다. line=" + line);
            return null;
        }
    }

    public String getStockListForMenu() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stockList.size(); i++) {
            sb.append(i + 1);
            sb.append(". ");
            sb.append(stockList.get(i).toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    // 주식 인덱스로 찾기 (오버로딩)
    public Stock findStock(int index) {
        if (index >= 0 && index < stockList.size()) {
            return stockList.get(index);
        }
        return null;
    }

    // 주식 이름으로 찾기 (오버로딩)
    public Stock findStock(String name) {
        for (Stock stock : stockList) {
            if (stock.getStockName().equals(name)) {
                return stock;
            }
        }
        return null;
    }

    // 새로운 주식을 리스트에 추가하고 파일에 저장
    public void addStock(Stock newStock) {
        stockList.add(newStock);  // 리스트에 주식 추가
        saveStockList();  // 파일에 저장
    }
}
