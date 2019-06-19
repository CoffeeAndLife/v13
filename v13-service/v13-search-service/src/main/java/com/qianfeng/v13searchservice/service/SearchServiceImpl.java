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
import java.util.Map;

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
    public ResultBean synDataById(Long id) {
        //1.根据id获取数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        //2.构建document对象
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_images",product.getImages());
        document.setField("product_sale_point",product.getSalePoint());
        //3.
        try {
            solrClient.add(document);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404","数据添加到索引库失败！");
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
        //新增高亮的效果
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        //queryCondition.addHighlightField("product_sale_point");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //设置分页条件
        //TODO 实现分页
        /*queryCondition.setStart((pageIndex-1)*pageSize);
        queryCondition.setRows(pageSize);*/

        //2.执行查询 documentList
        try {
            QueryResponse response = solrClient.query(queryCondition);
            //获取到查询的结果集
            SolrDocumentList documents = response.getResults();
            //documents -> List<TProduct>
            List<TProduct> list = new ArrayList<>(documents.size());

            //获取高亮的信息
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //String--->id的值 101
            //map.put("101","101对应的高亮信息");
            //map.put("102","102对应的高亮信息");

            //Map<String, List<String>>>
            //String-->字段名（product_name)
            //map.put("product_name","product_name对应的高亮描述");

            for (SolrDocument document : documents) {
                //document -> product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setImages(document.getFieldValue("product_images").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                //
                //单独为高亮做配置 product_name
                //获取到记录的高亮信息
                Map<String, List<String>> idHigh = highlighting.get(document.getFieldValue("id"));
                //获取商品名称对应的高亮信息
                List<String> productNameHigh = idHigh.get("product_name");
                if(productNameHigh == null || productNameHigh.isEmpty()){
                    //没有对应的高亮信息
                    product.setName(document.getFieldValue("product_name").toString());
                }else{
                    //有高亮信息
                    product.setName(productNameHigh.get(0));
                }

                list.add(product);
            }
            return new ResultBean("200",list);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404","执行查询失败");
        }
    }
}
