package com.example.es;

import com.example.es.domain.ItemDocument;
import com.example.es.service.ItemElasticSearchService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsDemoApplicationTests {

	@Autowired
	private ItemElasticSearchService itemElasticSearchService;

	@Test
	public void testSaveItemDoc() {
		Long id = 6L;
		ItemDocument itemDocument = new ItemDocument();
		itemDocument.setId(id);
		itemDocument.setTitle("一双帅掉渣的滑板鞋" + id);
		boolean isSaved = itemElasticSearchService.save(itemDocument);
		Assert.assertTrue("保存成功", isSaved);
		testFindById(id);
	}

	@Test
	public void testFindById() {
		testFindById(2L);
	}

	private void testFindById(Long id){
		ItemDocument itemDocument = itemElasticSearchService.findById(id);
		Assert.assertNotNull(itemDocument);
		System.out.println(new Gson().toJson(itemDocument));
	}

}
