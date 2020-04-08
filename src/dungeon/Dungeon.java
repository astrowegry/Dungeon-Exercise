/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author bwisniewsk004
 */
public class Dungeon {
    //moveVampire - trzeba obsłużyć scenariusz, że wampir chce stanąć na polu z graczem albo innym wampirem
    // zmieniłem moveVampire - żeby zawszse poruszał się w lewo - bo stoopidVampire nie miał sprawdzania pozycji w tej klasie (Dungeon)

    Random random = new Random();
    Scanner reader = new Scanner(System.in);

    private HashSet<Position> takenPositions;

    private HashMap<String, Position> commands;                                     // !!!!!!!!!!!!!!!!!! SUPER WAŻNE

    final private int length;
    final private int height;
    final private Vampire[] vampires;
    private int moves;
    final private boolean vampiresMove;
    Player pc = new Player();

    public Dungeon(int length, int height, int vampires, int moves, boolean vampiresMove) {
        this.length = length;
        this.height = height;
        this.vampires = new Vampire[vampires];
        this.moves = moves;
        this.vampiresMove = vampiresMove;
        this.takenPositions = new HashSet<Position>();
        this.takenPositions.add(this.pc.getPos());

        this.commands = new HashMap<String, Position>();
        this.commands.put("a", new Position(-1, 0));
        this.commands.put("d", new Position(1, 0));
        this.commands.put("w", new Position(0, -1));
        this.commands.put("s", new Position(0, 1));

    }

    public void run() {

//        System.out.println(takenPositions);
// end
        spawnVampires();    //wyłączyć dla testu i wstawić StoopidVampires

        // manage moves etc.:
        int i = this.moves;
        while (areVampiresRoaming(vampires)) {
            if (i == 0) {
                System.out.println("YOU LOSE");
                break;
            }
            playRoundNew(i);
            i--;
        }
        if (!areVampiresRoaming(vampires)) {
            System.out.println("YOU WIN");
        }

//        System.out.println(takenPositions);
    }

