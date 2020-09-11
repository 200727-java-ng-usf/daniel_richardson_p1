package com.revature.ers.repos;

import com.revature.ers.models.AppUser;
//import com.revature.ers.models.Role;
import com.revature.ers.services.ConnectionService;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * UserRepo: Data Access Object to drink up that postgresql milkshake of data
 * --finds users by
 * -- -ID
 * -- -credentials
 * -- -all users
 * -- -username
 * -- -email
 * --saves user
 * --maps user
 */

/*
    Recommended methods to implement:
        - Set<AppUser> findAllUsers()
        - Optional<AppUser> findUserById(int id)
        - Set<AppUser> findUsersByRole(String rolename)
        - boolean/void updateUser(AppUser updatedUser)
        - boolean/void deleteUserById(int id)
        - Optional<AppUser> findUserByEmail(String email)
 */
public class UserRepository {

    // extract common query clauses into a easily referenced member for reusability.
    private String baseQuery = "SELECT * FROM project1.ers_users au " +
                               "JOIN ers_user_roles ur " +
                               "ON au.user_role_id = ur.role_id ";

    public UserRepository() {
        System.out.println("[LOG] - Instantiating " + this.getClass().getName());
    }

    public Optional<AppUser> findUserById(int id) {

        Optional<AppUser> _user = Optional.empty();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE au.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            Set<AppUser> result = mapResultSet(pstmt.executeQuery());
            if (!result.isEmpty()) {
                _user = result.stream().findFirst();
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;

    }

    public Set<AppUser> findAllUsers() {

        Set<AppUser> users = new HashSet<>();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = "SELECT * FROM project1.ers_users au " +
                         "JOIN ers_user_roles ur " +
                         "ON au.user_role_id = ur.role_id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            users = mapResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;

    }

    public Optional<AppUser> findUserByCredentials(String username, String password, int role) {

        Optional<AppUser> _user = Optional.empty();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE username = ? AND password = ? AND user_role_id = ?;";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, role);
            System.out.println(pstmt);

            ResultSet rs = pstmt.executeQuery();

            _user = mapResultSet(rs).stream().findFirst();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;
    }

    public Optional<AppUser> findUserByUsername(String username) {

        Optional<AppUser> _user = Optional.empty();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            // you can control whether or not JDBC automatically commits DML statements
//            conn.setAutoCommit(false);

            String sql = baseQuery + "WHERE username = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            _user = mapResultSet(rs).stream().findFirst();

            // if you want to manually control the transaction
//            conn.commit();
//            conn.rollback();
//            conn.setSavepoint();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;

    }

    public Optional<AppUser> findUserByEmail(String email) {

        Optional<AppUser> _user = Optional.empty();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE email = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            _user = mapResultSet(rs).stream().findFirst();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;

    }

    public void save(AppUser newUser) {

        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = "INSERT INTO project1.ers_users (username, password, first_name, last_name, email, user_role_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";

            // second parameter here is used to indicate column names that will have generated values
            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"id"});
            pstmt.setString(1, newUser.getUsername());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getFirstName());
            pstmt.setString(4, newUser.getLastName());
            pstmt.setString(5, newUser.getEmail());
            pstmt.setInt(6, newUser.getRole());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {

                ResultSet rs = pstmt.getGeneratedKeys(); //should be the serialized user_id

                rs.next();
                newUser.setId(rs.getInt(1));

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    private Set<AppUser> mapResultSet(ResultSet rs) throws SQLException {

        Set<AppUser> users = new HashSet<>();

        while (rs.next()) {
            AppUser temp = new AppUser();
            temp.setId(rs.getInt("ers_user_id"));
            temp.setUsername(rs.getString("username"));
            temp.setPassword(rs.getString("password"));
            temp.setFirstName(rs.getString("first_name"));
            temp.setLastName(rs.getString("last_name"));
            temp.setEmail(rs.getString("email"));
            temp.setRole(rs.getInt("user_role_id"));
            users.add(temp);
        }

        return users;

    }

}
