package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by simonyu on 4/08/2014.
 */
@Entity
@Table(name = "m_project")
public class MProject extends Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "unique_id", length = 200)
    private String uniqueId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registered_date")
    private Date registerDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified")
    private Date lastModified;

    @Basic
    @Column(name = "auto_publish")
    private boolean autoPublish;

    @ManyToOne (targetEntity = User.class)
    @JoinColumn(name = "project_admin", referencedColumnName = "id", nullable = false)
    private User user;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isAutoPublish() {
        return autoPublish;
    }

    public void setAutoPublish(boolean autoPublish) {
        this.autoPublish = autoPublish;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
