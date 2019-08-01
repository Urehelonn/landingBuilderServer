package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;


@Entity
@Table(name = "builders")
public class Builder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    @CreationTimestamp
    private Date createdAt;

    @Temporal(TIMESTAMP)
    @Column(name = "modified_at", nullable = false, columnDefinition = "TIMESTAMP  default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Date modifiedAt;

    @Column(columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(columnDefinition = "varchar(512) default ''")
    private String logoUrl;

    @OneToOne(mappedBy = "builder")
    @JsonIgnore
    private User user;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "head_id", referencedColumnName = "id")
    private Head head;


    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "gallery_id", referencedColumnName = "id")
    private Gallery gallery;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }

}
