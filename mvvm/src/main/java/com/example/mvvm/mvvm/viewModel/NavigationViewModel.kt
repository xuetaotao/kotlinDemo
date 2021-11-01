package com.example.mvvm.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.NavigationBean
import com.example.mvvm.mvvm.mainRepository.SystemRepository
import com.example.mvvm.utils.SingleLiveEvent

class NavigationViewModel : CommonViewModel() {

    private val repository = SystemRepository()
    private val navigationTree = SingleLiveEvent<List<NavigationBean>>()

    fun getNavigationTree(): LiveData<List<NavigationBean>> {
        launchUI {
            val res = repository.getNavigationTree()
            navigationTree.value = res.data
        }
        return navigationTree
    }
}