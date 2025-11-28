package ru.filden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


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
        Main(schedule)
        }
    }
}
@Composable
fun Main(schedule: Schedule){
    val navController = rememberNavController()
    Column {
        navBar(navController)
        NavHost(navController, startDestination = NavRoutes.mainPage.route){
            composable ( NavRoutes.mainPage.route ){mainPage(schedule)}
            composable ( NavRoutes.students.route ){students()}
            composable ( NavRoutes.qr.route ){qr()}
        }
    }
}
@Composable
fun navBar(navController: NavController){
    Row(Modifier.padding(top=80.dp)){
        Text("Дежурные", Modifier.clickable{navController.navigate(NavRoutes.mainPage.route)}.weight(0.28f), fontSize = 22.sp)
        Text("Редактирование", Modifier.clickable{navController.navigate(NavRoutes.students.route)}.weight(0.52f), fontSize = 22.sp)
        Text("QR", Modifier.clickable{navController.navigate(NavRoutes.qr.route)}.weight(0.20f), fontSize = 22.sp)
    }
}
@Composable
fun mainPage(schedule: Schedule){
    val message = remember { mutableStateOf(schedule.firstDutyStudent) }

    Surface(Modifier.fillMaxSize().padding(top=150.dp, bottom = 150.dp, start = 50.dp, end = 50.dp)) {
        Column {
            Text(
                text = "Следующий дежурный: \n" + message.value.name, fontSize = 22.sp
            )
            Button(
                onClick = { schedule.completeDutyStudent(schedule.firstDutyStudent,
                    true)
                    message.value=schedule.firstDutyStudent},
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Text("Продежурил", fontSize = 25.sp)
            }
            Button(onClick = {
                schedule.completeDutyStudent(
                    schedule.firstDutyStudent,
                    false)
                message.value=schedule.firstDutyStudent
            }) {
                Text("Не продежурил", fontSize = 25.sp)
            }

        }
    }
}

@Composable
fun students(){

}
@Composable
fun qr(){

}
sealed class NavRoutes(val route: String){
    object mainPage: NavRoutes("mainPage")
    object students: NavRoutes("students")
    object qr: NavRoutes("qr")
}
