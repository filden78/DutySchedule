package ru.filden

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import ru.filden.logic.Schedule
import ru.filden.logic.ScheduleController
import ru.filden.logic.Student

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
        Text("Дежурные", Modifier
            .clickable { navController.navigate(NavRoutes.mainPage.route) }
            .weight(0.28f), fontSize = 22.sp)
        Text("Редактирование", Modifier
            .clickable { navController.navigate(NavRoutes.students.route) }
            .weight(0.52f), fontSize = 22.sp)
        Text("QR", Modifier
            .clickable { navController.navigate(NavRoutes.qr.route) }
            .weight(0.20f), fontSize = 22.sp)
    }
}
@Composable
fun mainPage(controller: ScheduleController){
    val pair = remember { mutableStateOf(controller.getSchedule().currentPair) }
    val message = remember { mutableStateOf(pair.value.first.name +"\n"+pair.value.second.name) }

    Surface(Modifier
        .fillMaxSize()
        .padding(top = 150.dp, bottom = 150.dp, start = 50.dp, end = 50.dp)) {
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




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun students(controller: ScheduleController) {
    val dialog = remember { mutableStateOf(false) }
    val students = remember { mutableStateOf(controller.getSchedule().students) }
    val edit = remember { mutableStateOf(false) }
    var column: Unit
    if(!edit.value) {
         column = LazyColumn {
            items(students.value.size) { index ->
                Row {
                    Text(
                        text = students.value[index].uuid.toString(),
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = students.value[index].name,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = students.value[index].getcountDuty()
                            .toString(), fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item {
                Surface(Modifier) {
                    Row(Modifier, horizontalArrangement = Arrangement.Center) {
                        Button(onClick = { dialog.value = true }) { Text(text = "Добавить") }
                        Button(onClick = { edit.value = true }) { Text(text = "Изменить") }
                    }
                }
            }
        }
    }
    if (edit.value){
        val temp = students.value
        column = LazyColumn {
            items(temp.size) { index ->
                Row {
                    Text(
                        text = temp[index].uuid.toString(),
                        modifier = Modifier.padding(16.dp)
                    )
                    TextField(
                        value = temp[index].name,
                        onValueChange = {temp[index].name =it},
                        modifier = Modifier.padding(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    TextField(
                        value = temp[index].getcountDuty()
                            .toString(),
                        onValueChange = {temp[index].setCountDuty(it.toInt())},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(16.dp)

                    )
                }
            }
            item {
                Surface(Modifier) {
                    Button(onClick = {
                        edit.value = false
                        students.value = temp
                    }) { Text(text = "Сохранить")}
                }
            }
        }
    }
    if(dialog.value){
        val id = remember{ mutableStateOf("") }
        val name = remember{mutableStateOf("")}
        val count = remember{ mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { dialog.value = false },
            title = {Text(text="Добавление студента")},
            text = {
                Column(Modifier.fillMaxWidth()) {
                    OutlinedTextField( modifier = Modifier.padding(15.dp),
                        value = id.value,
                        onValueChange = { newText -> id.value = newText },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(modifier = Modifier.padding(15.dp),
                        value = name.value ,
                        onValueChange = { newText -> name.value = newText },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    OutlinedTextField(modifier = Modifier.padding(15.dp),
                        value=count.value,
                        onValueChange = { newText -> count.value = newText },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            },
            confirmButton = {
                Button(onClick = {dialog.value = false
                    controller.getSchedule().students.add(Student(name.value, id.value.toInt(), count.value.toInt()))
                }) {
                    Text("Добавить")
                }
            }
        )
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
