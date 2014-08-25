package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by simonyu on 25/08/2014.
 */
@Entity
@Table(name = "collection_party")
public class MCollectionParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(targetEntity = MCollection.class)
    @JoinColumn(name = "collection_id", nullable = false)
    private MCollection collection;

    @ManyToOne(targetEntity = MParty.class)
    @JoinColumn(name = "party_id", nullable = false)
    private MParty party;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation")
    private MRelationType relationType;

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

    public MParty getParty() {
        return party;
    }

    public void setParty(MParty party) {
        this.party = party;
    }

    public MRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(MRelationType relationType) {
        this.relationType = relationType;
    }
}
