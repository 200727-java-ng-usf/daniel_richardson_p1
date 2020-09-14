package com.revature.ers.models;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Ticket {
//    serial reimb_id
//    int amount
//    time submitted
//    time resolved
//    str description
//    reciept string
//    author_id int fk
//    resolver_id int fk
//    reimb_status_id int fk
//    reimb_type_id int fk

    // fields/attributes
    private int id;
    private double amount;
    private Timestamp submitted; //https://alvinalexander.com/java/java-timestamp-example-current-time-now/
    private Timestamp resolve; //RESOLVED timestamp
    private String description;
    private String author; //this is the author's id, but we'll fetch the username
    private int authorID; //and get the id
    private String resolver; //manager
    private int resolverID;
    private String status; //status id
    private int statusID;
    private String type; //
    private int typeID;
    //ignoring receipt for now


    //constructors=======================
    //no args
    public Ticket(){}

    //for when managers resolve a ticket
    // todo i don't think this is getting mapped... force set the timestamp with setter
    public Ticket(int id, int resolverID, String status){
        //make new timestamp
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());

        this.id = id; //sql WHERE
        this.resolve = currentTimestamp; //update
        this.resolverID = resolverID; //update
        this.status = status; //update
    }

    //full buckaroo constructor
    public Ticket(int id, int amount, Timestamp submitted, Timestamp resolve, String description, String author, String resolver, String status, String type) {
        this.id = id;
        this.amount = amount;
        this.submitted = submitted;
        this.resolve = resolve;
        this.description = description;
        this.author = author;
        this.resolver = resolver;
        this.status = status;
        this.type = type;
    }
    //minus id
    public Ticket(int amount, Timestamp submitted, Timestamp resolve, String description, String author, String resolver, String status, String type) {
        this.amount = amount;
        this.submitted = submitted;
        this.resolve = resolve;
        this.description = description;
        this.author = author;
        this.resolver = resolver;
        this.status = status;
        this.type = type;
    }
    //newly created by employee
    //minus id, resolved, resolver
    public Ticket(int amount, Timestamp submitted, String description, String author, String status, String type) {
        this.amount = amount;
        this.submitted = submitted;
        this.description = description;
        this.author = author;
        this.status = status;
        this.type = type;
    }
    //util
    public void setResolvedWithCurrentTime(){
        //make new timestamp
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());
        this.resolve = currentTimestamp;
    }
    public void setSubmittedWithCurrentTime(){
        //make new timestamp
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());
        this.submitted = currentTimestamp;
    }

    //getter/setters ======================


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Timestamp getResolve() {
        return resolve;
    }

    public void setResolve(Timestamp resolve) {
        this.resolve = resolve;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public int getResolverID() {
        return resolverID;
    }

    public void setResolverID(int resolverID) {
        this.resolverID = resolverID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id &&
                Double.compare(ticket.amount, amount) == 0 &&
                Objects.equals(submitted, ticket.submitted) &&
                Objects.equals(resolve, ticket.resolve) &&
                Objects.equals(description, ticket.description) &&
                author.equals(ticket.author) &&
                Objects.equals(resolver, ticket.resolver) &&
                status.equals(ticket.status) &&
                type.equals(ticket.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, submitted, resolve, description, author, resolver, status, type);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", amount=" + amount +
                ", submitted=" + submitted +
                ", resolve=" + resolve +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", authorID=" + authorID +
                ", resolver='" + resolver + '\'' +
                ", resolverID=" + resolverID +
                ", status='" + status + '\'' +
                ", statusID=" + statusID +
                ", type='" + type + '\'' +
                ", typeID=" + typeID +
                '}';
    }
}
