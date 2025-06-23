package mk.ukim.finki.tripcostcalculator.ui

import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import mk.ukim.finki.tripcostcalculator.R


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView
    private lateinit var btnTakePhoto: Button

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageView.setImageURI(imageUri)
            Toast.makeText(requireContext(), "Сликата е зачувана!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Сликањето не успеа.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        imageView = view.findViewById(R.id.imageViewPhoto)
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)

        btnTakePhoto.setOnClickListener {
            if (checkCameraPermission()) {
                launchCamera()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
        return view
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun launchCamera() {
        imageUri = createImageUri()
        if (imageUri != null) {
            takePictureLauncher.launch(imageUri)
        } else {
            Toast.makeText(requireContext(), "Грешка при креирање на URI за слика.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "TripCost_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TripCostCalculator")
            }
        }
        return requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                Toast.makeText(requireContext(), "Без дозвола за камера нема сликање.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }
}