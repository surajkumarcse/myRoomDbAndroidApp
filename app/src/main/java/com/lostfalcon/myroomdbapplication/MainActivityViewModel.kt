package com.lostfalcon.myroomdbapplication

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.Room

class MainActivityViewModel : ViewModel() {
    private val TAG ="MainActivityViewModel"
    private var db: AppDatabase? = null;


    fun createDb(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "SurajAppDatabase"
        ).build()
    }

    fun getUsers(): List<User>? {
        val userDao = db?.userDao()
        return userDao?.getAll()
    }

    private var activeUid: Int = -1
    private var lastDeletedUid: Int = -1

    fun insertUser(firstName: String, lastName: String) {
        val userDao = db?.userDao()
        // not a good way to put primary key
        activeUid++
        userDao?.insertAll(User(uId = activeUid, firstName = firstName, lastName = lastName))
        Log.d(TAG, "inserted successfully - $firstName $lastName")
    }

    fun deleteUser() {
        if(lastDeletedUid >= activeUid) {
            Log.e(TAG, "not a valid id to delete")
            return
        }
        lastDeletedUid++
        val intArray = IntArray(1)
        intArray[0] = lastDeletedUid

        val userDao = db?.userDao()
        val users = userDao?.loadAllByIds(intArray)

        users?.let {
            userDao.delete(it[0])
            Log.e(TAG, "user deleted successfully ${it[0].toString()}")
        }
    }

    suspend fun updateActiveUser() {
        if(activeUid <= lastDeletedUid) {
            Log.e(TAG, "not a valid id to update")
            return
        }

        val intArray = IntArray(1)
        intArray[0] = activeUid
        val userDao = db?.userDao()
        val users = userDao?.loadAllByIds(intArray)

        users?.let {
            val updatedUser = it[0].copy(
                firstName = "updatedFirstName"
            )
            userDao.insert(updatedUser)
            Log.e(TAG, "updated the user id ${updatedUser.toString()}")
        }
    }
}