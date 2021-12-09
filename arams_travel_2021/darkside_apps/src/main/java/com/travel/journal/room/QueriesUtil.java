package com.travel.journal.room;

/**
 * Contains usefully queries for these databases.
 *
 * @author Alex_Smarandache
 */
class QueriesUtil {

    /**
     * Query to get the user with the specified mail and password.
     */
    public static final String GET_USER = "SELECT * FROM User WHERE email = :mail AND password = :password";

    /**
     * Query to get the user with the specified mail.
     */
    public static final String GET_USER_BY_EMAIL = "SELECT * FROM User WHERE email = :mail";

    /**
     * Query to get the trip with the specified id.
     */
    public static final String GET_TRIP_BY_ID = "SELECT * FROM Trip WHERE id = :tripId";

    /**
     * Query to get the trip with the specified user.
     */
    public static final String GET_TRIP_BY_USER = "SELECT * FROM Trip WHERE user_id = :userId";

    /**
     * Query to set favorite the trip with the specified id.
     */
    public static final String SET_TRIP_FAVORITE = "UPDATE Trip SET is_favorite = 1 WHERE id = :tripId";

    /**
     * Query to unset favorite the trip with the specified id.
     */
    public static final String UNSET_TRIP_FAVORITE = "UPDATE Trip SET is_favorite = 0 WHERE id = :tripId";

    /**
     * Query to delete the trip with the specified id.
     */
    public static final String DELETE_TRIP_BY_ID = "DELETE FROM Trip WHERE id = :tripId";


    /**
     * The hidden constructor.
     */
    private QueriesUtil() {
        // nothing.
    }


}
