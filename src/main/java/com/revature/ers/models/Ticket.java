package com.revature.ers.models;

import java.sql.Timestamp;
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
    private Timestamp submitted;
    private Timestamp resolve;
    private String description;
    private String author; //this is the author's id, but we'll fetch the username
    private String resolver; //manager's id
    private String status; //status id, check
    private String type; //
    //ignoring receipt for now


    //constructors=======================
    //no args
    public Ticket(){}

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

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                ", resolver='" + resolver + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
