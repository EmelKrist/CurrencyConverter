package org.emel.ClientService.models;

import lombok.*;

import javax.persistence.*;

/**
 * Класс модели(сущности) Currency
 */
@Entity
@Table(name = "Currency")
@Getter
@Setter
@NoArgsConstructor
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
}
