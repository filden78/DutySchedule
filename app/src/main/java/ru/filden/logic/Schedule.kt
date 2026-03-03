package ru.filden.logic

class Schedule(initialStudents: List<Student>) {
    private val _students = initialStudents.sortedBy { it.countDuty }.toMutableList()
    private var _currentPair: Pair = Pair(_students[0], _students[1])

    val students: MutableList<Student>
        get() = _students.toMutableList()

    var currentPair: Pair
        get() = _currentPair
        set(value) { _currentPair = value }

    fun completeDuty(pair: Pair) {
        _students.replaceAll { student ->
            when (student) {
                pair.first, pair.second -> student.increment()
                else -> student
            }
        }

        // Сортируем заново
        _students.sortBy { it.countDuty }
        _currentPair = Pair(_students[0], _students[1])
    }
    fun getStudentOnName(string: String): Student? {
        for(stud in _students){
            if(stud.name.equals(string)){
                return stud
            }
        }
        return null
    }

    data class Pair(var first: Student, var second: Student)
}