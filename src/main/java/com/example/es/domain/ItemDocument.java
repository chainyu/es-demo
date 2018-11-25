package com.example.es.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "#{itemElasticSearchServiceImpl.indexName}", type = "#{itemElasticSearchServiceImpl.typeName}", createIndex = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ItemDocument implements Serializable{

    private static final long serialVersionUID = 288477511242262832L;
    /** 商品标识 */
    @Id
    private Long id;

    /** 店铺ID */
    private Long storeId;

    /** 渠道ID */
    private List<Long> channelId;

    /** 商品编码 */
    private String code;

    /** 商品名称 */
    private String title;

    /** 副标题 */
    private String subtitle;

    /** 吊牌价 */
    private Double listPrice;

    /** 销售价 */
    private Double salePrice;

    /** 款号 */
    private String style;

    /** 商品的url */
    private String customUrl;

    /** 上架时间 */
    private Date listTime;

    /** 定时上架时间 */
    private Date activeStartTime;

    /** 定时下架时间 */
    private Date activeEndTime;

    /** 最近更新时间 */
    private Date lastModified;

    /** 关键字 */
    private List<String> keywords;

    /** 商品属性 */
    private List<String> properties;

    /** 商品的分类 */
    private List<String> categories;

    /** 图片Alt */
    private String imageAlt;

    /** 商品搜索列表，高级属性切换对象,按照定义顺序来存储 */
    private List<String> images;

    /** 商品的角标 */
    private List<String> marks;

    /** 商品的品牌 */
    private String brand;

    //~~~~~~~~~~~~~~~~~商品统计数据start~~~~~~~~~~~~~~~~//
    /** 销售量 */
    private Long sales;

    /** 商品评分 */
    private Double rate;

    /** 商品评论量 */
    private Long review;

    /** 浏览量 */
    private Long view;

    /** 商品收藏量 */
    private Long favorite;

    /** 库存 */
    private Integer stock;
}
