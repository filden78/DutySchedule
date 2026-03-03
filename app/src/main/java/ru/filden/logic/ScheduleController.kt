package ru.filden.logic


////TEST
class ScheduleController {
    var groups : MutableMap<String, Schedule> = mutableMapOf(Pair("1",
        Schedule(arrayListOf<Student>().apply {
            add(Student("Ивав",1,0))
            add(Student("Андрей",2,0))
            add(Student("Михаил",3,0))
            add(Student("Антон",4,0))
        })),
        Pair("2",
        Schedule(arrayListOf<Student>().apply {
            add(Student("Ольга",5,0))
            add(Student("Анна",6,0))
            add(Student("Светлана",7,0))
            add(Student("Илья",8,0))
        })),
        Pair("3",
            Schedule(arrayListOf<Student>().apply {
                add(Student("Максим",9,0))
                add(Student("Арина",10,0))
                add(Student("Владислав",11,0))
                add(Student("Никита",12,0))
            })),
        Pair("4",
            Schedule(arrayListOf<Student>().apply {
                add(Student("Алёна",13,0))
                add(Student("Лариса",14,0))
                add(Student("Дарья",15,0))
                add(Student("Екатерина",16,0))
            })))

    // suspend fun createCon(){}
    fun getStudents(group: String): ArrayList<Student> {
        var schedule = groups.get(group)?.students
        return schedule?.toMutableList() as ArrayList<Student>

    }
    fun deleteStudent(id: Int): Boolean{
        for(s in groups.values){
            for(st in s.students){
                if(st.uuid == id){
                   s.students.remove(st)
                    return true
                }
            }
        }
        return false
    }
    fun updateStudent(student: Student){
        for(s in groups.values){
            for(st in s.students){
                if(st.uuid == student.uuid){
                    st.countDuty = student.countDuty
                    st.name = student.name
                }
            }
        }
    }
    fun addStudent(student: Student, group: String){
        groups[group]?.students?.add(student)
    }
    fun getGroups() : List<String>?{
        return groups.keys.toList()
    }
}


