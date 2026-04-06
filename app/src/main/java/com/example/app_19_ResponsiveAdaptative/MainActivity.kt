package com.example.app_19_ResponsiveAdaptative

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_19_ResponsiveAdaptative.ui.theme.MyApp_Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esta línea oculta la barra de título clásica de Android
        actionBar?.hide()

        enableEdgeToEdge()
        setContent {
            MyApp_Theme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val windowSize = calculateWindowSizeClass(this)
                    val configuration = LocalConfiguration.current

                    Box (modifier = Modifier.padding(innerPadding)) {


//                    MyBasicLayout(
//                        modifier = Modifier.padding( innerPadding),
//                        windowSize = windowSize,
//                        configuration = configuration
//                    )

                        ChessGameScreen()
                    }

                }
            }
        }
    }
}


@Composable
fun MyBasicLayout(modifier: Modifier = Modifier,
             windowSize: WindowSizeClass,
             configuration: Configuration
){

    Column(modifier.verticalScroll(rememberScrollState())) {

        //Dejo un espacio para alejar del borde superior
        Spacer(modifier = Modifier.height(50.dp))

        Column {
            Text("Window size class - Width: ${windowSize.widthSizeClass}")
            Text("Window size class - Height: ${windowSize.heightSizeClass}")
        }

        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Text("Landscape")
            }
            else -> {
                Text("Portrait")
            }
        }

    }
}

@Composable
fun ChessGameScreen() {
    val configuration = LocalConfiguration.current

    // Detectamos la orientación
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            ChessLandscapeLayout()
        }
        else -> {
            ChessPortraitLayout()
        }
    }
}


@Composable
fun ChessPortraitLayout() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PlayerInfoCard(name = "Oponente", timeLeft = "05:00", isTurn = true)

        // Tablero cuadrado basado en el ancho disponible
        ChessBoard(modifier = Modifier.fillMaxWidth().aspectRatio(1f))

        PlayerInfoCard(name = "Tú", timeLeft = "04:32", isTurn = false)

        GameControls(modifier = Modifier.fillMaxWidth())
    }
}


@Composable
fun ChessLandscapeLayout() {
    Row(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tablero a la izquierda (ocupa el alto máximo)
        ChessBoard(modifier = Modifier.fillMaxHeight().aspectRatio(1f))

        // Panel lateral de información y controles
        Column(
            modifier = Modifier.fillMaxHeight().weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayerInfoCard(name = "Oponente", timeLeft = "05:00", isTurn = true)
            PlayerInfoCard(name = "Tú", timeLeft = "04:32", isTurn = false)
            GameControls(modifier = Modifier.fillMaxWidth())
        }
    }
}


@Composable
fun ChessBoard(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color.Gray)) {
        Column {
            repeat(8) { row ->
                Row(modifier = Modifier.weight(1f)) {
                    repeat(8) { col ->
                        val isDark = (row + col) % 2 != 0
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(if (isDark) Color(0xFF769656) else Color(0xFFEeEed2))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerInfoCard(
    name: String,
    timeLeft: String,
    isTurn: Boolean,
    modifier: Modifier = Modifier
) {
    // Definimos colores dinámicos según si es el turno del jugador
    val backgroundColor = if (isTurn) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    val borderColor = if (isTurn) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor),
        tonalElevation = if (isTurn) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icono de Avatar simple
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (isTurn) MaterialTheme.colorScheme.primary else Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isTurn) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Cronómetro con estilo digital
            Surface(
                color = if (isTurn) MaterialTheme.colorScheme.errorContainer else Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = timeLeft,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = FontFamily.Monospace // Fuente fija para que el tiempo no "salte"
                    ),
                    color = if (isTurn) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun GameControls(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = { /* Rendirse */ }) { Text("Rendirse") }
        Button(onClick = { /* Tablas */ }) { Text("Tablas") }
    }
}