import java.util.ArrayList;

class Player {
    private String playerId;
    private int playerMoney;
    private ArrayList<PlayerStock> playerStocks = new ArrayList<>();

    public Player() {
    }

    public Player(String id) {
        this.playerId = id;
        this.playerMoney = 10000;
    }

    public void setplayerId(String id) {
        this.playerId = id;
    }

    public String getplayerId() {
        return this.playerId;
    }

    public int getPlayerMoney() {
        return this.playerMoney;
    }

    public void setPlayerMoney(int money) {
        this.playerMoney = money;
    }

    public ArrayList<PlayerStock> getPlayerStocks() {
        return this.playerStocks;
    }

    public void setPlayerStocks(ArrayList<PlayerStock> stocks) {
        this.playerStocks = stocks;
    }

    public void addStock(PlayerStock stock) {
        boolean stockExists = false;

        for (PlayerStock existingStock : playerStocks) {
            if (existingStock.getStockName().equals(stock.getStockName())) {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(existingStock.getStockQuantity() + stock.getStockQuantity());
                stockExists = true;
                break;
            }
        }

        if (!stockExists) {
            playerStocks.add(stock);
        }
    }

    public void updatePlayerStock(PlayerStock stock) {
        for (PlayerStock existingStock : playerStocks) {
            if (existingStock.getStockName().equals(stock.getStockName())) {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(stock.getStockQuantity());
                if (existingStock.getStockQuantity() == 0) {
                    playerStocks.remove(existingStock);
                }
                break;
            }
        }
    }

    public PlayerStock findStock(int index) {
        if (index >= 0 && index < playerStocks.size()) {
            return playerStocks.get(index);
        }
        return null;
    }

    public String getPlayerStocksForFile() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playerStocks.size(); i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(playerStocks.get(i));
        }
        return sb.toString();
    }

    public String getPlayerStocksForMenu() {
        StringBuilder sb = new StringBuilder();
        if (playerStocks.isEmpty()) {
            sb.append("보유한 주식 목록이 없습니다.");
        } else {
            for (int i = 0; i < playerStocks.size(); i++) {
                sb.append(i + 1);
                sb.append(". ");
                sb.append(playerStocks.get(i).toString());
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
