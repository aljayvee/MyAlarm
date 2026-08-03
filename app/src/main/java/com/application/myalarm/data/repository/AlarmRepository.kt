package com.application.myalarm.data.repository

import com.application.myalarm.data.db.AlarmDao
import com.application.myalarm.data.db.AlarmEntity
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {

    val alarms: Flow<List<AlarmEntity>> = alarmDao.getAll()

    val alarmCount: Flow<Int> = alarmDao.getCount()

    suspend fun getById(id: Long): AlarmEntity? {
        return alarmDao.getById(id)
    }

    suspend fun getEnabledAlarms(): List<AlarmEntity> {
        return alarmDao.getEnabled()
    }

    suspend fun insertAlarm(alarm: AlarmEntity): Long {
        return alarmDao.insert(alarm)
    }

    suspend fun updateAlarm(alarm: AlarmEntity) {
        alarmDao.update(alarm)
    }

    suspend fun deleteAlarm(alarm: AlarmEntity) {
        alarmDao.delete(alarm)
    }

    suspend fun deleteAlarmById(id: Long) {
        alarmDao.deleteById(id)
    }

    suspend fun toggleAlarm(id: Long, enabled: Boolean) {
        val alarm = alarmDao.getById(id) ?: return
        alarmDao.update(alarm.copy(isEnabled = enabled))
    }
}
