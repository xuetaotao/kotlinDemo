package com.example.mvvm.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.KnowledgeTreeBody
import com.example.mvvm.mvvm.mainRepository.SystemRepository
import com.example.mvvm.utils.SingleLiveEvent

class KnowledgeViewModel : CommonViewModel() {

    private val repository = SystemRepository()
    private val knowledgeTree = SingleLiveEvent<List<KnowledgeTreeBody>>()

    fun getKnowledgeTree(): LiveData<List<KnowledgeTreeBody>> {
        launchUI {
            val res = repository.getKnowledgeTree()
            knowledgeTree.value = res.data
        }
        return knowledgeTree
    }
}