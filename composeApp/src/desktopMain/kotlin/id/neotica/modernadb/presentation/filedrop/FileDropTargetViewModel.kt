package id.neotica.modernadb.presentation.filedrop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.neotica.modernadb.data.adb.android.AdbInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileDropTargetViewModel: ViewModel() {
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _filePath: MutableStateFlow<String> = MutableStateFlow("")
    private var _installStatus = MutableStateFlow("")

    val isLoading = _isLoading
    val filePath = _filePath
    val installStatus = _installStatus

    fun clear() {
        _filePath.value = ""
        _installStatus.value = ""
    }

    fun clearStatus() {
        _installStatus.value = ""
    }

    fun setPath(filePath: String) {
        _filePath.value = filePath
    }

    fun install (apk: String) {
        if (_isLoading.value || _filePath.value.isBlank()) return
        _installStatus.value = "Starting installation..."
        _isLoading.value = true

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { AdbInput.install(apk) }

            when (result) {
                "Success" -> {
                    _filePath.value = ""
                    _isLoading.value = false
                }

                else -> {
                    _filePath.value = ""
                    _isLoading.value = false
                }

            }
            _installStatus.value = result
        }

    }
}