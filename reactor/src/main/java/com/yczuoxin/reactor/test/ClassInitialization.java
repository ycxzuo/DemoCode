package com.yczuoxin.reactor.test;

class Bowl {
    Bowl(int marker) {
        System.out.println("Bowl(" + marker + ")"); // 1
    }
    void f1(int marker) {
        System.out.println("f1(" + marker + ")");
    }
}

class Table {
    Bowl bowl1 = new Bowl(1);
    Table() {
        System.out.println("Table()");
        bowl2.f1(2);
    }
    void f2(int marker) {
        System.out.println("f2(" + marker + ")");
    }
    static Bowl bowl6;
    static Bowl bowl2 = new Bowl(2);
    static void fs(int marker) {
        System.out.println("fs(" + marker + ")");
    }
    static {
        bowl6 = new Bowl(6);
    }
    static Bowl bowl7 = new Bowl(7);
}

class Cupboard {
    Bowl bowl3 = new Bowl(3);
    static Bowl bowl4 = new Bowl(4);
    public Cupboard() {
        System.out.println("Cupboard()");
        bowl4.f1(4);
    }
    void f3(int marker) {
        System.out.println("f3(" + marker + ")");
    }
    static Bowl bowl5 = new Bowl(5);
}

public class ClassInitialization {
    public static void main(String[] args) {
        System.out.println("now in main");
        Table.fs(0);
        System.out.println("Creating new Cupboard in main");
        new Cupboard();
        System.out.println("Creating new Table in main");
        Table table = new Table();
        table.f2(1);
        cupboard.f3(1);
    }

    static Cupboard cupboard = new Cupboard();
}