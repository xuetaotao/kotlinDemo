package com.example.mvvm.mvvm.mainViewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.ArticleListBean
import com.example.mvvm.mvvm.mainRepository.QuestionRepository
import com.example.mvvm.mvvm.viewModel.CommonViewModel
import com.example.mvvm.utils.SingleLiveEvent

class QuestionViewModel : CommonViewModel() {

    private val repository = QuestionRepository()
    private val questionData = SingleLiveEvent<ArticleListBean>()

    fun getQuestionList(page: Int): LiveData<ArticleListBean> {
        launchUI {
            val result = repository.getQuestionList(page)
            questionData.value = result.data
        }
        return questionData
    }
}