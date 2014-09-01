package edu.monash.merc.eddy.modc.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by simonyu on 1/08/2014.
 */
@Entity
@Table(name = "collection")
public class MCollection extends Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ref_key")
    private String refKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MCollectionType type;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 4000)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endDate;

    @Column(name = "access_rights", length = 2000)
    private String accessRights;

    @OneToOne(mappedBy = "collection", targetEntity = MAddress.class, fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private MAddress address;

    @OneToOne(mappedBy = "collection", targetEntity = MCoverage.class, fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private MCoverage coverage;

    @OneToMany(mappedBy = "collection", targetEntity = MPublication.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE})
    private List<MPublication> publications;

    @OneToMany(mappedBy = "collection", targetEntity = MCitation.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE})
    private List<MCitation> citations;

    @OneToMany(mappedBy = "collection", targetEntity = MCollectionIdentifier.class, fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private List<MCollectionIdentifier> collectionIdentifiers;

    @OneToMany(mappedBy = "collection", targetEntity = MCollectionParty.class, fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private List<MCollectionParty> collectionParties;

    @OneToMany(mappedBy = "collection", targetEntity = MCollectionKeyword.class, fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private List<MCollectionKeyword> collectionKeywords;

    @OneToMany(mappedBy = "collection", targetEntity = MLicence.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE})
    private List<MLicence> licences;

    @ManyToOne(targetEntity = MProject.class)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private MProject project;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRefKey() {
        return refKey;
    }

    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }

    public MCollectionType getType() {
        return type;
    }

    public void setType(MCollectionType type) {
        this.type = type;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }

    public MAddress getAddress() {
        return address;
    }

    public void setAddress(MAddress address) {
        this.address = address;
    }

    public MCoverage getCoverage() {
        return coverage;
    }

    public void setCoverage(MCoverage coverage) {
        this.coverage = coverage;
    }

    public List<MPublication> getPublications() {
        return publications;
    }

    public void setPublications(List<MPublication> publications) {
        this.publications = publications;
    }

    public List<MCitation> getCitations() {
        return citations;
    }

    public void setCitations(List<MCitation> citations) {
        this.citations = citations;
    }

    public List<MCollectionIdentifier> getCollectionIdentifiers() {
        return collectionIdentifiers;
    }

    public void setCollectionIdentifiers(List<MCollectionIdentifier> collectionIdentifiers) {
        this.collectionIdentifiers = collectionIdentifiers;
    }

    public List<MCollectionParty> getCollectionParties() {
        return collectionParties;
    }

    public void setCollectionParties(List<MCollectionParty> collectionParties) {
        this.collectionParties = collectionParties;
    }

    public List<MCollectionKeyword> getCollectionKeywords() {
        return collectionKeywords;
    }

    public void setCollectionKeywords(List<MCollectionKeyword> collectionKeywords) {
        this.collectionKeywords = collectionKeywords;
    }

    public List<MLicence> getLicences() {
        return licences;
    }

    public void setLicences(List<MLicence> licences) {
        this.licences = licences;
    }

    public MProject getProject() {
        return project;
    }

    public void setProject(MProject project) {
        this.project = project;
    }
}
