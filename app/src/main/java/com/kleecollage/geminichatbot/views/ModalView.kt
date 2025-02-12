package com.kleecollage.geminichatbot.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.kleecollage.geminichatbot.ui.theme.backColor
import com.kleecollage.geminichatbot.viewModel.GeminiViewModel
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalView(viewModel: GeminiViewModel, showModal: Boolean, onDismiss: () -> Unit) {

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    // var image by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val permissionCheckResult =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        viewModel.image = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        cameraLauncher.launch(uri)
    }


    if (showModal) {
        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier.fillMaxSize()
                .background(backColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.image.path?.isNotEmpty() == true) {
                    Image(
                        modifier = Modifier.padding(16.dp, 8.dp),
                        painter = rememberAsyncImagePainter(viewModel.image), contentDescription = null
                    )
                }
                Row(modifier = Modifier.padding(10.dp)) {
                    OutlinedButton(onClick = {
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED)
                            cameraLauncher.launch(uri)
                        else
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                    }) {
                        Text(text = "Take Picture", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    OutlinedButton(onClick = {
                        val imageStream: InputStream? = context.contentResolver.openInputStream(viewModel.image)
                        val bitmap: Bitmap? = BitmapFactory.decodeStream(imageStream)

                        if (bitmap != null )
                            viewModel.descriptionImage(bitmap)
                    }) {
                        Text(text = "Send to Gemini", color = Color.White)
                    }
                }
                Text(
                    text = viewModel.descriptionResponse,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}


@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}