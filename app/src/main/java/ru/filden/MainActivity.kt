package ru.filden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.filden.logic.Schedule

import ru.filden.logic.resourceManager
import ru.filden.logic.ScheduleController
import ru.filden.logic.Student
import java.util.ArrayList
import java.util.Random


class MainActivity : ComponentActivity() {

    var schedule:  Schedule = Schedule(arrayListOf(
        Student("qwe", 1,0),
        Student("asd", 2,0),
        Student("zxc", 3,1),
        Student("123", 4,2),
        Student("ert", 5,1),
        Student("dfg", 6,3)))
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
    val controller: ScheduleController = ScheduleController(schedule)
    val navController = rememberNavController()
    Column {
        navBar(navController)
        NavHost(navController, startDestination = NavRoutes.mainPage.route){
            composable ( NavRoutes.mainPage.route ){mainPage(controller)}
            composable ( NavRoutes.students.route ){students(controller)}
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
fun mainPage(controller: ScheduleController){
    val pair = remember { mutableStateOf(controller.getSchedule().currentPair) }
    val message = remember { mutableStateOf(pair.value.first.name +"\n"+pair.value.second.name) }

    Surface(Modifier.fillMaxSize().padding(top=150.dp, bottom = 150.dp, start = 50.dp, end = 50.dp)) {
        Column {
            Text(
                text = "Следующие дежурные: \n${message.value}", fontSize = 22.sp
            )
            Button(
                onClick = { controller.getSchedule().completeDuty(controller.getSchedule().currentPair)
                    pair.value=controller.getSchedule().currentPair
                    message.value = pair.value.first.name +"\n"+pair.value.second.name
                          },

                modifier = Modifier.padding(end = 10.dp)
            ) {
                Text("Продежурили", fontSize = 25.sp)
            }
        }
    }
}



@Composable
fun students(controller: ScheduleController) {
    val students = remember { mutableStateListOf<Student>() }
    students.addAll(controller.getStudents())
    val enable = remember { mutableStateOf(false) }
    LazyColumn {
        items(students.size) { index ->
            Row{
                Text(text = students[index].uuid.toString(), Modifier.padding(8.dp, top = 32.dp, end = 8.dp, bottom = 32.dp))
                TextField(
                    value = students[index].name,
                    onValueChange = { students[index].name = it },
                    Modifier.padding(16.dp).size(width = 160.dp, height = 60.dp),
                    enabled = enable.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)

                )
                TextField(
                    value = students[index].getcountDuty().toString(),
                    onValueChange = { students[index].setCountDuty(it.toIntOrNull() as Int) },
                    modifier = Modifier.padding(16.dp).size(width = 80.dp, height = 60.dp),
                    enabled = enable.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

                )
                IconButton(
                    onClick = {students.remove(students[index])
                                     controller.setStudents(ArrayList(students))},
                    Modifier.padding(16.dp),
                    enabled = enable.value,)
                {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = "Удалить"
                    )
                }
            }
        }
    }

}

@Composable
fun qr(){

}
sealed class NavRoutes(val route: String){
    object mainPage: NavRoutes("mainPage")
    object students: NavRoutes("students")
    object qr: NavRoutes("qr")
}
