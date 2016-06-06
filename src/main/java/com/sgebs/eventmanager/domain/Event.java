package com.sgebs.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Min(value = 0)
    @Max(value = 8)
    @Column(name = "duration", nullable = false)
    private Float duration;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "details")
    private String details;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private Set<Invitation> invitations = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Set<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(Set<Invitation> invitations) {
        this.invitations = invitations;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) { this.duration = duration; }

    public  ZonedDateTime getEndDate(){
        long minutes;
        long duration = Math.round(getDuration() *100.0);
        Long durationH = duration/100;
        Long durationM = duration%100;
        minutes = durationH * 60 + durationM * 60 / 100;

        return date.plusMinutes(minutes);
    }

    public boolean isIntersect(Event anotherEvent){
        ZonedDateTime anotherStartDate =  anotherEvent.getDate();
        ZonedDateTime anotherEndDate =  anotherEvent.getEndDate();

        ZonedDateTime startDate = this.date;
        ZonedDateTime endDate  = this.getEndDate();

        if(((startDate.isAfter(anotherStartDate) && startDate.isBefore(anotherEndDate) ||
            (endDate.isAfter(anotherStartDate) && endDate.isBefore(anotherEndDate)) ||
            (startDate.isBefore(anotherStartDate) && endDate.isAfter(anotherEndDate))))){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if(event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", date='" + date + "'" +
            ", duration='" + duration + "'" +
            ", details='" + details + "'" +
            '}';
    }
}
