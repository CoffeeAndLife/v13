package com.qianfeng.v13searchservice;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13SearchServiceApplicationTests {

	@Autowired
	private SolrClient solrClient;

	@Test
	public void addTest() throws IOException, SolrServerException {
		//1.以document为基本单位
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id",2);
		document.setField("product_name","华为旗舰大龙虾");
		document.setField("product_price",999);
		document.setField("product_images","123");
		document.setField("product_sale_point","买三斤送一斤");
		//2.保存document到solr索引库中
		solrClient.add(document);
		//3.提交
		solrClient.commit();
		System.out.println("保存成功！");
	}

	@Test
	public void queryTest() throws IOException, SolrServerException {
		//1.组装查询条件
		SolrQuery queryCondition = new SolrQuery();
		//分词，匹配
		queryCondition.setQuery("product_name:华为旗舰大龙虾");

		//2.执行查询
		QueryResponse response = solrClient.query(queryCondition);
		//3.
		SolrDocumentList solrDocuments = response.getResults();

		for (SolrDocument document : solrDocuments) {
			System.out.println("id:"+document.get("id")+",name:"+document.get("product_name"));
		}
	}

	@Test
	public void contextLoads() {
	}

}
