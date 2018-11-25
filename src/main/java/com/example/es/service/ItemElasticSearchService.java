package com.example.es.service;

import com.example.es.domain.ItemDocument;

import java.util.List;

public interface ItemElasticSearchService {

	List<ItemDocument> search(ItemDocument temDocument);

	ItemDocument findById(Long id);

	String save(ItemDocument doc);

	boolean update(ItemDocument doc);

	boolean deleteById(Long id);

	boolean init();
}
