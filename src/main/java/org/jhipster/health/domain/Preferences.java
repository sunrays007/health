package org.jhipster.health.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import org.jhipster.health.domain.enumeration.Units;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "preferences")
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 10)
    @Max(value = 21)
    @Column(name = "weekly_goal", nullable = false)
    private Integer weekly_goal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "weight_units", nullable = false)
    private Units weight_units;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekly_goal() {
        return weekly_goal;
    }

    public Preferences weekly_goal(Integer weekly_goal) {
        this.weekly_goal = weekly_goal;
        return this;
    }

    public void setWeekly_goal(Integer weekly_goal) {
        this.weekly_goal = weekly_goal;
    }

    public Units getWeight_units() {
        return weight_units;
    }

    public Preferences weight_units(Units weight_units) {
        this.weight_units = weight_units;
        return this;
    }

    public void setWeight_units(Units weight_units) {
        this.weight_units = weight_units;
    }

    public User getUser() {
        return user;
    }

    public Preferences user(User user) {
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
        Preferences preferences = (Preferences) o;
        if (preferences.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, preferences.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + id +
            ", weekly_goal='" + weekly_goal + "'" +
            ", weight_units='" + weight_units + "'" +
            '}';
    }
}
