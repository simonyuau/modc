package edu.monash.merc.eddy.modc.domain;

import javax.persistence.*;

/**
 * Created by simonyu on 22/08/2014.
 */
@Entity
@Table(name = "address")
public class MAddress extends Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "postal_address", length = 1000)
    private String postalAddress;

    @Column(name = "electronic_address")
    private String electronicAddess;

    @Enumerated(EnumType.STRING)
    @Column(name = "electronic_address_type")
    private MAddressType type;

    @OneToOne(targetEntity = MCollection.class)
    @JoinColumn(name = "collection_id", referencedColumnName = "id", nullable = false)
    private MCollection collection;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getElectronicAddess() {
        return electronicAddess;
    }

    public void setElectronicAddess(String electronicAddess) {
        this.electronicAddess = electronicAddess;
    }

    public MAddressType getType() {
        return type;
    }

    public void setType(MAddressType type) {
        this.type = type;
    }

    public MCollection getCollection() {
        return collection;
    }

    public void setCollection(MCollection collection) {
        this.collection = collection;
    }
}
