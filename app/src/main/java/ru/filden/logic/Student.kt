package ru.filden.logic;

public class Student {




    private  String name;
    private final int UUID;
    private int countDuty;

    public Student(String name, int uuid, int countDuty){
        this.name = name;
        this.UUID = uuid;
        this.countDuty = countDuty;

    }

    public String getName() {
        return name;
    }
    public int getcountDuty() {
        return countDuty;
    }
    public int getUUID() {
        return UUID;
    }
    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public Student setCountDuty(int priority) {
        this.countDuty = priority;
        return this;
    }
    public Student increnment(){
        this.countDuty++;
        return this;
    }
    public Schedule.Pair PairOf(Student second){
        return new Schedule.Pair(this, second);
    }
}
