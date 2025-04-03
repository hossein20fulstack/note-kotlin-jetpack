package com.example.noteapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.ui.theme.BackgroundColorLight
import com.example.noteapp.ui.theme.BlueLight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.remember as remember1

class MainActivity : ComponentActivity() {
    val db = DBHelper(this)
    val Dao = Dao_notes(db)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
             var cont= LocalContext.current
            val navcontroller = rememberNavController()
            NavHost(navController = navcontroller, startDestination = "Screen1") {
                composable("Screen1") {
                    Screen1_Main(navcontroller, Dao)
                }
                composable("Screen2") {
                    Screen2_Add(navcontroller, Dao)
                }
                composable("Screen3") {
                    Screen3_Trash(navcontroller, Dao)
                }
                composable("Screen4") {

                    Screen4_showNotes(navcontroller, Dao)
                }
                Toast.makeText(cont, "hello", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

var itemId = 10


@Composable
fun Screen1_Main(navcontroller: NavHostController, Dao: Dao_notes) {
    BackApp()
    DateDisplay()
    BtnDeleteADD(navcontroller)
    ItemList(Dao.findNotes(DBHelper.NOTE_0_STATE), Dao, navcontroller)


}

@Composable
fun Screen2_Add(navcontroller: NavHostController, Dao: Dao_notes) {
    BackApp()
    AddNotesPage(navcontroller, Dao, DateDisplay())

}

@Composable
fun Screen3_Trash(navcontroller: NavHostController, Dao: Dao_notes) {
    BackApp()
    DateDisplay()
    BtnHome(navcontroller)
    ItemListTrash(Dao.findNotes(DBHelper.NOTE_1_STATE), Dao, navcontroller)

}

@Composable
fun Screen4_showNotes(navcontroller: NavHostController, Dao: Dao_notes) {
    BackApp()
    DateDisplay()

    ShowNotePage(navcontroller = navcontroller, daoNotes = Dao, itemId, DateDisplay())
}

@Composable
fun ShowNotePage(
    navcontroller: NavHostController,
    daoNotes: Dao_notes,
    itemId: Int,
    date: String
) {
    val showsNotes = daoNotes.findByid(itemId)

    var itemTitle by remember1 {
        mutableStateOf("${showsNotes.title}")
    }
    var itemDetaill by remember1 {
        mutableStateOf("${showsNotes.detail}")
    }
    val rrr = DBNotesModel(itemId, itemTitle, itemDetaill, DBHelper.NOTE_0_STATE, date)
    BtnUpdate(navcontroller = navcontroller, rrr, daoNotes)
    Row(
        modifier = Modifier
            .padding(top = 90.dp)
            .fillMaxWidth()
            .background(color = Color.Gray)
    ) {
        Column(
            modifier = Modifier.padding(top = 1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 20.dp)
                .fillMaxWidth(),
                value = itemTitle,
                onValueChange = { itemTitle = it },
                label = { Text("عنوان یادداشت") })
            Spacer(modifier = Modifier.height(20.dp))
            TextField(modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(start = 10.dp, end = 8.dp),
                value = itemDetaill,
                onValueChange = { itemDetaill = it },
                label = { Text("متن یادداشت") })
            Spacer(modifier = Modifier.height(15.dp))
        }


    }

}

@Composable
fun BtnUpdate(navcontroller: NavHostController, Dbnote: DBNotesModel, daoNotes: Dao_notes) {

    Row(
        modifier = Modifier
            .padding(top = 35.dp)
            .fillMaxWidth()
            .background(color = BlueLight)

    ) {
        Button(modifier = Modifier.padding(start = 130.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            onClick = {
                daoNotes.updateNotes(Dbnote, itemId)
                navcontroller.navigate("Screen1")
            }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.Edit,
                contentDescription = null,

                )
        }
    }

}

@Composable
fun AddNotesPage(navcontroller: NavHostController, daoNotes: Dao_notes, date: String) {

    var title by remember1 {
        mutableStateOf("")
    }
    var detail by remember1 {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
            .background(color = Color.Yellow)

    ) {
        Button(modifier = Modifier.padding(start = 30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            onClick = {
                daoNotes.saveNotes(
                    DBNotesModel(
                        0,
                        title, detail, DBHelper.NOTE_0_STATE, date,
                    )
                )
                navcontroller.navigate("Screen1")

            }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.Done,
                contentDescription = null,
            )
        }

        Button(modifier = Modifier.padding(start = 130.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            onClick = { navcontroller.navigate("Screen1") }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.Home,
                contentDescription = null,

                )
        }
    }

    Column(
        modifier = Modifier.padding(top = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 70.dp)
            .fillMaxWidth(),
            maxLines = 2,
            value = title,
            onValueChange = { title = it },
            label = { Text("عنوان یادداشت") })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(start = 10.dp, end = 8.dp), maxLines = 15,
            value = detail,
            onValueChange = { detail = it },
            label = { Text("متن یادداشت") })
        Spacer(modifier = Modifier.height(15.dp))

    }
}


