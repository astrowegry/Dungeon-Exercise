/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon;

import java.util.HashMap;

/**
 *
 * @author bwisniewsk004
 */
public abstract class Person {

    private Position pos;
//    private int posX;
//    private int posY;
    private char name;
    private boolean active;

    public Person(int posX, int posY, char name) {
        this.pos = new Position(posX, posY);
//        this.posX = posX;
//        this.posY = posY;
        this.active = true;
        this.name = name;
    }

    public void move(Position p) {
        pos.move(p.getX(), p.getY());
//        this.posX += x;
//        this.posY += y;
    }

    public boolean isActive() {
        return this.active;
    }

    public void die() {
        this.active = false;
    }

    @Override
    public String toString() {
        return this.name + " " + this.pos.getX() + " " + this.pos.getY()/* + " " + this.isActive()*/;
    }

    public char getName() {
        return this.name;
    }
    
    public Position getPos() {
        return this.pos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.pos != null ? this.pos.hashCode() : 0);
        hash = 23 * hash + this.name;
        hash = 23 * hash + (this.active ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.name != other.name) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        if (this.pos != other.pos && (this.pos == null || !this.pos.equals(other.pos))) {
            return false;
        }
        return true;
    }
    
    

}
