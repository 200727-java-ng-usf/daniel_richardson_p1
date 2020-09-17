package com.revature.ers.repos;
import com.revature.ers.models.AppUser;
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
 * New:
 * --deletes user
 * --updates user
 */
public class UserRepository {
    // extract common query clauses into a easily referenced member for reusability.
    private String baseQuery = "SELECT * FROM project1.ers_users au " +
                               "JOIN ers_user_roles ur " +
                               "ON au.user_role_id = ur.role_id ";
    public UserRepository() {
        System.out.println("[LOG] - Instantiating " + this.getClass().getName());
    }

    /**
     * Finds a user by given ID
     * @param id
     * @return
     */
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
    /**
     * Gets all the users. Called by managers/admins
     * @return
     */
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
    /**
     * Used for logging in.
     * @param username
     * @param password
     * @param role
     * @return
     */
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
    /**
     * Used before user creation to ensure username isn't already taken
     * @param username
     * @return
     */
    public Optional<AppUser> findUserByUsername(String username) {

        Optional<AppUser> _user = Optional.empty();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = baseQuery + "WHERE username = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            _user = mapResultSet(rs).stream().findFirst();


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;

    }
    /**
     * Used to ensure email isn't already taken.
     * @param email
     * @return
     */
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
    /**
     * For creating new users
     * @param newUser
     */
    public void save(AppUser newUser) {
        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = "INSERT INTO project1.ers_users (username, password, first_name, last_name, email, user_role_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";

            // second parameter here is used to indicate column names that will have generated values
            PreparedStatement pstmt = conn.prepareStatement(sql); //, new String[] {"id"} <buggy
            pstmt.setString(1, newUser.getUsername());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getFirstName());
            pstmt.setString(4, newUser.getLastName());
            pstmt.setString(5, newUser.getEmail());
            pstmt.setInt(6, newUser.getRole());
            System.out.println(pstmt);
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }
    /**
     * For editing users by ID
     * @param updatedUser
     */
    public void update(AppUser updatedUser) {
        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = "Update project1.ers_users " +
                            "SET username = ?, " +
                                "password = ?, " +
                                "first_name = ?, " +
                                "last_name = ?, " +
                                "user_role_id = ?, " +
                                "email = ? " +
                            "WHERE ers_user_id = ?";


            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updatedUser.getUsername());
            pstmt.setString(2, updatedUser.getPassword());
            pstmt.setString(3, updatedUser.getFirstName());
            pstmt.setString(4, updatedUser.getLastName());
            pstmt.setInt(5, updatedUser.getRole());
            pstmt.setString(6, updatedUser.getEmail());
            pstmt.setInt(7, updatedUser.getId());
            System.out.println(pstmt);
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }


    }
    /**
     * Doesn't actually delete the user, but rewrites the data as deleted (to dodge the ticket constraints)
     * @param target
     */
    public void delete(AppUser target){
        //emails are unique
        //i was going to change this back to delete by ID, but i'm in too deep now
        //since there's a constraint preventing deleting rows with users who have resolved or submitted tickets,
            //we'll instead change this to updating all user fields to DELETED
            //mostly b/c i think the tickets should stay on the server for the employee's future records
            //and just in case the company gets investigated for tax evasion or fraud or something idk i'm not a cop
        String email = target.getEmail();
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
//            String sql = "DELETE FROM project1.ers_users " +
//                    "WHERE email = ?";
            String sql = "Update project1.ers_users " +
                    "SET username = ?, " +
                    "password = ' ', " +
                    "first_name = ' ', " +
                    "last_name = ' ', " +
                    "email = ?, " +
                    "user_role_id = 0 " +
                    "WHERE email = ?";

            String hashDelete = "DELETED"+target.hashCode();
            //this right here is some high level hacky stuff
            //i did this because emails and usernames must be unique by constraint, and future deletes would've been duplicates

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hashDelete);
            pstmt.setString(2, hashDelete);
            pstmt.setString(3, email);
            System.out.println(pstmt);
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }
    /**
     * Maps the results into a user set
     * @param rs
     * @return
     * @throws SQLException
     */
    private Set<AppUser> mapResultSet(ResultSet rs) throws SQLException {

        Set<AppUser> users = new HashSet<>();

        while (rs.next()) {
            AppUser temp = new AppUser();
            temp.setId(rs.getInt("ers_user_id"));
            System.out.println("Mapping... ID: "+temp.getId());
            temp.setUsername(rs.getString("username"));
            temp.setPassword(rs.getString("password"));
            temp.setFirstName(rs.getString("first_name"));
            temp.setLastName(rs.getString("last_name"));
            temp.setEmail(rs.getString("email"));
            temp.setRole(rs.getInt("user_role_id"));
            temp.setRoleName(rs.getString("role_name"));
            users.add(temp);
        }

        return users;

    }
}
