package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
@Table(name = "catalog")
public class CatalogEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 120,unique = true)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer unitPrice;


    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;




}
