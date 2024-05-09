package com.java.marketplace.resources;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.java.marketplace.dto.ProductOfferingDTO;
import com.java.marketplace.dto.ProductOfferingFindByIdDTO;
import com.java.marketplace.entities.ProductOffering;
import com.java.marketplace.services.ProductOfferingService;

@RestController
@RequestMapping(value = "/productOfferings")
public class ProductOfferingResource {
	
	@Autowired
	private ProductOfferingService service;
	
	@GetMapping
	public ResponseEntity<Page<ProductOfferingDTO>> findAll(
			Pageable pageable,
			@RequestParam(required = false, defaultValue = "sellIndicator") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder){

        Sort.Order firstField = new Sort.Order(Sort.Direction.fromString(sortOrder), sortField);
        Sort.Order lastField = new Sort.Order(Sort.Direction.fromString("desc"), "stock");
		//sort
//		Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
		Sort sort = Sort.by(firstField, lastField);
		//pageable
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		//request
		Page<ProductOffering> list = service.findAll(pageable);
//		Page<ProductOfferingDTO> listDto = list.stream().map(x -> new ProductOfferingDTO(x)).collect(Collectors.toList());
		Page<ProductOfferingDTO> dtoPage = list.map(new Function<ProductOffering, ProductOfferingDTO>() {
		    @Override
		    public ProductOfferingDTO apply(ProductOffering entity) {
		    	ProductOfferingDTO dto = new ProductOfferingDTO(entity);
		        return dto;
		    }
		});
		return ResponseEntity.ok().body(dtoPage);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductOfferingFindByIdDTO> findById(@PathVariable Long id){
		ProductOffering obj = service.findById(id);
		ProductOfferingFindByIdDTO objDTO = new ProductOfferingFindByIdDTO(obj);
		return ResponseEntity.ok().body(objDTO);
	}
	
	@GetMapping(value = "/short/{id}")
	public ResponseEntity<ProductOfferingDTO> findByIdoOperator(@PathVariable Long id){
		ProductOffering obj = service.findByIdShort(id);
		ProductOfferingDTO objDTO = new ProductOfferingDTO(obj);
		return ResponseEntity.ok().body(objDTO);
	}
	
//	create new product
	@PostMapping
	public ResponseEntity<ProductOfferingDTO> insert(@Valid @RequestBody ProductOfferingDTO obj) {
		ProductOfferingDTO objDTO = new ProductOfferingDTO(service.insert(obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(objDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductOfferingDTO> update(@PathVariable Long id, @Valid @RequestBody ProductOfferingDTO obj) {
		obj = new ProductOfferingDTO(service.update(id, obj));
		return ResponseEntity.ok().body(obj);
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<ProductOfferingDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> obj) {
		ProductOfferingDTO objDTO = new ProductOfferingDTO(service.partialUpdate(id, obj));
		return ResponseEntity.ok().body(objDTO);
	}
}
