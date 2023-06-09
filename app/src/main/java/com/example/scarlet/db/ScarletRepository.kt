package com.example.scarlet.db

class ScarletRepository(
    private val dbInstance: ScarletDatabase
) {

    fun getBlocksByCompleted(completed: Boolean) = dbInstance.blockDao.getBlocksByCompleted(completed)

    fun getSessionsByBlockId(blockId: Int) = dbInstance.sessionDao.getSessionsByBlockId(blockId)

    fun getBlockById(blockId: Int) = dbInstance.blockDao.getBlockById(blockId)
}