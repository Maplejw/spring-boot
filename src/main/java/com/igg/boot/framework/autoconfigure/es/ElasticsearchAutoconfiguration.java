package com.igg.boot.framework.autoconfigure.es;

import java.util.List;
import java.util.Properties;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
@EnableConfigurationProperties(ElastisearchProperties.class)
@ConditionalOnClass(Client.class)
@ConditionalOnProperty(prefix="igg.es",name="cluster-nodes",matchIfMissing = false)
public class ElasticsearchAutoconfiguration {
    private final ElastisearchProperties properties;
    
    public ElasticsearchAutoconfiguration(ElastisearchProperties properties) {
        this.properties = properties;
    }
    
    
    @Bean
    public TransportClient elasticsearchClient() throws Exception {
        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        factory.setClusterNodes(this.properties.getClusterNodes());
        factory.setProperties(createProperties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    
    @Bean
    public IggElasticsearchTemplate elasticsearchTemplate(Client client, ElasticsearchConverter converter) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            if(properties.isLowerCaseWithUnderscores()) {
                gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
            }
            Gson gson = gsonBuilder.create();
            return new IggElasticsearchTemplate(client, converter,gson);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    @Bean
    public ElasticsearchDao elasticsearchDao(IggElasticsearchTemplate iggElasticsearchTemplate) {
        return new ElasticsearchDao(iggElasticsearchTemplate);
    }

    public static class IggElasticsearchTemplate extends ElasticsearchTemplate {
        private Gson gson;
        
        public IggElasticsearchTemplate(Client client, ElasticsearchConverter elasticsearchConverter,Gson gson) {
            super(client, elasticsearchConverter, new GsonEntityMapper(gson));
            this.gson = gson;
        }

        public IggElasticsearchTemplate(Client client, ElasticsearchConverter elasticsearchConverter, EntityMapper entityMapper) {
            super(client, elasticsearchConverter, entityMapper);
        }
        
        public <T> void bulkIndexWithRouting(List<T> model) {
            Client client = getClient();
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            model.forEach(vo -> {
                Document document = ElastisearchParse.getDoucment(vo.getClass());
                String routing = ElastisearchParse.getRouting(vo);
                IndexRequestBuilder indexRequestBuilder =  client.prepareIndex(document.indexName(), document.type());
                indexRequestBuilder.setRouting(routing);
                indexRequestBuilder.setSource(gson.toJson(vo));
                bulkRequest.add(indexRequestBuilder);
            });
            
            bulkRequest.execute().actionGet();
        }
        
    }

    private static class GsonEntityMapper implements EntityMapper {
        private Gson gson;

        public GsonEntityMapper(Gson gson) {
            this.gson = gson;
        }

        @Override
        public String mapToString(Object object) {
            return gson.toJson(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) {
            return gson.fromJson(source, clazz);
        }

    }
    
    private Properties createProperties() {
        Properties properties = new Properties();
        properties.put("cluster.name", this.properties.getClusterName());
        properties.putAll(this.properties.getProperties());
        return properties;
    }

}
