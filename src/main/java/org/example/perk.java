package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class perk {
    @Id
    @GeneratedValue
    private int pid;

}
