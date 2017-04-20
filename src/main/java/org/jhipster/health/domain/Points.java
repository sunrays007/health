package org.jhipster.health.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Points.
 */
@Entity
@Table(name = "points")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "points")
public class Points implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @Column(name = "exercise")
    private Integer exercise;

    @Column(name = "meals")
    private Integer meals;

    @Column(name = "alcolhol")
    private Integer alcolhol;

    @Size(max = 140)
    @Column(name = "notes", length = 140)
    private String notes;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Points date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getExercise() {
        return exercise;
    }

    public Points exercise(Integer exercise) {
        this.exercise = exercise;
        return this;
    }

    public void setExercise(Integer exercise) {
        this.exercise = exercise;
    }

    public Integer getMeals() {
        return meals;
    }

    public Points meals(Integer meals) {
        this.meals = meals;
        return this;
    }

    public void setMeals(Integer meals) {
        this.meals = meals;
    }

    public Integer getAlcolhol() {
        return alcolhol;
    }

    public Points alcolhol(Integer alcolhol) {
        this.alcolhol = alcolhol;
        return this;
    }

    public void setAlcolhol(Integer alcolhol) {
        this.alcolhol = alcolhol;
    }

    public String getNotes() {
        return notes;
    }

    public Points notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public Points user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Points points = (Points) o;
        if (points.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, points.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Points{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", exercise='" + exercise + "'" +
            ", meals='" + meals + "'" +
            ", alcolhol='" + alcolhol + "'" +
            ", notes='" + notes + "'" +
            '}';
    }
}
