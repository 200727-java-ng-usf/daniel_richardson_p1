package com.revature.ers.repos;

import com.revature.ers.models.AppUser;
import com.revature.ers.models.Ticket;
import com.revature.ers.services.ConnectionService;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * TicketRepo: Data Access Object to drink up that postgresql milkshake of data
 * --finds tickets by
 * -- -ID
 * -- -All
 * --saves ticket
 * --maps ticket
 * --updates ticket
 * --approves ticket
 * --denies ticket
 */

public class TicketRepository {


    public TicketRepository() {
        System.out.println("[LOG] - Instantiating " + this.getClass().getName());
    }


//    public Optional<Ticket> findTicketById(int id) {
//        Optional<Ticket> _ticket = Optional.empty();
//
//        try (Connection conn = ConnectionService.getInstance().getConnection()) {
//
//            String sql = "WHERE au.id = ?";
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, id);
//
//            Set<AppUser> result = mapResultSet(pstmt.executeQuery());
//            if (!result.isEmpty()) {
//                _user = result.stream().findFirst();
//            }
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//        }
//
//        return _user;
//
//    }

    public Set<Ticket> findAllTickets() {

        Set<Ticket> tickets = new HashSet<>();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            // my people call this 'fettuccini sql'
            String sql = "SELECT distinct er.reimb_id, "+
                            "eu.username, "+
                            "er.amount, "+
                            "er.description, "+
                            "er.submitted, "+
                            "er.resolved, "+
                            "eu2.username as resolver, "+
                            "rs.reimb_status, "+
                            "rt.reimb_type "+
                        "FROM project1.ers_reimbursements er "+
                        "JOIN project1.ers_reimbursement_types rt "+
                        "ON er.reimb_type_id = rt.reimb_type_id "+
                        "JOIN project1.ers_reimbursement_statuses rs "+
                        "ON er.reimb_status_id = rs.reimb_status_id "+
                        "join project1.ers_users eu "+
                        "on er.author_id = eu.ers_user_id "+
                        "left outer join project1.ers_users eu2 "+
                        "on er.resolver_id = eu2.ers_user_id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            tickets = mapResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickets;

    }

//    public void save(Ticket ticket) {
//
//        try (Connection conn = ConnectionService.getInstance().getConnection()) {
//
//            String sql = "INSERT INTO project1.ers_users (username, password, first_name, last_name, email, user_role_id) " +
//                    "VALUES (?, ?, ?, ?, ?, ?)";
//
//            // second parameter here is used to indicate column names that will have generated values
//            PreparedStatement pstmt = conn.prepareStatement(sql); //, new String[] {"id"} <buggy
//            pstmt.setString(1, newUser.getUsername());
//            pstmt.setString(2, newUser.getPassword());
//            pstmt.setString(3, newUser.getFirstName());
//            pstmt.setString(4, newUser.getLastName());
//            pstmt.setString(5, newUser.getEmail());
//            pstmt.setInt(6, newUser.getRole());
//            System.out.println(pstmt);
//            pstmt.executeUpdate();
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//        }
//
//    }

//    public void update(Ticket updatedTicket) { //for employees to update their ticket
//        try (Connection conn = ConnectionService.getInstance().getConnection()) {
//
//            String sql = "Update project1.ers_users " +
//                    "SET username = ?, " +
//                    "password = ?, " +
//                    "first_name = ?, " +
//                    "last_name = ?, " +
//                    "user_role_id = ? " +
//                    "WHERE email = ?";
//
//            // second parameter here is used to indicate column names that will have generated values
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, updatedUser.getUsername());
//            pstmt.setString(2, updatedUser.getPassword());
//            pstmt.setString(3, updatedUser.getFirstName());
//            pstmt.setString(4, updatedUser.getLastName());
//            pstmt.setInt(5, updatedUser.getRole());
//            pstmt.setString(6, updatedUser.getEmail());
//            System.out.println(pstmt);
//            pstmt.executeUpdate();
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//        }
//
//
//    }

    public void resolve(Ticket ticket){ //manager method
        //id, resolve, resolver, status

        //debug

        System.out.println(ticket.getResolve());
        try (Connection conn = ConnectionService.getInstance().getConnection()) {

            String sql = "Update project1.ers_reimbursements " +
                    "SET resolved = ?, " +
                    "resolver_id = ?, " +
                    "reimb_status_id = ? " +
                    "WHERE reimb_id = ?";

            // second parameter here is used to indicate column names that will have generated values
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, ticket.getResolve());
            pstmt.setInt(2, ticket.getResolverID());
            pstmt.setInt(3, ticket.getStatusID());
            pstmt.setInt(4, ticket.getId());
            System.out.println(pstmt);
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }


    private Set<Ticket> mapResultSet(ResultSet rs) throws SQLException {

        Set<Ticket> tickets = new HashSet<>();

        while(rs.next()) {
            Ticket temp = new Ticket();
            temp.setId(rs.getInt("reimb_id"));
            temp.setAmount(rs.getDouble("amount"));
            temp.setSubmitted(rs.getTimestamp("submitted"));
            temp.setResolve(rs.getTimestamp("resolved"));
            temp.setDescription(rs.getString("description"));
            temp.setAuthor(rs.getString("username"));
            temp.setResolver(rs.getString("resolver"));
            temp.setType(rs.getString("reimb_type"));
            temp.setStatus(rs.getString("reimb_status"));
            tickets.add(temp);
        }

        return tickets;

    }

}
