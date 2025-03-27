package com.solcare.solar.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CollectionId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 50, unique = true)
	private String name;
	
}
