package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;

/**
 * Created by simonyu on 25/08/2014.
 */
@Entity
@Table (name = "collection_identifier")
public class MCollectionIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne (targetEntity = MCollection.class)
    @JoinColumn (name = "collection_id")
    private MCollection collection;

    @ManyToOne (targetEntity = MIdentifier.class)
    @JoinColumn (name = "identifier_id")
    private MIdentifier identifier;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MCollection getCollection() {
        return collection;
    }

    public void setCollection(MCollection collection) {
        this.collection = collection;
    }

    public MIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(MIdentifier identifier) {
        this.identifier = identifier;
    }
}
