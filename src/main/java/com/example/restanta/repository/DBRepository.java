package com.example.restanta.repository;

import com.example.restanta.model.Entity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBRepository<ID extends Comparable<ID>, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String connectionString;
    protected String user;
    protected String password;

    public DBRepository(String connectionString, String user, String password) {
        this.connectionString = connectionString;
        this.user = user;
        this.password = password;
        read();
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, user, password);
    }

    protected abstract void read();
    protected abstract void refreshLastId();
}
