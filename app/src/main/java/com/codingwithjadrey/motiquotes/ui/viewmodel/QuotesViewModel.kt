package com.codingwithjadrey.motiquotes.ui.viewmodel

import androidx.lifecycle.*
import com.codingwithjadrey.motiquotes.data.entity.Motive
import com.codingwithjadrey.motiquotes.data.dao.MotiveDao
import kotlinx.coroutines.launch

class QuotesViewModel(private val motiveDao: MotiveDao) : ViewModel() {
    val getAllMotiveItems: LiveData<List<Motive>> = motiveDao.getALlMotiveItems().asLiveData()

    private fun insertQuote(motive: Motive) {
        viewModelScope.launch { motiveDao.insert(motive) }
    }

    private fun getQuoteItem(creationDate: String, quoteSource: String, motivationQuote: String): Motive {
        return Motive(createdOn = creationDate, quoteSource = quoteSource, motiveQuote = motivationQuote)
    }

    fun addQuote(creationDate: String, quoteSource: String, motivationQuote: String) {
        val quote = getQuoteItem(creationDate, quoteSource, motivationQuote)
        insertQuote(quote)
    }

    fun getQuote(id: Int): LiveData<Motive> {
        return motiveDao.getMotiveItem(id).asLiveData()
    }

    fun deleteQuote(motive: Motive) {
        viewModelScope.launch { motiveDao.delete(motive) }
    }

    private fun updateQuote(motive: Motive) {
        viewModelScope.launch { motiveDao.update(motive) }
    }

    private fun getUpdatedQuoteItem(
        quoteID: Int,
        creationDate: String,
        quoteSource: String,
        motivationQuote: String
    ): Motive {
        return Motive(
            id = quoteID,
            createdOn = creationDate,
            quoteSource = quoteSource,
            motiveQuote = motivationQuote
        )
    }

    fun updateQuoteItem(quoteID: Int, creationDate: String, quoteSource: String, motivationQuote: String) {
        val updatedQuote = getUpdatedQuoteItem(quoteID, creationDate, quoteSource, motivationQuote)
        updateQuote(updatedQuote)
    }
}

class QuotesViewModelFactory(private val motiveDao: MotiveDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuotesViewModel::class.java)) {
            return QuotesViewModel(motiveDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}