package ru.filden.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Schedule {

    private ArrayList<Student> QueuedStudents;


    public Schedule(ArrayList<Student> students){
        QueuedStudents = new ArrayList<>(students.size());
        QueuedStudents.addAll(students);
    }

    private void updatePriority(){
        ArrayList<Student> temp = new ArrayList<>();
        QueuedStudents.stream()
                .filter(s->s.getPriority()!=99)
                .sorted(Comparator.comparingInt(Student::getPriority))
                .forEach(temp::add);
        QueuedStudents.clear();
        QueuedStudents.addAll(temp);
    }
    private void decrementPriority(ArrayList<Student> s){
        for(int i = 1; i<s.size();i++){
            s.set(i, s.get(i).setPriority(s.get(i).getPriority()-1));
        }
    }
    private int getMaxPriority() {
        return QueuedStudents.stream()
                .filter(s -> s.getPriority() < 99)
                .mapToInt(Student::getPriority)
                .max()
                .orElse(0);
    }

    //main
    public Student getFirstDutyStudent(){
        return QueuedStudents.get(0);
    }
    public Student getSecondDutyStudent(){
        return QueuedStudents.get(1);
    }
    public boolean completeDutyStudent(Student student, Boolean IsDuty){
        if(student.getPriority()<99 && IsDuty){
            student.setPriority(getMaxPriority()+1);
            updatePriority();
            decrementPriority(QueuedStudents);
            return true;
        }
        else {
            student.setPriority(getSecondDutyStudent().getPriority() + 1);
            updatePriority();
            return false;
        }
    }



    //serialization
    public Map<Integer, String> getFullSchedule(){
        return QueuedStudents.stream()
                .collect(Collectors.toMap(Student::getPriority, Student::getName));
    }
    public List<Student> getStudents(){
        return new ArrayList<>(QueuedStudents);
    }
}
