
import java.util.Scanner;

class SkalaStockMarket {
    private PlayerRepository playerRepository = new PlayerRepository();
    private StockRepository stockRepository = new StockRepository();
    private Player player = null;
    private Thread stockSimulationThread = null; // 스레드 저장용 변수

    public void start() {
        // 주식 레파지토리에서 주식 정보를 불러옴
        stockRepository.loadStockList();

        // 플레이어 레파지토리에서 플레이어 정보를 불러옴
        playerRepository.loadPlayerList();

        // 콘솔로 입력을 받을 수 있도록 스캐너 설정
        Scanner scanner = new Scanner(System.in);

        System.out.print("플레이어 ID를 입력하세요: ");
        String playerId = scanner.nextLine();
        player = playerRepository.findPlayer(playerId);
        if (player == null) { // 새로운 플레이어
            player = new Player(playerId);

            System.out.print("💰초기 투자금을 입력하세요: ");
            int money = scanner.nextInt();
            player.setPlayerMoney(money);
            playerRepository.addPlayer(player);
        }
        displayPlayerStocks();

        // 프로그램 루프
        boolean running = true;
        while (running) {
            System.out.println("\n=== 스칼라 주식 프로그램 메뉴 ===");
            System.out.println("1️⃣ 보유 주식 목록");
            System.out.println("2️⃣ 주식 구매");
            System.out.println("3️⃣ 주식 판매");
            System.out.println("4️⃣ 주식 추가");
            System.out.println("0️⃣ 프로그램 종료");

            System.out.print("선택: ");
            int code = scanner.nextInt();

            switch (code) {
                case 1:
                    displayPlayerStocks();
                    break;
                case 2:
                    buyStock(scanner);  // 주식 구매 시 실시간 가격 반영
                    break;
                case 3:
                    sellStock(scanner);
                    break;
                case 4:
                    addStock(scanner);  // 주식 추가 기능 호출
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다...Bye");
                    running = false;
                    break;
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }

        scanner.close();
    }

    // 주식 가격 시뮬레이션을 위한 메서드 (60초마다 가격 변동)
    private void startStockMarketSimulation() {
        stockSimulationThread = new Thread(() -> {
            while (true) {
                try {
                    // 모든 주식 가격을 1분마다 랜덤하게 변동
                    for (Stock stock : stockRepository.getStockList()) {
                        stock.changeStockPrice(); // 가격 변경
                        System.out.println("변경된 주식 가격: " + stock);  // 변동된 주식 가격 출력
                    }

                    // 1분 대기 (60초)
                    Thread.sleep(5000);
                    System.out.println("********💡실시간 업데이트 중💡********");
                } catch (InterruptedException e) {
                    System.out.println("실시간 업데이트를 잠시 멈춰드릴게요");
                    break;  // 스레드 종료 시 예외를 처리하고 스레드를 종료합니다.
                }
            }
        });
        stockSimulationThread.start();  // 주식 가격 변동 스레드 시작
    }

    // 주식 목록 표시
    private void displayStockList() {
        System.out.println("\n=== 주식 목록 ===");
        System.out.println(stockRepository.getStockListForMenu());
    }

    // 플레이어의 보유 주식 목록 표시
    private void displayPlayerStocks() {
        System.out.println("\n######### 플레이어 정보 #########");
        System.out.println("- 플레이어ID : " + player.getplayerId());
        System.out.println("- 보유금액 : " + player.getPlayerMoney());
        System.out.println("- 보유 주식 목록");
        System.out.println(player.getPlayerStocksForMenu());
    }

    // 주식 추가 기능
    private void addStock(Scanner scanner) {
        System.out.println("\n새로운 주식을 추가합니다.");
        System.out.print("주식명을 입력하세요: ");
        String stockName = scanner.next();

        System.out.print("주식 가격을 입력하세요: ");
        int stockPrice = scanner.nextInt();

        // 새로운 주식 생성
        Stock newStock = new Stock(stockName, stockPrice);

        // 주식 레파지토리에 추가하고 파일에 저장
        stockRepository.addStock(newStock);
        System.out.println("✔️새로운 주식이 추가되었습니다: " + newStock);
    }

    // 주식 구매
    private void buyStock(Scanner scanner) {
        System.out.println("\n구매할 주식 번호를 선택하세요:");
        System.out.println("\n✅주식 값이 실시간으로 변경되고 있습니다(5초간격)");
        startStockMarketSimulation(); // 주식 목록을 실시간으로 표시

        int index = scanner.nextInt() - 1;

        Stock selectedStock = stockRepository.findStock(index);
        if (selectedStock != null) {
            System.out.print("구매할 수량을 입력하세요: ");

            // 구매 수량 입력시 실시간 주식 가격 변동 중지
            stopStockMarketSimulation();

            int quantity = scanner.nextInt();


            int totalCost = selectedStock.getStockPrice() * quantity;
            int playerMoney = player.getPlayerMoney();
            if (totalCost <= playerMoney) {
                player.setPlayerMoney(playerMoney - totalCost);
                player.addStock(new PlayerStock(selectedStock, quantity));
                System.out.println(quantity + "주를 구매했습니다! 남은 금액: " + player.getPlayerMoney());

                // 변경된 내용을 파일로 저장
                playerRepository.savePlayerList();
            } else {
                System.out.println("‼️ 금액이 부족합니다.");
            }
        } else {
            System.out.println("‼️ 잘못된 선택입니다.");
        }
    }

    // 주식 판매
    private void sellStock(Scanner scanner) {
        System.out.println("\n판매할 주식 번호를 선택하세요:");
        displayPlayerStocks();

        System.out.print("선택: ");
        int index = scanner.nextInt() - 1;

        PlayerStock playerStock = player.findStock(index);
        if (playerStock != null) {
            System.out.print("판매할 수량을 입력하세요: ");
            int quantity = scanner.nextInt();

            // 어얼리 리턴
            if (quantity > playerStock.getStockQuantity()) {
                System.out.println("‼️수량이 부족합니다.");
                return;
            }

            Stock baseStock = stockRepository.findStock(playerStock.getStockName());
            int playerMoney = player.getPlayerMoney() + baseStock.getStockPrice() * quantity;
            player.setPlayerMoney(playerMoney);

            playerStock.setStockQuantity(playerStock.getStockQuantity() - quantity);
            player.updatePlayerStock(playerStock);

            // 변경된 내용을 파일로 저장
            playerRepository.savePlayerList();
        } else {
            System.out.println("‼️잘못된 선택입니다.");
        }
    }

    // 주식 가격 변동 스레드 멈추기
    private void stopStockMarketSimulation() {
        if (stockSimulationThread != null) {
            stockSimulationThread.interrupt(); // 스레드 중지
        }
    }
}



