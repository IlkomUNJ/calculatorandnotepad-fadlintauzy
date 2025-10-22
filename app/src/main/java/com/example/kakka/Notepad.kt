package com.example.kakka

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(noteId: Int, navController: NavController) {
    val stateHolder = rememberEditNoteStateHolder(noteId = noteId)


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text Editor") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Simpan dan Kembali")
                    }
                },
                actions = {

                        IconButton(onClick = {
                            stateHolder.addNote()

                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "Buat Catatan Baru")
                        }

                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextFormatToolbar(
                currentStyle = stateHolder.currentTextStyle,
                onStyleChange = stateHolder::applyStyleToSelection
            )

            OutlinedTextField(
                value = stateHolder.noteTextFieldValue,
                onValueChange = stateHolder::onTextFieldValueChange,
                placeholder = { Text("Mulai menulis...") },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}


@Composable
fun TextFormatToolbar(
    currentStyle: SpanStyle,
    onStyleChange: (SpanStyle) -> Unit
) {
    val currentSize = currentStyle.fontSize?.value ?: 16f
    val isBold = currentStyle.fontWeight == FontWeight.Bold
    val isItalic = currentStyle.fontStyle == FontStyle.Italic

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kontrol Ukuran Teks
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FormatSize, contentDescription = "Ukuran")
                Spacer(Modifier.width(8.dp))
                // Perkecil
                IconButton(onClick = {
                    val newSize = (currentSize - 2f).coerceIn(12f, 30f)
                    onStyleChange(currentStyle.copy(fontSize = newSize.sp))
                }, enabled = currentSize > 12f) {
                    Text("A-", style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    text = "${currentSize.toInt()}",
                    style = MaterialTheme.typography.labelLarge
                )
                // Perbesar
                IconButton(onClick = {
                    val newSize = (currentSize + 2f).coerceIn(12f, 30f)
                    onStyleChange(currentStyle.copy(fontSize = newSize.sp))
                }, enabled = currentSize < 30f) {
                    Text("A+", style = MaterialTheme.typography.titleMedium)
                }
            }

            IconButton(
                onClick = { onStyleChange(currentStyle.copy(fontWeight = if (isBold) FontWeight.Normal else FontWeight.Bold)) }
            ) {
                Icon(
                    Icons.Filled.FormatBold,
                    contentDescription = "Tebal",
                    tint = if (isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                onClick = { onStyleChange(currentStyle.copy(fontStyle = if (isItalic) FontStyle.Normal else FontStyle.Italic)) }
            ) {
                Icon(
                    Icons.Filled.FormatItalic,
                    contentDescription = "Miring",
                    tint = if (isItalic) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}




