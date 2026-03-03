package ru.filden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.filden.logic.Schedule

import ru.filden.logic.ScheduleController
import ru.filden.logic.Student


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        val controller = ScheduleController()
        var schedule: MutableState<Schedule>

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val group = intent.getStringExtra("currentGroup")?: "1"
            schedule = remember { mutableStateOf(Schedule( controller.getStudents(group))) }
            Main(controller, group, schedule)
        }
    }
}

@Composable
fun Main(scheduleController: ScheduleController, group: String, schedule: MutableState<Schedule>){
    val navController = rememberNavController()
    Column {
        navBar(navController)
        NavHost(navController, startDestination = NavRoutes.mainPage.route){
            composable ( NavRoutes.mainPage.route ){mainPage(scheduleController,group,schedule)}
            composable ( NavRoutes.students.route ){students(scheduleController,group,schedule)}
        }
    }
}
@Composable
fun navBar(navController: NavController){

    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 50.dp)){
        Column(Modifier.padding(horizontal = 50.dp)) {
            Text("Дежурства", Modifier.clickable(true, onClick = {navController.navigate(NavRoutes.mainPage.route)}))
        }
        Column(Modifier.padding(horizontal = 50.dp)) {
            Text("Студенты",Modifier.clickable(true, onClick = {navController.navigate(NavRoutes.students.route)}))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainPage(scheduleController: ScheduleController, group: String, schedule: MutableState<Schedule>) {
    val students =
        remember { mutableStateListOf<Student>().apply { addAll(schedule.value.students) } }
    var expanded1 by remember { mutableStateOf(false) }

    var expanded2 by remember { mutableStateOf(false) }
    var pair = remember { mutableStateOf(schedule.value.currentPair) }
    Column(Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(horizontal = 60.dp, vertical = 20.dp)) {
            ExposedDropdownMenuBox(
                expanded = expanded1,
                onExpandedChange = {
                    expanded1 = !expanded1
                }
            ) {
                val icon = if (expanded1)
                    Icons.Filled.ArrowDropUp
                else
                    Icons.Filled.ArrowDropDown
                TextField(
                    label = { },
                    readOnly = true,
                    value = pair.value.first.name,
                    onValueChange = {pair.value.first = schedule.value.getStudentOnName(it)!! },
                    trailingIcon = {
                        Icon(
                            icon, "contentDescription",
                            Modifier.clickable { expanded1 = !expanded1 })
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded1,
                    onDismissRequest = {
                        expanded1 = false
                    }
                ) {
                    students.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption.name) },
                            onClick = {
                                pair.value.first = selectionOption
                                expanded1 = false
                            }
                        )
                    }
                }
            }
        }
    }
    Row(modifier = Modifier.padding(horizontal = 60.dp, vertical = 90.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded2,
            onExpandedChange = {
                expanded2 = !expanded2
            }
        )
        {
            TextField(
                label = { },
                readOnly = true,
                value = pair.value.second.name,
                onValueChange = {pair.value.second = schedule.value.getStudentOnName(it)!! },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded2
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded2,
                onDismissRequest = {
                    expanded2 = false
                }
            ) {
                students.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.name) },
                        onClick = {
                            pair.value.second = selectionOption
                            expanded2 = false
                        }
                    )
                }
            }
        }
    }
    Row(modifier = Modifier.padding(horizontal = 110.dp, vertical = 256.dp)) {
        Button(
            onClick = {
                schedule.value.completeDuty(pair.value)
            }) {
            Text("Продежурили")
        }
    }

}

@Composable
fun students(scheduleController: ScheduleController, group: String, schedule: MutableState<Schedule>) {

}

sealed class NavRoutes(val route: String){
    object mainPage: NavRoutes("mainPage")
    object students: NavRoutes("students")
}
@Preview(showBackground = true)
@Composable
fun view(){
    val controller = ScheduleController()
    Main(controller, "1", remember {  mutableStateOf(Schedule(controller.getStudents("1")))})
}