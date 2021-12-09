package com.travel.journal.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * User DAO.
 *
 * @author Alex_Smarandache
 */
@Dao
public interface UserDao {

    /**
     * Query to get user by email and password.
     *
     * @param mail     The user email.
     * @param password The user password.
     *
     * @return The founded user.
     */
    @Query(QueriesUtil.GET_USER)
    User getUser(String mail, String password);


    /**
     * Query to get user by email
     *
     * @param mail The user email.
     *
     * @return The founded user.
     */
    @Query(QueriesUtil.GET_USER_BY_EMAIL)
    User getUserByEmail(String mail);


    /**
     * Performs the user insert operation.
     *
     * @param user User to insert in database.
     *
     * @return The user id.
     */
    @Insert
    long insert(User user);

}