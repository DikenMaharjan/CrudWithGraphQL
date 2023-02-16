package com.example.crudwithgraphql

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudwithgraphql.data.repo.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    init {
        viewModelScope.launch {

        }
    }
}