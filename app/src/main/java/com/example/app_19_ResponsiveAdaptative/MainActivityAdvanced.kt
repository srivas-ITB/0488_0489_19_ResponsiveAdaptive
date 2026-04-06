package com.example.app_19_ResponsiveAdaptative

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_19_ResponsiveAdaptative.ui.theme.MyApp_Theme

class MainActivityAdvanced : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esta línea oculta la barra de título clásica de Android
        actionBar?.hide()

        enableEdgeToEdge()
        setContent {
            MyApp_Theme() {
                AdaptiveChessScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveChessScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Usamos el Scaffold solo para la estructura base
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Desaparece por completo en Landscape
            if (!isLandscape) {
                CenterAlignedTopAppBar(
                    title = { Text("Chess Master", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        scrolledContainerColor = Color.Unspecified,
                        navigationIconContentColor = Color.Unspecified,
                        titleContentColor = Color.Unspecified,
                        actionIconContentColor = Color.Unspecified
                    )
                )
            }
        },
        // Esto asegura que el contenido use todo el espacio disponible
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->

        // Contenedor principal que respeta los recortes (Notch/Cámaras)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isLandscape) 0.dp else innerPadding.calculateTopPadding())
                .safeDrawingPadding() // Evita que el notch tape piezas
        ) {
            if (isLandscape) {
                LandscapeLayout()
            } else {
                PortraitLayout()
            }
        }
    }
}

@Composable
fun LandscapeLayout() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), // Pequeño margen para no pegar al borde físico
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TABLERO: El rey de la pantalla
        ChessBoard(
            modifier = Modifier
                .fillMaxHeight() // Ocupa todo el alto disponible
                .aspectRatio(1f)  // Se mantiene cuadrado
                .clip(RoundedCornerShape(8.dp))
                .shadow(4.dp)
        )

        // PANEL LATERAL: Información y acciones
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Título minimalista opcional dentro del panel
            Text(
                "Chess Master",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PlayerInfoCard(name = "Oponente", timeLeft = "05:00", isTurn = false)
                PlayerInfoCard(name = "Tú", timeLeft = "04:32", isTurn = true)
            }

            // Botones compactos para no robar espacio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = { /* ... */ }, modifier = Modifier.weight(1f)) {
                    Text("Tablas", fontSize = 12.sp)
                }
                Button(onClick = { /* ... */ }, modifier = Modifier.weight(1f)) {
                    Text("Rendirse", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PortraitLayout() {
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

