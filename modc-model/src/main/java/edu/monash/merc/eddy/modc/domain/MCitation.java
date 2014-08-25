package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;

/**
 * Created by simonyu on 22/08/2014.
 */
@Entity
@Table(name = "citation")
public class MCitation extends Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "citation", length = 2000)
    private String citation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MCitationType type;

    @ManyToOne(targetEntity = MCollection.class)
    @JoinColumn(name = "collection_id", referencedColumnName = "id", nullable = false)
    private MCollection collection;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public MCitationType getType() {
        return type;
    }

    public void setType(MCitationType type) {
        this.type = type;
    }

    public MCollection getCollection() {
        return collection;
    }

    public void setCollection(MCollection collection) {
        this.collection = collection;
    }
}
