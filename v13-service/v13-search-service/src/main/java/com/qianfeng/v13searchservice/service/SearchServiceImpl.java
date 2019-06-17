package com.qianfeng.v13searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.v13.api.ISearchService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TProduct;
import com.qianfeng.v13.mapper.TProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangguizhao
 */
@Service
public class SearchServiceImpl implements ISearchService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean synAllData() {
        //1.获取数据库的数据
        List<TProduct> list = productMapper.list();
        //2.将数据导入到索引库中
        for (TProduct product : list) {
            //3.product -> solrInputDocument
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_price",product.getPrice());
            document.setField("product_images",product.getImages());
            document.setField("product_sale_point",product.getSalePoint());
            //
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                return new ResultBean("404","数据添加到索引库失败！");
            }
        }
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404","数据提交到索引库失败！");
        }
        return new ResultBean("200","数据同步到索引库成功！");
    }


    @Override
    public ResultBean queryByKeywords(String keywords) {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        //做判断
        if(StringUtils.isAnyEmpty(keywords)){
            queryCondition.setQuery("*:*");
        }else{
            queryCondition.setQuery("product_keywords:"+keywords);
        }
        //2.执行查询 documentList
        try {
            QueryResponse response = solrClient.query(queryCondition);
            //获取到查询的结果集
            SolrDocumentList documents = response.getResults();
            //documents -> List<TProduct>
            List<TProduct> list = new ArrayList<>(documents.size());
            //
            for (SolrDocument document : documents) {
                //document -> product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                product.setName(document.getFieldValue("product_name").toString());
                product.setImages(document.getFieldValue("product_images").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                //
                list.add(product);
            }
            return new ResultBean("200",list);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404","执行查询失败");
        }
    }
}
