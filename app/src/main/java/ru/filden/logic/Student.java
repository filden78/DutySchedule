package ru.filden.logic;

public class Student {


    private final String name;
    private final long UUID;
    private int priority;

    public Student(String name, long uuid, int priority){
        this.name = name;
        this.UUID = uuid;

        if(priority==0) this.priority = 1;
        else this.priority = priority;
    }

    public String getName() {
        return name;
    }
    public int getPriority() {
        return priority;
    }
    public long getUUID() {
        return UUID;
    }

    public Student setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
