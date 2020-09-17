package com.revature.ers.repos;

import com.revature.ers.dtos.Principal;
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

    /**
     * Finds the tickets belonging to a specific user
     * @param username
     * @return
     */
    public Set<Ticket> findTicketsByUsername(String username) {
        Set<Ticket> tickets = new HashSet<>();
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
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
                            "on er.resolver_id = eu2.ers_user_id " +
                         "WHERE eu.username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            System.out.println(pstmt);
            ResultSet rs = pstmt.executeQuery();
            tickets = mapResultSet(rs);
            //before, it gave me a syntax error at end of input
            //solved this by removing stmt variable with sql in the argument of execute query
                //stmt vs pstmt

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }
    /**
     * Gets all the tickets on the db. For use by managers/admins
     * @return
     */
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
    /**
     * For use by managers when resolving (approving/denying) tickets
     * @param ticket
     */
    public void resolve(Ticket ticket){
        //id, resolve, resolver, status
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "Update project1.ers_reimbursements " +
                    "SET resolved = ?, " +
                    "resolver_id = ?, " +
                    "reimb_status_id = ? " +
                    "WHERE reimb_id = ?";

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
    /**
     * For use by employees when editing a pending ticket. Only pending tickets may be edited.
     * @param ticket
     */
    public void editPending(Ticket ticket){
        //id, amount, desc, type
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "Update project1.ers_reimbursements " +
                    "SET amount = ?, " +
                    "description = ?, " +
                    "reimb_type_id = ? " +
                    "WHERE reimb_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, ticket.getAmount());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setInt(3, ticket.getTypeID());
            pstmt.setInt(4, ticket.getId());
            System.out.println(pstmt);
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     * For submitting new tickets from employees.
     * @param ticket
     */
    public void submit(Ticket ticket){
        //submitted, author, status, amount, description, typeID
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "INSERT into project1.ers_reimbursements " +
                    "(amount, description, submitted, author_id, reimb_type_id, reimb_status_id) " +
                    "VALUES (?, ?, ?, ?, ?, 1)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, ticket.getAmount());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setTimestamp(3, ticket.getSubmitted());
            pstmt.setInt(4, ticket.getAuthorID());
            pstmt.setInt(5, ticket.getTypeID());
            System.out.println(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     * Ensures that the requested ticket(s) belong to the calling user
     * @param ticket
     * @return
     */
    public boolean validateTicketWithUser(Ticket ticket){
        //make sure the user is editing their own ticket
        //also make sure its PENDING
        try(Connection conn = ConnectionService.getInstance().getConnection()){
            String sql = "select * from ers_reimbursements " +
                            "where reimb_id = ? and author_id = ? and reimb_status_id = 1";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ticket.getId());
            pstmt.setInt(2, ticket.getAuthorID()); //set from servlet
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                //if there's a thing (should only be one), it's valid
                return true;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }
    /**
     * Maps the query results into a set for further manipulation or for JSON packages for a response
     * @param rs
     * @return
     * @throws SQLException
     */
    private Set<Ticket> mapResultSet(ResultSet rs) throws SQLException {
        System.out.println("Mapper invoked.");
        Set<Ticket> tickets = new HashSet<>();
        while(rs.next()) {
            System.out.println("rs.next found a row...");
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
            // it was a pain to get the date into the json without it bugging out,
            // fixed this by imbedding a date-to-string method in both submitted and resolved set methods
            // when we parse the data on the front end,
                // instead of pulling data from timestamp data, pull from string versions
        }
        return tickets;
    }
}
