package ru.filden.logic

class ScheduleController{
    var schedule: Schedule
    constructor(_students: ArrayList<Student>){
        schedule = Schedule(_students)
    }
    constructor(schedule: Schedule){
        this.schedule = schedule
    }
    fun getStudents(): ArrayList<Student>{
        return schedule.students as ArrayList<Student>
    }
    fun setStudents(_students: ArrayList<Student>){
        schedule = Schedule(_students)
    }
    @JvmName("ktGetSchedule")
    fun getSchedule(): Schedule{
        return schedule
    }

}
