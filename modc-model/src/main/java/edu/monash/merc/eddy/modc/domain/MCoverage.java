package edu.monash.merc.eddy.modc.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by simonyu on 22/08/2014.
 */
@Entity
@Table(name = "coverage")
public class MCoverage extends Domain {
    @Id
    @GeneratedValue(generator = "pk_generator")
    @GenericGenerator(name = "pk_generator", strategy = "org.hibernate.id.enhanced.TableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "table_name", value = "pk_gen_tab"),
                    @org.hibernate.annotations.Parameter(name = "value_column_name ", value = "pk_next_val"),
                    @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "pk_name"),
                    @org.hibernate.annotations.Parameter(name = "segment_value", value = "coverage_id"),
                    @org.hibernate.annotations.Parameter(name = "increment_size  ", value = "5"),
                    @org.hibernate.annotations.Parameter(name = "optimizer ", value = "hilo")
            })
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
