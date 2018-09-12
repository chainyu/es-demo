package com.example.es.service;

import com.alibaba.fastjson.JSON;
import com.example.es.dao.ItemSearchRepository;
import com.example.es.domain.ItemDocument;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class ItemElasticSearchServiceImpl implements ItemElasticSearchService {

    private Logger LOGGER = LoggerFactory.getLogger(ItemElasticSearchServiceImpl.class);

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private ItemSearchRepository itemSearchRepository;

    /**
     * 索引名字
     */
    @Value("${es.index.name}")
    public String indexName;

    /**
     * 类型名字
     */
    @Value("${es.type.name")
    public String typeName;

    @Override
    public boolean save(ItemDocument doc) {

        ItemDocument result = itemSearchRepository.save(doc);
        return result != null;
    }

    @Override
    public boolean update(ItemDocument doc) {

        UpdateQuery query = new UpdateQuery();
        query.setClazz(doc.getClass());
        elasticsearchTemplate.update(query);
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("ES is deleting index:{}", id);
        itemSearchRepository.deleteById(id);
        return true;
    }

    @Override
    public ItemDocument search(ItemDocument itemSearchParamCommand) {
        LOGGER.debug("searchForm:[{}],indexName:[{}],typeName[{}]", JSON.toJSONString(itemSearchParamCommand), "saas_test_item", typeName);
        //SearchQuery searchQuery = simpleQueryConvert.convert(itemSearchParamCommand, indexName, typeName);
        SearchQuery searchQuery = new NativeSearchQuery(QueryBuilders.matchAllQuery());

        LOGGER.debug("searchQuery:[{}]", JSON.toJSONString(searchQuery));
        LOGGER.debug("query:{}", searchQuery.getQuery());
        LOGGER.debug("filterQuery:{}", searchQuery.getFilter());
        LOGGER.debug("aggregations:{}", searchQuery.getAggregations());

        SearchResponse response = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<SearchResponse>() {
            @Override
            public SearchResponse extract(SearchResponse response) {
                return response;
            }
        });

        //ItemSearchResultCommand itemSearchResultCommand = searchResultConvert.convert(response, itemSearchParamCommand);
        return null;
    }

    @Override
    public ItemDocument findById(Long id) {
        Optional<ItemDocument> document = itemSearchRepository.findById(id);
        return document.orElse(new ItemDocument());
    }


}
