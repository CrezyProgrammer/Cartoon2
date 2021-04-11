package com.app.cartoonvideos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){
    companion object {

        var category: String= ""
    }
}