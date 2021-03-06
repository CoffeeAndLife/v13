package com.qianfeng.v13searchservice;

import com.qianfeng.v13.api.ISearchService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TProduct;
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
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13SearchServiceApplicationTests {

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private ISearchService searchService;


	@Test
	public void synDataByIdTest(){
		ResultBean resultBean = searchService.synDataById(9L);
		System.out.println(resultBean.getStatusCode());
	}

	//测试
	//正常路径：
	//非正常路径：
	@Test
	public void queryByKeywordsTest(){
		ResultBean<List<TProduct>> resultBean = searchService.queryByKeywords("");
		List<TProduct> products = resultBean.getData();
		for (TProduct product : products) {
			System.out.println(product.getId()+"->"+product.getName());
		}
	}

	@Test
	public void synAllDataTest(){
		ResultBean resultBean = searchService.synAllData();
		System.out.println(resultBean.getStatusCode());
	}

	@Test
	public void addOrUpdateTest() throws IOException, SolrServerException {
		//1.以document为基本单位
		SolrInputDocument document = new SolrInputDocument();
		// id不存在就是新增，id存在就是覆盖（更新）
		document.setField("id",666);
		document.setField("product_name","湖人总冠军");
		document.setField("product_price",888888);
		document.setField("product_images","123");
		document.setField("product_sale_point","卡哇伊");
		//2.保存document到solr索引库中
		//solrClient.add(document);
		solrClient.add("collection2",document);
		//3.提交
		//solrClient.commit();
		solrClient.commit("collection2");
		System.out.println("保存成功！");
	}

	@Test
	public void queryTest() throws IOException, SolrServerException {
		//1.组装查询条件
		SolrQuery queryCondition = new SolrQuery();
		//分词，匹配
		queryCondition.setQuery("product_name:湖人总冠军");

		//2.执行查询
		//QueryResponse response = solrClient.query(queryCondition);

		QueryResponse response = solrClient.query("collection2",queryCondition);
		//3.
		SolrDocumentList solrDocuments = response.getResults();

		for (SolrDocument document : solrDocuments) {
			System.out.println("id:"+document.get("id")+",name:"+document.get("product_name"));
		}
	}

	@Test
	public void delByIdTest() throws IOException, SolrServerException {
		solrClient.deleteById("2");
		solrClient.commit();
	}

	@Test
	public void delByConditionTest() throws IOException, SolrServerException {
		//都会分词之后，再匹配
		solrClient.deleteByQuery("product_name:华为旗舰大龙虾");
		solrClient.commit();
	}

	@Test
	public void delAllTest() throws IOException, SolrServerException {
		//都会分词之后，再匹配
		solrClient.deleteByQuery("*:*");
		solrClient.commit();
	}

}
