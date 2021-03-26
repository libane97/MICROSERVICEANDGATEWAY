package dj.djibgate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Voiture.
 */
@Entity
@Table(name = "voiture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Voiture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "moteur", nullable = false)
    private String moteur;

    @OneToMany(mappedBy = "voiture")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Checkup> checkups = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "voitures", allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Voiture title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Voiture price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMoteur() {
        return moteur;
    }

    public Voiture moteur(String moteur) {
        this.moteur = moteur;
        return this;
    }

    public void setMoteur(String moteur) {
        this.moteur = moteur;
    }

    public Set<Checkup> getCheckups() {
        return checkups;
    }

    public Voiture checkups(Set<Checkup> checkups) {
        this.checkups = checkups;
        return this;
    }

    public Voiture addCheckup(Checkup checkup) {
        this.checkups.add(checkup);
        checkup.setVoiture(this);
        return this;
    }

    public Voiture removeCheckup(Checkup checkup) {
        this.checkups.remove(checkup);
        checkup.setVoiture(null);
        return this;
    }

    public void setCheckups(Set<Checkup> checkups) {
        this.checkups = checkups;
    }

    public Client getClient() {
        return client;
    }

    public Voiture client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voiture)) {
            return false;
        }
        return id != null && id.equals(((Voiture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Voiture{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", moteur='" + getMoteur() + "'" +
            "}";
    }
}
