package pl.edu.agh.kis.flashcards.database.services

import pl.edu.agh.kis.flashcards.database.dao.SessionDao
import pl.edu.agh.kis.flashcards.database.entity.SessionEntity

class SessionRepository(private val sessionDao: SessionDao) {

    fun addSession(sessionEntity: SessionEntity){
        sessionDao.insert(sessionEntity)
    }

}