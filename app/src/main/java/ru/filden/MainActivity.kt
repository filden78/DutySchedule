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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
        Student("qwe", kotlin.random.Random.nextLong(),0),
        Student("asd", kotlin.random.Random.nextLong(),0),
        Student("zxc", kotlin.random.Random.nextLong(),1),
        Student("123", kotlin.random.Random.nextLong(),2),
        Student("ert", kotlin.random.Random.nextLong(),1),
        Student("dfg", kotlin.random.Random.nextLong(),3)))
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
    val curValues = remember { mutableStateListOf<Student>() }
    for (student in controller.getStudents()) {
        curValues.add(student);
    }
    val newValues = remember { mutableStateListOf<Student>() }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(curValues.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = curValues[index].uuid.toString())
                TextField(
                    value = curValues[index].name,
                    onValueChange = { value -> newValues.add(curValues[index].setName(value)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                TextField(
                    value = curValues[index].getcountDuty().toString(),
                    onValueChange = { value -> newValues.add(curValues[index].setCountDuty(value.toInt())) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        item {

            Button(onClick = {
                val temp: kotlin.collections.ArrayList<Student> = arrayListOf()
                for (s in newValues)
                    temp.add(s)
                controller.setStudents(temp)
            }) {
                Text(text = "Сохранить")
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
