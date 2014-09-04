package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by simonyu on 22/08/2014.
 */
@Entity
@Table(name = "keyword")
public class MKeyword extends Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "keyword")
    private String keyword;

    @ManyToMany(mappedBy = "keywords")
    private List<MCollection> collections;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<MCollection> getCollections() {
        return collections;
    }

    public void setCollections(List<MCollection> collections) {
        this.collections = collections;
    }
}