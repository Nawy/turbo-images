package com.turbo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbo.config.elastic.mapping.DefaultMappingPropertyBuilder;
import com.turbo.config.elastic.mapping.MappingBuilder;
import com.turbo.config.elastic.mapping.MappingPropertyType;
import com.turbo.model.search.content.ImageSearchEntity;
import org.apache.http.util.Asserts;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by ermolaev on 5/15/17.
 */
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchConfig {
    private static final int MAX_SIZE_OF_SELECT_LIST = 300;

    private String[] hosts;
    private String clusterName;

    private String searchPostIndexName;
    private String searchPostTypeName;

    private String searchImageIndexName;
    private String searchImageTypeName;

    private String searchUserIndexName;
    private String searchUserTypeName;

    private String statPostsIndexName;
    private String statPostsTypeName;

    private int maxSizePostsPerPage;
    private int maxSizeUsersPerPage;

    private TransportClient elasticClient;
    private ObjectMapper objectMapper;

    @Autowired
    public ElasticsearchConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        Asserts.check(maxSizePostsPerPage != 0, "Max size post per page cannot be 0");
        Asserts.check(maxSizeUsersPerPage != 0, "Max size users per page cannot be 0");
        Asserts.check(maxSizePostsPerPage < MAX_SIZE_OF_SELECT_LIST, "Max size posts per page cannot be more than " + MAX_SIZE_OF_SELECT_LIST);
        Asserts.check(maxSizeUsersPerPage < MAX_SIZE_OF_SELECT_LIST, "Max size users per page cannot be more than " + MAX_SIZE_OF_SELECT_LIST);

        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        try {
            InetSocketTransportAddress[] addresses = getAddresses();
            elasticClient = new PreBuiltTransportClient(settings).addTransportAddresses(addresses);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Can't create elastic transport client!", e);
        }

        createContentIndicesIfNonExist();
        createStatisticIndicesIfNonExist();
    }

    public TransportClient getElasticClient() {
        return elasticClient;
    }

    private InetSocketTransportAddress[] getAddresses() throws UnknownHostException {
        Objects.requireNonNull(hosts, "Elasticsearch hosts in properties cannot be empty");
        InetSocketTransportAddress[] addresses = new InetSocketTransportAddress[hosts.length];
        for(int i = 0; i < hosts.length; i++) {
            String[] addr = hosts[i].split(":");
            addresses[i] = new InetSocketTransportAddress(
                    InetAddress.getByName(addr[0]),
                    Integer.valueOf(addr[1])
            );
        }

        return addresses;
    }

    private void createContentIndicesIfNonExist() {
        Objects.requireNonNull(elasticClient);

        IndicesAdminClient indices = elasticClient.admin().indices();
        final boolean isPostExists = indices.exists(new IndicesExistsRequest(searchPostIndexName)).actionGet().isExists();
        final boolean isUserExists = indices.exists(new IndicesExistsRequest(searchUserIndexName)).actionGet().isExists();
        final boolean isImageExists = indices.exists(new IndicesExistsRequest(searchImageIndexName)).actionGet().isExists();

        if(!isPostExists) {
            indices.create(new CreateIndexRequest(searchPostIndexName)).actionGet();
        }
        if(!isUserExists) {
            indices.create(new CreateIndexRequest(searchUserIndexName)).actionGet();
        }
        if(!isImageExists) {
            indices.create(new CreateIndexRequest(searchImageIndexName)).actionGet();
        }

        try {
            indices.preparePutMapping(searchImageIndexName)
                    .setType(searchImageTypeName)
                    .setSource(
                            objectMapper.writeValueAsBytes(
                                    new MappingBuilder()
                                            .addProperty(
                                                    "id",
                                                    new DefaultMappingPropertyBuilder()
                                                            .setType(MappingPropertyType.LONG)
                                                            .createMappingProperty()
                                            )
                                            .addProperty(
                                                    "user_id",
                                                    new DefaultMappingPropertyBuilder()
                                                            .setType(MappingPropertyType.LONG)
                                                            .createMappingProperty()
                                            )
                                            .addProperty(
                                                    "creation_date",
                                                    new DefaultMappingPropertyBuilder()
                                                            .setType(MappingPropertyType.DATE)
                                                            .setFormat(ImageSearchEntity.CREATION_DATE_PATTERN)
                                                            .setDocValues(false)
                                                            .createMappingProperty()
                                            ).createMapping()
                            ),
                            XContentType.JSON
                    ).execute().actionGet();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void createStatisticIndicesIfNonExist() {
        Objects.requireNonNull(elasticClient);

        IndicesAdminClient indices = elasticClient.admin().indices();
        final boolean isStatPostExists = indices.exists(new IndicesExistsRequest(statPostsIndexName)).actionGet().isExists();

        if(!isStatPostExists) {
            indices.create(new CreateIndexRequest(statPostsIndexName)).actionGet();
        }
    }


    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getSearchPostIndexName() {
        return searchPostIndexName;
    }

    public void setSearchPostIndexName(String searchPostIndexName) {
        this.searchPostIndexName = searchPostIndexName;
    }

    public String getSearchPostTypeName() {
        return searchPostTypeName;
    }

    public void setSearchPostTypeName(String searchPostTypeName) {
        this.searchPostTypeName = searchPostTypeName;
    }

    public String getSearchUserIndexName() {
        return searchUserIndexName;
    }

    public void setSearchUserIndexName(String searchUserIndexName) {
        this.searchUserIndexName = searchUserIndexName;
    }

    public String getSearchUserTypeName() {
        return searchUserTypeName;
    }

    public void setSearchUserTypeName(String searchUserTypeName) {
        this.searchUserTypeName = searchUserTypeName;
    }

    public int getMaxSizePostsPerPage() {
        return maxSizePostsPerPage;
    }

    public void setMaxSizePostsPerPage(int maxSizePostsPerPage) {
        this.maxSizePostsPerPage = maxSizePostsPerPage;
    }

    public int getMaxSizeUsersPerPage() {
        return maxSizeUsersPerPage;
    }

    public void setMaxSizeUsersPerPage(int maxSizeUsersPerPage) {
        this.maxSizeUsersPerPage = maxSizeUsersPerPage;
    }

    public void setElasticClient(TransportClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    public String getStatPostsIndexName() {
        return statPostsIndexName;
    }

    public void setStatPostsIndexName(String statPostsIndexName) {
        this.statPostsIndexName = statPostsIndexName;
    }

    public String getStatPostsTypeName() {
        return statPostsTypeName;
    }

    public void setStatPostsTypeName(String statPostsTypeName) {
        this.statPostsTypeName = statPostsTypeName;
    }

    public String getSearchImageIndexName() {
        return searchImageIndexName;
    }

    public void setSearchImageIndexName(String searchImageIndexName) {
        this.searchImageIndexName = searchImageIndexName;
    }

    public String getSearchImageTypeName() {
        return searchImageTypeName;
    }

    public void setSearchImageTypeName(String searchImageTypeName) {
        this.searchImageTypeName = searchImageTypeName;
    }
}
