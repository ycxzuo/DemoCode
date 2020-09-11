package com.yczuoxin.reactor.test;

class Bread {
    static { System.out.println("load class: Bread"); }
    Bread() { System.out.println("Bread()"); }
}
class Cheese {
    static { System.out.println("load class: Cheese");}
    Cheese() { System.out.println("Cheese()"); }
}
class Lettuce {
    static { System.out.println("load class: Lettuce"); }
    Lettuce() { System.out.println("Lettuce()"); }
}
class Meal {
    static { System.out.println("load class: Meal"); }
    Meal() { System.out.println("Meal()"); }
}

class Lunch extends Meal {

    static { System.out.println("load class: Lunch"); }
    Lunch() {
        System.out.println("Lunch() 1");
        make();
        System.out.println("Lunch() 2");
    }
    public void make() {
        System.out.println("make lunch");
    }
}

class PortableLunch extends Lunch {
    static { System.out.println("load class: PortableLunch"); }
    PortableLunch() { System.out.println("PortableLunch()"); }
}

public class Sandwich extends PortableLunch {

    private int minutes = 5;
    private Bread b = new Bread();
    private Cheese c = new Cheese();
    private Lettuce l = new Lettuce();

    static { System.out.println("load class: Sandwich"); }

    Sandwich() { 
        System.out.println("Sandwich()");
    }

    public void make() {
        System.out.println("make sandwith with:" + b + "," + c + "," + l);
        System.out.println("need " + minutes + " minutes.");
    }

    public static void main(String[] args) {
        new Sandwich();
    }
}