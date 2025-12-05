package ru.filden.logic;

import java.util.*;

public class Schedule {
    private ArrayList<Student> students;
    private Pair currentPair;

    public Schedule(ArrayList<Student> s){
        ArrayList<Student> temp = new ArrayList<>();
        s.stream().sorted(Comparator.comparingInt(Student::getcountDuty))
                .forEach(temp::add);
        this.students = temp;
        currentPair = students.get(0).PairOf(students.get(1));
    }
    private void updateStudent(){
        ArrayList<Student> temp = new ArrayList<>();
        students.stream().sorted(Comparator.comparingInt(Student::getcountDuty))
                .forEach(temp::add);
        students.clear();
        students = temp;

    }
    private void updateCurrentPair(){
        currentPair = students.get(0).PairOf(students.get(1));
    }

    public Pair getCurrentPair() {
        return currentPair;
    }

    public void putCurrentPair(Pair p){
        currentPair = p;
    }
    public void completeDuty(Pair p){
        processDutyStudent(p.getFirst());
        processDutyStudent(p.getSecond());
    }
    private void processDutyStudent(Student s){
        s.increnment();
        updateStudent();
        updateCurrentPair();
    }

    public ArrayList<Student> getStudents(){
        return students;
    }

    public static class Pair{
        private final Student first;
        private final Student second;
        Pair(Student s1, Student s2){
            first=s1;
            second=s2;
        }

        public Student getFirst() {
            return first;
        }

        public Student getSecond() {
            return second;
        }
    }
}