@Composable
fun ItemList(items: List<DBNotesModel>, Dao: Dao_notes, navcontroller: NavHostController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 99.dp, bottom = 49.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        items(items) { item ->
            ItemView(item, Dao, navcontroller)

        }
    }
}

@Composable
fun ItemView(item: DBNotesModel, Dao: Dao_notes, navcontroller: NavHostController) {
        
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    Dao.SetDeleteState(item, item.id.toString())
                    navcontroller.navigate("Screen1")
                    
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,

                        )
                }

            }
            ClickableText(
                modifier = Modifier

                    .padding(top = 16.dp, end = 15.dp),
                text = AnnotatedString(item.title)
            ) {
                itemId = item.id
                navcontroller.navigate("Screen4")
            }
        }
    }
}


@Composable
fun ItemListTrash(items: List<DBNotesModel>, Dao: Dao_notes, navcontroller: NavHostController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        items(items) { item ->
            ItemViewTrash(item, Dao, navcontroller)
        }
    }
}

@Composable
fun ItemViewTrash(item: DBNotesModel, Dao: Dao_notes, navcontroller: NavHostController) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(modifier = Modifier, onClick = {
                    Dao.SetDeleteState(item, item.id.toString())
                    navcontroller.navigate("Screen3")
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        Modifier.size(19.dp)
                    )
                }
                Button(onClick = {
                    Dao.Delete(item.id.toString())
                    navcontroller.navigate("Screen3")

                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        Modifier.size(19.dp)

                    )
                }
            }
            Text(
                text = item.title,
                modifier = Modifier.padding(17.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}

@Composable
fun BackApp() {
    Column(
        modifier = Modifier
            .background(color = BackgroundColorLight)
            .fillMaxSize()
    ) {}
}

@Composable
fun BtnDeleteADD(navcontroller: NavHostController) {
    Row(
        modifier = Modifier
            .padding(top = 35.dp)
            .fillMaxWidth()
            .background(color = BlueLight)

    ) {
        Button(modifier = Modifier.padding(start = 30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            onClick = {
                navcontroller.navigate("Screen3")
            }) {
            Icon(painterResource(R.drawable.ic_trash), contentDescription = null)
        }

        Button(modifier = Modifier.padding(start = 130.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            onClick = { navcontroller.navigate("Screen2") }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.Add,
                contentDescription = null,

                )
        }
    }
}


@Composable
fun BtnHome(navcontroller: NavHostController) {
    Row(
        modifier = Modifier
            .padding(top = 35.dp)
            .fillMaxWidth()
            .background(color = BlueLight)

    ) {
        Button(modifier = Modifier.padding(start = 130.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            onClick = { navcontroller.navigate("Screen1") }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.Home,
                contentDescription = null,

                )
        }
    }
}

@Composable
fun DateDisplay(): String {
    // دریافت تاریخ فعلی
    val currentDate = Calendar.getInstance().time
    // فرمت کردن تاریخ به شکل دلخواه
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(currentDate)
    Row(
        modifier = Modifier
            .padding(top = 3.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(25.dp)
            .background(Color.Yellow),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = formattedDate)
    }
    return formattedDate
}
fun moreinfo(){

}


