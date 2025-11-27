package ru.filden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.filden.ui.theme.DutyScheduleTheme
import ru.filden.logic.Schedule
import ru.filden.logic.Student
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    var students = arrayListOf<Student>(Student("loh", Random.nextLong(), 1),
    Student("asd", Random.nextLong(), 2),
    Student("qwe", Random.nextLong(), 3),
        Student("123", Random.nextLong(), 4),
        Student("pdf", Random.nextLong(), 5),
    Student("hfgfh", Random.nextLong(), 99))
    var schedule: Schedule = Schedule("123",students)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DutyScheduleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    viewDuty(schedule)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun viewDuty(schedule: Schedule) {
        Column(modifier = Modifier.padding(vertical = 50.dp)) {
          DutyRow(schedule)
            }
        }
@Composable
fun DutyRow(schedule: Schedule) {
    Row(modifier = Modifier.padding(5.dp).border(1.dp, color= Color.Gray)) {
        var text = remember { mutableStateOf(schedule.firstDutyStudent.name)}
        Text(
            text = text.value
        )
        Button(onClick = { schedule.completeDutyStudent(schedule.firstDutyStudent, true)
            text.value = schedule.firstDutyStudent.name
        }) {
            Text("Продежурил")
        }
        Button(onClick = {schedule.completeDutyStudent(schedule.firstDutyStudent, false)}) {
            text.value = schedule.firstDutyStudent.name
            Text("Не Продежурил")
        }
    }
}