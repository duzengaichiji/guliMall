package com.nhx.gmall.bean;
import javax.persistence.*;


import java.io.Serializable;
import java.util.List;

public class PmsBaseCatalog2 implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private Integer catalog1Id;

    @Transient
    private List<PmsBaseCatalog3> catalog3List;

    public List<PmsBaseCatalog3> getCatalog3List() {
        return catalog3List;
    }

    public void setCatalog3List(List<PmsBaseCatalog3> catalog3List) {
        this.catalog3List = catalog3List;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCatalog1Id() {
        return catalog1Id;
    }

    public void setCatalog1Id(Integer catalog1Id) {
        this.catalog1Id = catalog1Id;
    }
}