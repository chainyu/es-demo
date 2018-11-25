package com.example.es.service;

import com.alibaba.fastjson.JSON;
import com.example.es.dao.ItemSearchRepository;
import com.example.es.domain.ItemDocument;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class ItemElasticSearchServiceImpl implements ItemElasticSearchService {

    private Logger LOGGER = LoggerFactory.getLogger(ItemElasticSearchServiceImpl.class);

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

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
    public String save(ItemDocument doc) {

        //ItemDocument result = itemSearchRepository.save(doc);
        //return result != null;
        IndexQuery indexQuery = new IndexQueryBuilder().withIndexName(indexName).withType(typeName).withObject(doc).build();
        String index = elasticsearchTemplate.index(indexQuery);
        return index;
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
        //itemSearchRepository.deleteById(id);
        elasticsearchTemplate.delete(indexName, typeName, String.valueOf(id));
        return true;
    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public List<ItemDocument> search(ItemDocument itemSearchParamCommand) {
        LOGGER.debug("searchForm:[{}],indexName:[{}],typeName[{}]", JSON.toJSONString(itemSearchParamCommand), indexName, typeName);
        //SearchQuery searchQuery = simpleQueryConvert.convert(itemSearchParamCommand, indexName, typeName);
        SearchQuery searchQuery = new NativeSearchQuery(QueryBuilders.matchAllQuery());

        LOGGER.debug("searchQuery:[{}]", JSON.toJSONString(searchQuery));
        LOGGER.debug("query:{}", searchQuery.getQuery());
        LOGGER.debug("filterQuery:{}", searchQuery.getFilter());
        LOGGER.debug("aggregations:{}", searchQuery.getAggregations());

        List<ItemDocument> itemDocuments = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        return itemDocuments;
    }

    @Override
    public ItemDocument findById(Long id) {
        //Optional<ItemDocument> document = itemSearchRepository.findById(id);
        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds(String.valueOf(id));
        SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<SearchResponse>() {
            @Override
            public SearchResponse extract(SearchResponse searchResponse) {
                return searchResponse;
            }
        });

        List<SearchHit> searchHits = asList(searchResponse.getHits().getHits());
        if(searchHits != null && searchHits.size()>0){
            List<ItemDocument> collect = searchHits.stream().map(searchHit -> {
                String sourceStr = searchHit.getSourceAsString();
                return JSON.parseObject(sourceStr, ItemDocument.class);
            }).collect(Collectors.toList());

            return collect.get(0);
        }



        return null;
    }


}