    private boolean areVampiresRoaming(Vampire[] vampires) {

        for (Vampire v : vampires) {
            if (v.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void playRoundNew(int move) {
        this.printState(move);
        this.movePlayer(this.pc);
    }

    //UNUSED CLASS - REPLACED BY playRoundNew
    public void playRoundObsolete(int moves) {
        for (int i = moves; i > 0; i--) {

            this.printState(i);
            this.movePlayer(this.pc);

            if (!areVampiresRoaming(vampires)) {
                System.out.println("YOU WIN");
                break;
            }

            //debug
            System.out.println(areVampiresRoaming(vampires));
            //debug end
        }
    }

    private void spawnVampires() { // DO POPRAWY

        for (int i = 0; i < this.vampires.length; i++) {

            spawnOneV(i);
        }

    }

    private void spawnOneV(int i) {
        boolean posTaken;
        int pX;
        int pY;

        do {

            pX = random.nextInt(this.length);
            //test
            //System.out.println("pX:" + pX);
            pY = random.nextInt(this.height);
            //test
            //System.out.println("pY:" + pY);

            //można lepiej
            if (!this.takenPositions.contains(new Position(pX, pY))) {
                posTaken = false;
                //test
                //System.out.println("nie ma");
            } else {
                posTaken = true;
                // test
                //System.out.println("jest");
            }

        } while (posTaken);
        // test
        //System.out.println("ok");
        this.vampires[i] = new Vampire(pX, pY);
        this.takenPositions.add(vampires[i].getPos());

        //System.out.println(this.vampires[i]);
    }

    private void printState(int moves) {
        //print moves left

        System.out.println(/*"moves: " +*/moves + "\n");

        //System.out.println(takenPositions);
        //drukuj stan gracza
        System.out.println(pc.toString());

        //drukuj stany wampirów
        for (Person p : vampires) {
            System.out.println(p.toString());
        }
        //new line
        System.out.print("\n");

        // print board
        printBoard();
        //and new line
        System.out.print("\n");
    }

    private void printBoard() {

        for (int h = 0; h < this.height; h++) {

            // zbuduj array znaków
            char[] chars = new char[this.length];

            for (int l = 0; l < chars.length; l++) {
                chars[l] = '.';

                if (pc.getPos().getY() == h) {
                    chars[pc.getPos().getX()] = pc.getName();
                }
                for (Vampire vampire : vampires) {
                    if (vampire.isActive()) {
                        if (vampire.getPos().getY() == h) {
                            chars[vampire.getPos().getX()] = vampire.getName();
                        }
                    }
                }
            }
            for (char c : chars) {
                System.out.print(c/* + " "*/);
            }
            System.out.print("\n");
        }
    }

    private boolean isMoveBlocked(Person actor, Position command) {
        int i = 0;

        for (Position pos : takenPositions) {
            //System.out.println(pos + "   -  komenda: " + command);
            //System.out.println("nowa pozycja: " + new Position(actor.getPos().getX() + command.getX(), actor.getPos().getY() + command.getY()));
            if (pos.equals(new Position(actor.getPos().getX() + command.getX(), actor.getPos().getY() + command.getY()))) {
                //System.out.println("pozycja zajęta");
                i += 1;
            }
            //System.out.println("pozycja wolna");
        }
        return (i > 0);
//        System.out.println("albo tu dojeżdża");
//        return takenPositions.contains(new Position(actor.getPos().getX() + command.getX(), actor.getPos().getY() + command.getY()));
    }

    private boolean isMoveWithinBounds(Person actor, Position command) {

        boolean right = (actor.getPos().getX() + command.getX() < this.length);

        boolean down = (actor.getPos().getY() + command.getY() < this.height);

        boolean left = (actor.getPos().getX() + command.getX() > -1);

        boolean up = (actor.getPos().getY() + command.getY() > -1);

        return right && down && left && up;
    }

    private void movePlayer(Player pc) {
        String commandsLine = reader.nextLine();

        //debug
//        System.out.println("przed ruchem   " + takenPositions);
        //debug end
        for (int i = 0; i < commandsLine.length(); i++) {
            movePerson(pc, Character.toString((commandsLine.charAt(i))));
            if (vampiresMove) {
                this.moveVampires(vampires);
            }
        }
        //return commandsLine.length();     //po co to było?
    }

    private void movePerson(Person actor, String command) {

        //int x = 0;    nie wiem po co to kurwa było
        //int y = 0;
        Position directions = commands.get(command);
        //debug
//        System.out.println(actor.toString() + " - isMoveBlocked: " + isMoveBlocked(actor, directions));
        //debug end

        if (isMoveWithinBounds(actor, directions)) {

            if (!isMoveBlocked(actor, directions)) {
                actor.move(directions);

                //jeśli Gracz wejdzie na wampira:
            } else if (actor.equals(this.pc)) {
//                System.out.println("attack");
                actor.move(directions);
                findVampire(actor.getPos()).die();
            }
//else {
//                System.out.println("kurwa move blocked");
//            }

        }
    }

    private Vampire findVampire(Position pos) {
        for (Vampire vampire : this.vampires) {
            if (vampire.getPos().equals(pos)) {
                return vampire;
            }
        }
        return null;
    }

    private void moveVampires(Vampire[] vampires) {
        for (Vampire v : vampires) {
            //debug
//            System.out.println("przed ruchem wampira   " + takenPositions);
            //debug end
            moveVampire(v);
            //debug
//            System.out.println("po ruchu wampira   " + takenPositions);
            //debug end
        }
    }

    private void moveVampire(Vampire v) {  // może moveVampires(vampire[]) ???

        String[] moveCommands = {"a", "s", "w", "d"};

        //TEST: zmiana z     int r = random.nextInt(1);
//        int r = 0;
        //TEST END
        int r = random.nextInt(4);

        movePerson(v, moveCommands[r]);
        //System.out.println("v moves: " + commands[r]);

//        if (isMoveBlocked(v, this.commands.get(moveCommands[r]))) {
//            for (Position posSprawdz : takenPositions) {
//                if (v.getPos().equals(posSprawdz)) {
//                    System.out.println(v.getPos() + " - " + posSprawdz);
//                    System.out.println("DUPA3333333");
//                }
//            }
//        }
    }
}
