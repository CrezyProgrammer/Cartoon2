package com.cartoon.funnyvideos.viewModel
import androidx.lifecycle.ViewModel
import com.cartoon.funnyvideos.repo.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    repository: DataRepository
) : ViewModel() {
    val pigeonListLiveData = repository.getAllVideo(false)
    val popularListLiveData = repository.getPopularVideo(true)
}