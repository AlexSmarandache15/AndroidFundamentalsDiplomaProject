package com.travel.journal.room;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Class to represent the user.
 */
@Entity
public class User implements Serializable {

    /**
     * The user ID.
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * The user name.
     */
    private String name;

    /**
     * The user email.
     */
    private String email;

    /**
     * The user password.
     */
    private String password;


    /**
     * Constructor.
     *
     * @param name       The user name.
     * @param email      The user email.
     * @param password   The user password.
     */
    public User(String name, String email, String password) {
        this.name     = name;
        this.email    = email;
        this.password = password;
    }


    /**
     * Update the userID.
     *
     * @param id The new user id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The user id.
     */
    public long getId() {
        return id;
    }

    /**
     * Update the user name.
     *
     * @param name The new user name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The user name.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the user email.
     *
     * @param email The new user email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The user email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Update the user password.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
