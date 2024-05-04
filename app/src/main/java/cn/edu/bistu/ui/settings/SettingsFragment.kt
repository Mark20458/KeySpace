package cn.edu.bistu.ui.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cn.edu.bistu.App
import cn.edu.bistu.databinding.FragmentSettingsBinding
import cn.edu.bistu.ui.dialog.TipDialog
import cn.edu.bistu.util.Backup
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.SPUtil
import cn.edu.bistu.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class SettingsFragment : Fragment() {
    private lateinit var mBind: FragmentSettingsBinding

    // 导出，文件夹选择器
    private val pickFolderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: return@registerForActivityResult
                val folder = DocumentFile.fromTreeUri(requireContext(), uri)
                if (folder == null || !folder.isDirectory) {
                    return@registerForActivityResult
                }
                val fileName = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()
                ).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS")) + "backup.zip"
                folder.listFiles().forEach {
                    if (it.name == fileName) {
                        TipDialog.getInstance("该目录下已有backup.zip文件，您确定要替换吗？")
                            .setCancelCallback {
                                backup(folder, fileName)
                            }.show(childFragmentManager, "backup")
                        return@registerForActivityResult
                    }
                }
                // 该目录下没有同名的文件
                backup(folder, fileName)
            }
        }

    // 导入
    private val readFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    data.data?.run {
                        try {
                            import(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ToastUtil.show("导入失败")
                        }
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentSettingsBinding.inflate(inflater)
        initView()
        initListener()
        return mBind.root
    }


    private fun initView() {
        mBind.email.text = SPUtil.getString(PreferencesKey.ACCOUNT, "")
        mBind.enableFingerprintUnlock.text =
            if (SPUtil.getBoolean(PreferencesKey.IS_BIOMETRIC_ENABLE, true)) "启用" else "已禁用"
    }

    private fun initListener() {
        // 指纹解锁
        mBind.fingerprintUnlockLayout.setOnClickListener {
            var b = SPUtil.getBoolean(PreferencesKey.IS_BIOMETRIC_ENABLE, true)
            b = !b
            SPUtil.saveBoolean(PreferencesKey.IS_BIOMETRIC_ENABLE, b)
            mBind.enableFingerprintUnlock.text = if (b) "启用" else "已禁用"
        }

        // 备份
        mBind.backupLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            pickFolderLauncher.launch(intent)
        }
        // 导入
        mBind.importLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            readFileLauncher.launch(intent)
        }
    }

    /**
     * 备份文件在该uri下
     */
    private fun backup(folder: DocumentFile, fileName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            Backup.backup(requireContext(), folder, fileName)
        }
        ToastUtil.show("备份成功")
    }

    /**
     * 导入数据，将现有的全部删除，替换成文件中的
     */
    private fun import(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            Backup.import(requireContext(), uri)
        }
        App.getInstance().stack.clear()
        App.getInstance().stack.push(cn.edu.bistu.database.model.Item(name = "root", id = -1))
        ToastUtil.show("导入成功")
    }
}