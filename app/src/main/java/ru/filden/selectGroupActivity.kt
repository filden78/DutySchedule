package ru.filden

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.filden.logic.ScheduleController

class selectGroupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = ScheduleController()
        setContent {
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit){
                coroutineScope.launch(Dispatchers.IO) {

                }
            }
            val context = LocalContext.current
            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember { mutableStateOf("") }
            var textfieldSize by remember { mutableStateOf(Size.Zero)}
            val icon = if (expanded)
                Icons.Filled.ArrowDropUp
            else
                Icons.Filled.ArrowDropDown
            Column {
                Box() {
                    OutlinedTextField(
                        value = selectedText,
                        onValueChange = { selectedText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                            .onGloballyPositioned { coordinates ->
                                textfieldSize = coordinates.size.toSize()
                            },
                        label = { Text("Группа") },
                        trailingIcon = {
                            Icon(
                                icon, "contentDescription",
                                Modifier.clickable { expanded = !expanded })
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                    ) {
                        controller.getGroups()?.forEach { label ->
                            DropdownMenuItem(text = { Text(text = label) }, onClick = {
                                selectedText = label
                            })
                        }
                    }
                }
                    // когда будет добавлен вход это будет автоматически определятся, пока что КОСТЫЛЬ бля
                Button(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("currentGroup", selectedText)
                    context.startActivity(intent)
                }) {Text("Войти как студент")}
                Button(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("currentGroup", selectedText)
                    context.startActivity(intent)
                }) {Text("Войти как староста группы")}
                Button(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("currentGroup", selectedText)
                    context.startActivity(intent)
                }) {Text("Войти как преподаватель")}
            }}
    }
}