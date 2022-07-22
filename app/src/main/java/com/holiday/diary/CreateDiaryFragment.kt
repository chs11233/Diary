package com.holiday.diary

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.holiday.diary.database.DiarysDatabase
import com.holiday.diary.databinding.FragmentCreateDiaryBinding
import com.holiday.diary.entities.Diarys
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

class CreateDiaryFragment : BaseFragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    private var mBinding: FragmentCreateDiaryBinding? = null
    private val binding get() = mBinding!!

    private var mInterstitialAd: InterstitialAd? = null

    var currentDate: String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    var selectedColor = "#F5F5F5"
    private var diaryId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        diaryId = requireArguments().getInt("diaryId", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentCreateDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateDiaryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (diaryId != -1) {
            launch {
                context?.let {
                    var diarys = DiarysDatabase.getDatabase(it).diaryDao().getSpecificDiary(diaryId)
                    binding.dtTitle.setText(diarys.title)
                    binding.dtDiaryDesc.setText(diarys.diaryText)
                    binding.deleteBtn.visibility = View.VISIBLE

                    if (diarys.imgPath != "") {
                        selectedImagePath = diarys.imgPath!!
                        binding.imgDiary.setImageBitmap(BitmapFactory.decodeFile(diarys.imgPath))
                        binding.layoutImage.visibility = View.GONE
                        binding.layoutImage2.visibility = View.VISIBLE
                        binding.imgDiary.visibility = View.VISIBLE
                    } else {
                        binding.layoutImage.visibility = View.GONE
                        binding.layoutImage2.visibility = View.GONE
                        binding.imgDiary.visibility = View.GONE
                    }
                }
            }
        } else {
            binding.deleteBtn.visibility = View.GONE
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("img_action")
        )

        val date = SimpleDateFormat("yyyy/M/dd hh:mm:ss")

        currentDate = date.format(Date())
        binding.dtDateTime.text = currentDate

        binding.deleteBtn.setOnClickListener {
            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            deleteDiary()
        }

        binding.doneBtn.setOnClickListener {
            if (diaryId != -1) {
                updateDiary()
            } else {
                saveDiary()
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireContext() as Activity)
                }
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        setListener()
    }

    private fun saveDiary() {
        if (binding.dtTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else if (binding.dtDiaryDesc.text.isNullOrEmpty()) {
            Toast.makeText(context, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            launch {
                val diarys = Diarys()
                diarys.title = binding.dtTitle.text.toString()
                diarys.diaryText = binding.dtDiaryDesc.text.toString()
                diarys.dateTime = currentDate
                diarys.imgPath = selectedImagePath

                val cal = Calendar.getInstance()
                diarys.year = cal.get(Calendar.YEAR)
                diarys.month = cal.get(Calendar.MONTH) + 1
                diarys.day = cal.get(Calendar.DATE)

                context?.let {
                    DiarysDatabase.getDatabase(it).diaryDao().insertDiarys(diarys)
                    binding.dtTitle.setText("")
                    binding.dtDiaryDesc.setText("")
                    binding.layoutImage2.visibility = View.GONE
                    binding.imgDiary.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun updateDiary() {
        launch {
            context?.let {
                var diarys = DiarysDatabase.getDatabase(it).diaryDao().getSpecificDiary(diaryId)
                diarys.title = binding.dtTitle.text.toString()
                diarys.diaryText = binding.dtDiaryDesc.text.toString()
                diarys.dateTime = currentDate
                diarys.imgPath = selectedImagePath

                DiarysDatabase.getDatabase(it).diaryDao().updateDiary(diarys)
                binding.dtTitle.setText("")
                binding.dtDiaryDesc.setText("")
                binding.layoutImage2.visibility = View.GONE
                binding.imgDiary.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun deleteDiary() {
        launch {
            context?.let {
                DiarysDatabase.getDatabase(it).diaryDao().deleteSpecificDiary(diaryId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private val BroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var actionImg = p1!!.getStringExtra("action")
            when (actionImg!!) {
                "Image" -> {
                    readStorageTask()
                }
                else -> {
                    binding.layoutImage.visibility = View.GONE
                    binding.imgDiary.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                }
            }
        }
    }

    private fun setListener() {
        binding.layoutImage.setOnClickListener {
            val intent = Intent("img_action")
            intent.putExtra("action", "Image")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
    }

    private fun hasReadStoragePerm(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun readStorageTask() {
        if (hasReadStoragePerm()) {
            pickImageFromGallery()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath: String? = null
        var cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                var selectedImageUrl = data.data
                if (selectedImageUrl != null) {
                    try {
                        var inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imgDiary.setImageBitmap(bitmap)
                        binding.imgDiary.visibility = View.VISIBLE
                        binding.layoutImage2.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,
            permissions,
            grantResults,
            requireActivity())
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRationaleDenied(requestCode: Int) {}

    override fun onRationaleAccepted(requestCode: Int) {}
}