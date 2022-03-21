package com.bootcamp.reactive.blog.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(value="authors")
public class Author {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime birthDate;
    @JsonIgnore
	public Boolean isMayorEdad()
	{
		if (birthDate != null) {
		   LocalDate fechaNacimiento = birthDate.toLocalDate();
	       Period edad = Period.between(fechaNacimiento, LocalDate.now());
	    	if(edad.getYears()>=18){
	    		return true;
	    	}
		}
	   return false;
	}
}
