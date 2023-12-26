package com.example.springboot.model;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.hateoas.RepresentationModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_products")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable{

	private static final long serialVersionUID = 5150844720896189441L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String name;
	private double value;
	
	public ProductModel() {
		
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	

}
