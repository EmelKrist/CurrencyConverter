package org.emel.ClientService.models;

import javax.persistence.*;

/**
 * Класс модели(сущности) Currency
 */
@Entity
@Table(name = "Currency")
public class Currency {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;

    public Currency(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Currency() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
