package com.igg.boot.framework;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.test.context.junit4.SpringRunner;

import com.igg.boot.framework.autoconfigure.es.ElasticsearchDao;
import com.igg.boot.framework.autoconfigure.es.condition.AndCondition;
import com.igg.boot.framework.autoconfigure.es.condition.Condition;
import com.igg.boot.framework.es.dao.TestDao;
import com.igg.boot.framework.es.model.TestModel;
import com.igg.boot.framework.es.model.VpnTrial;



@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {
    @Autowired
    private ElasticsearchDao elastisearch;
    @Autowired
    private TestDao testDao;
    
    @Test
    public void condition() {
        AndCondition andCondition = Condition.and();
        AndCondition tmpCondition = Condition.and();
        tmpCondition.add(Condition.term("country", "unknown")).add(Condition.term("snId", "100039")).filter();
        andCondition.add(Condition.term("country", "CN")).add(tmpCondition).should();

        List<VpnTrial> list = elastisearch.query(andCondition, VpnTrial.class);
        
        Assert.assertEquals(7, list.size());
    }
    
    @Test
    public void ss() throws InterruptedException {
        TestModel testModel = new TestModel();
        testModel.setUserName("hello world3");
        testModel.setAddTime(20191117);
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(testModel);
        testDao.save(testModel);
        elastisearch.saveWithRouting(testModel);
       
        AndCondition andCondition = Condition.and("20191116");
        andCondition.add(Condition.term("add_time", 20191116)).filter();
        List<TestModel> list = elastisearch.query(andCondition, TestModel.class);
        System.out.println(list.toString());
   
    }
    
    
}
