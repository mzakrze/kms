package pl.mzakrze.kms.user_drive.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "tag")
public class Tag {
    private String gid;
    private String name;
    private Document document;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "gid", unique = true, nullable = false, updatable = false)
    public String getGid() {
        return gid;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "document_gid")
    public Document getDocument() {
        return document;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
