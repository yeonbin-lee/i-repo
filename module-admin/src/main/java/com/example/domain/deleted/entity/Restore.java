package com.example.domain.deleted.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class Restore {

    @Id
    @Column(name = "restore_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column
    private String reason;

    @Builder
    public Restore(Long id, String email, LocalDate date, String reason) {
        this.id = id;
        this.email = email;
        this.date = date;
        this.reason =reason;
    }
}
