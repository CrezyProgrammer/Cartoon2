package com.cartoon.cartoonvideos.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cartoon.cartoonvideos.entity.Video
import com.cartoon.cartoonvideos.repo.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
   private val repository: DataRepository
) : ViewModel() {

    fun videoListLiveData(s: String): LiveData<List<Video>> {
        return repository.getAllVideo(s)
    }

}