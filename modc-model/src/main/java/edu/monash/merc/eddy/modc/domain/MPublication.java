package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;

/**
 * Created by simonyu on 22/08/2014.
 */
@Entity
@Table(name = "publication")
public class MPublication extends Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @ManyToOne(targetEntity = MCollection.class)
    @JoinColumn(name = "collection_id", referencedColumnName = "id", nullable = false)
    private MCollection collection;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MCollection getCollection() {
        return collection;
    }

    public void setCollection(MCollection collection) {
        this.collection = collection;
    }
}
