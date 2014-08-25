package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;

/**
 * Created by simonyu on 22/08/2014.
 */
@Entity
@Table(name = "coverage")
public class MCoverage extends Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "coverage")
    private String coverage;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MCoverageType type;

    @OneToOne(targetEntity = MCollection.class)
    @JoinColumn(name = "collection_id", referencedColumnName = "id", nullable = false)
    private MCollection collection;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public MCoverageType getType() {
        return type;
    }

    public void setType(MCoverageType type) {
        this.type = type;
    }

    public MCollection getCollection() {
        return collection;
    }

    public void setCollection(MCollection collection) {
        this.collection = collection;
    }
}
