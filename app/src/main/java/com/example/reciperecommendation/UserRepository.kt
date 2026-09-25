package com.example.reciperecommendation

class UserRepository(private val userDao: UserDao) {

    suspend fun findUser(username: String): UserEntity? =
        userDao.getByUsername(username.trim())

    suspend fun createUser(username: String, password: String): Result<Long> {
        return try {
            val entity = UserEntity(username = username.trim(), password = password)
            val id = userDao.insertUser(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setLoggedIn(userId: Long) {
        userDao.logoutAll()
        userDao.setLoggedIn(userId)
    }

}
