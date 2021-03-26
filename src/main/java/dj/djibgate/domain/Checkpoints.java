package dj.djibgate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Checkpoints.
 */
@Entity
@Table(name = "checkpoints")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Checkpoints implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "note", nullable = false)
    private Integer note;

    @ManyToOne
    @JsonIgnoreProperties(value = "checkpoints", allowSetters = true)
    private Checkup checkup;

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

    public Checkpoints libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getNote() {
        return note;
    }

    public Checkpoints note(Integer note) {
        this.note = note;
        return this;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Checkup getCheckup() {
        return checkup;
    }

    public Checkpoints checkup(Checkup checkup) {
        this.checkup = checkup;
        return this;
    }

    public void setCheckup(Checkup checkup) {
        this.checkup = checkup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Checkpoints)) {
            return false;
        }
        return id != null && id.equals(((Checkpoints) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Checkpoints{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", note=" + getNote() +
            "}";
    }
}
