package dungeon;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //nie-testowy dungeon:
        new Dungeon(10, 10, 5, 14, true).run();

        Scanner reader = new Scanner(System.in);

        // TESTOWE:
//        int dimX;
//        
//        int dimY;
//        
//        int vampireCount;
//        
//        int movesCount;
//        System.out.println("give length, height, vampire count and no of moves:");
//        
//        dimX = reader.nextInt();
//        
//        dimY = reader.nextInt();
//        
//        vampireCount = reader.nextInt();
//        
//        movesCount = reader.nextInt();
//        Dungeon dung = new Dungeon(dimX, dimY, vampireCount, movesCount, true );
//        dung.run();
        // KONIEC TESTU ^
    }
}
