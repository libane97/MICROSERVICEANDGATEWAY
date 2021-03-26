package dj.djibgate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Checkup.
 */
@Entity
@Table(name = "checkup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Checkup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "date_check", nullable = false)
    private Instant dateCheck;

    @OneToMany(mappedBy = "checkup")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Checkpoints> checkpoints = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "checkups", allowSetters = true)
    private Voiture voiture;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Checkup libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Instant getDateCheck() {
        return dateCheck;
    }

    public Checkup dateCheck(Instant dateCheck) {
        this.dateCheck = dateCheck;
        return this;
    }

    public void setDateCheck(Instant dateCheck) {
        this.dateCheck = dateCheck;
    }

    public Set<Checkpoints> getCheckpoints() {
        return checkpoints;
    }

    public Checkup checkpoints(Set<Checkpoints> checkpoints) {
        this.checkpoints = checkpoints;
        return this;
    }

    public Checkup addCheckpoints(Checkpoints checkpoints) {
        this.checkpoints.add(checkpoints);
        checkpoints.setCheckup(this);
        return this;
    }

    public Checkup removeCheckpoints(Checkpoints checkpoints) {
        this.checkpoints.remove(checkpoints);
        checkpoints.setCheckup(null);
        return this;
    }

    public void setCheckpoints(Set<Checkpoints> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public Checkup voiture(Voiture voiture) {
        this.voiture = voiture;
        return this;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Checkup)) {
            return false;
        }
        return id != null && id.equals(((Checkup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Checkup{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateCheck='" + getDateCheck() + "'" +
            "}";
    }
}
