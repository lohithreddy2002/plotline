package com.example.plotline

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.plotline.databinding.ActivityMainBinding
import com.example.plotline.home.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: HomeAdapter
    private val viewModel by viewModels<MainViewModel>()


    private val imageActivity = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        binding.selectImage.setImageURI(it)
        if (it != null) {

            val bitmap = getBitmapFromUri(it)
            if (bitmap != null) {
                saveImage(bitmap,1)
            }
            val edgedImage = createEdgedImage(it)
            binding.edgedImage.setImageBitmap(edgedImage)
        }


    }
    private var latestTmpUri: Uri? = null

    private val imageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            latestTmpUri?.let { uri ->
                binding.selectImage.setImageURI(uri)
                val edgedImage = createEdgedImage(uri)
                binding.edgedImage.setImageBitmap(edgedImage)
            }
        }

    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        val originalImageStream: InputStream? =
            uri.let { it1 -> contentResolver.openInputStream(it1) }
        val originalBitmap = BitmapFactory.decodeStream(originalImageStream)
        originalImageStream?.close()
        return originalBitmap
    }

    private fun createEdgedImage(uri: Uri): Bitmap? {
        val originalImageStream: InputStream? =
            uri.let { it1 -> contentResolver.openInputStream(it1) }
        val originalBitmap = BitmapFactory.decodeStream(originalImageStream)
        originalImageStream?.close()
        val originalMatrix = Mat();
        val bmp32 = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, originalMatrix)
        val edgesMatrix = Mat()
        Imgproc.Canny(originalMatrix, edgesMatrix, 60.0, 100.0)
        val edgedImage =
            Bitmap.createBitmap(edgesMatrix.cols(), edgesMatrix.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edgesMatrix, edgedImage)
        saveImage(edgedImage)
        return edgedImage
    }

    private fun saveImage(image: Bitmap,flag:Int=0) {
        val filePath: String = cacheDir.path
        val dir = File(filePath)
        val file = File(dir, "image" + "${System.currentTimeMillis()}" + ".png")
        if(flag == 0 ) {
            viewModel.edgedImageUri = file.toUri()
        }else{
            viewModel.originalImageUri = file.toUri()
        }
        val fOut = FileOutputStream(file)

        image.compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()
    }


    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                viewModel.originalImageUri = uri
                latestTmpUri = uri
                imageFromCamera.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file ${System.currentTimeMillis()}", ".png", cacheDir)
                .apply {
                    createNewFile()
                }


        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageButtonCamera.setOnClickListener {
            takeImage()
        }
        binding.imageButtonSelect.setOnClickListener {
            imageActivity.launch(arrayOf("image/*"))
        }
        lifecycleScope.launchWhenStarted {
            viewModel.dataAddStatus.collect{
                if(it){
                    Toast.makeText(this@MainActivity,"Images Added to Database",Toast.LENGTH_LONG)
                    delay(500)
                    finish()
                }

            }
        }

        binding.addToDatabase.setOnClickListener {
            viewModel.addImageToDatabase()
        }
    }


}