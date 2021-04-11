package com.app.cartoonvideos.viewModel
import androidx.lifecycle.ViewModel
import com.app.cartoonvideos.MyApp
import com.app.cartoonvideos.repo.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    repository: DataRepository
) : ViewModel() {
    val pigeonListLiveData = repository.getAllVideo(MyApp.category)
}