package com.example.es.service;

import com.example.es.domain.ItemDocument;
public interface ItemElasticSearchService {

	ItemDocument search(ItemDocument temDocument);

	ItemDocument findById(Long id);

	boolean save(ItemDocument doc);

	boolean update(ItemDocument doc);

	boolean deleteById(Long id);
}
