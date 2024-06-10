package com.blink.blinkid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blink.blinkid.commons.LocalDataStore
import com.blink.blinkid.model.Group
import com.blink.blinkid.model.User
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.AddStudentRequest
import com.blink.blinkid.model.Constants
import com.blink.blinkid.repo.ExamRepository
import com.blink.blinkid.repo.UserRepository
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: ExamRepository,
    private val localDataStore: LocalDataStore
) : ViewModel() {

    private val _selectedGroup = MutableStateFlow<Group?>(null)
    val selectedGroup: StateFlow<Group?> = _selectedGroup.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()

    private val _groups = MutableStateFlow<NetworkResult<List<Group>>>(NetworkResult.Initial)
    val groups: StateFlow<NetworkResult<List<Group>>> = _groups.asStateFlow()

    private val _group = MutableStateFlow<NetworkResult<Group>>(NetworkResult.Initial)
    val group: StateFlow<NetworkResult<Group>> = _group.asStateFlow()

    private val _students = MutableStateFlow<NetworkResult<List<User>>>(NetworkResult.Initial)
    val students: StateFlow<NetworkResult<List<User>>> = _students.asStateFlow()


    private val _admins = MutableStateFlow<NetworkResult<List<User>>>(NetworkResult.Initial)
    val admins: StateFlow<NetworkResult<List<User>>> = _admins.asStateFlow()

    private val _user = MutableStateFlow<NetworkResult<User>>(NetworkResult.Initial)
    val user: StateFlow<NetworkResult<User>> = _user.asStateFlow()

    private lateinit var loggedInUser: User

    init {
        getGroups()
        getLoggedInUser()
    }

    private fun getLoggedInUser() {
        viewModelScope.launch {
            localDataStore.getObject(Constants.USER, object : TypeToken<User>() {})?.let {
                loggedInUser = it
            }
        }
    }

    fun addStudent(addStudentRequest: AddStudentRequest) {
        viewModelScope.launch {
            _user.value = NetworkResult.Loading
            groupRepository.addStudent(addStudentRequest).collect {
                _user.value = it
            }
        }
    }

    fun getGroups() {
        viewModelScope.launch {
            _groups.value = NetworkResult.Loading
            groupRepository.getGroups().collect {
                _groups.value = it
            }
        }
    }


    fun getStudents() {
        viewModelScope.launch {
            _students.value = NetworkResult.Loading
            userRepository.getStudents().collect {
                _students.value = it
            }
        }
    }

    fun getAdmins() {
        viewModelScope.launch {
            _admins.value = NetworkResult.Loading
            userRepository.getAdmins().collect {
                _admins.value = it
            }
        }
    }

    fun addGroup(group: Group) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.addGroup(group).collect {
                if (it is NetworkResult.Success) {
                    it.body?.let { group ->
                        getLoggedInUser()
                        loggedInUser.id?.let { it1 -> addAdminToGroup(group.id!!, it1) }
                    }
                }
            }
        }
    }

    fun updateGroup(groupId: Int, group: Group) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.updateGroup(groupId, group).collect {
                _group.value = it
            }
        }
    }

    fun addStudentToGroup(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.addStudentToGroup(groupId, userId).collect {
                
                _group.value = it
                _selectedGroup.value = (it as NetworkResult.Success).body
            }
        }
    }

    private fun addAdminToGroup(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.addAdminToGroup(groupId, userId).collect {
                _group.value = it
            }
        }
    }

    fun deleteGroup(groupId: Int) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.deleteGroup(groupId).collect {
                _group.value = it
            }
        }
    }

    fun deleteAdminFromGroup(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.deleteAdminFromGroup(groupId, userId).collect {
                _group.value = it
            }
        }
    }

    fun deleteStudentFromGroup(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.deleteStudentFromGroup(groupId, userId).collect {
                _group.value = it
                _selectedGroup.value = (it as NetworkResult.Success).body
            }
        }
    }


    fun getGroup(groupId: Int) {
        viewModelScope.launch {
            _group.value = NetworkResult.Loading
            groupRepository.getGroup(groupId).collect {
                _group.value = it
            }
        }
    }


    fun setSelectedGroup(group: Group) {
        _selectedGroup.value = group
    }

    fun setSelectedUser(user: User) {
        _selectedUser.value = user
    }


}