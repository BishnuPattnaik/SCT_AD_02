package eu.tutorials.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import eu.tutorials.todolistapp.ui.theme.TodoListAppTheme
import kotlin.random.Random

data class Task(
    val id: Int,
    var description: String,
    var color: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskScreen()
                }
            }
        }
    }
}

@Composable
fun TaskScreen() {
    var tasks = remember { mutableStateListOf<Task>() }
    var taskDescription by remember { mutableStateOf("") }
    var taskIdCounter by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Input field to add a new task
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                placeholder = { Text("Enter a task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (taskDescription.isNotBlank()) {
                    val randomColor = getRandomColor()
                    tasks.add(Task(id = taskIdCounter++, description = taskDescription, color = randomColor))
                    taskDescription = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task list
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onDelete = { tasks.remove(task) },
                    onEdit = { newDescription ->
                        task.description = newDescription
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedDescription by remember { mutableStateOf(task.description) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Task Circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            Canvas(
                modifier = Modifier.size(100.dp)
            ) {
                drawCircle(
                    color = task.color,
                    radius = size.minDimension / 2
                )
            }
            // Task description inside the circle
            if (isEditing) {
                BasicTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onEdit(editedDescription)
                            isEditing = false
                        }
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Text(
                    text = task.description,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Edit and Delete buttons below the circle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { isEditing = !isEditing }) {
                Text(if (isEditing) "Save" else "Edit")
            }
            Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(Color.Red)) {
                Text("Delete")
            }
        }
    }
}

// Function to get random color for each task
fun getRandomColor(): Color {
    val colors = listOf(
        Color(0xFFFFA726),  // Orange
        Color(0xFF66BB6A),  // Green
        Color(0xFF42A5F5),  // Blue
        Color(0xFFAB47BC),  // Purple
        Color(0xFFFF7043),  // Deep Orange
        Color(0xFFEC407A),  // Pink
        Color(0xFF26A69A)   // Teal
    )
    return colors[Random.nextInt(colors.size)]
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoListAppTheme {
        TaskScreen()
    }
}
