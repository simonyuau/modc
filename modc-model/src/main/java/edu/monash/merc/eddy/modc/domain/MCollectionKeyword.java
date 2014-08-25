package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;

/**
 * Created by simonyu on 25/08/2014.
 */
@Entity
@Table(name = "collection_keyword")
public class MCollectionKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(targetEntity = MCollection.class)
    @JoinColumn(name = "collection_id")
    private MCollection collection;

    @ManyToOne(targetEntity = MKeyword.class)
    @JoinColumn(name = "keyword_id")
    private MKeyword keyword;

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

    public MKeyword getKeyword() {
        return keyword;
    }

    public void setKeyword(MKeyword keyword) {
        this.keyword = keyword;
    }
}
