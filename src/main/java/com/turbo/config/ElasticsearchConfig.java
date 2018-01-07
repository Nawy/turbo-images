package com.turbo.config;

import com.turbo.model.search.content.ImageSearchEntity;
import com.turbo.model.search.content.PostSearchEntity;
import com.turbo.model.search.field.ImageFieldNames;
import com.turbo.model.search.field.PostFieldNames;
import com.turbo.model.search.field.UserFieldNames;
import lombok.Data;
import org.apache.http.util.Asserts;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Data
public class ElasticsearchConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchConfig.class);
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

        //FIXME IS This required? Is This correct?
        if (!isPostExists) {
            createPostMapping(indices);
        }
        if(!isImageExists) {
            createImageMapping(indices);
        }
        if(!isUserExists) {
            createUserMapping(indices);
        }
    }

    private void createPostMapping(IndicesAdminClient indices) {
        try {
            XContentBuilder mapping = jsonBuilder()
                    .startObject()
                    .startObject(searchPostTypeName)
                    .startObject("properties")

                    .startObject(PostFieldNames.ID)
                    .field("type", "long")
                    .endObject()

                    .startObject(PostFieldNames.NAME)
                    .field("type", "text")
                    .endObject()

                    .startObject(PostFieldNames.DESCRIPTIONS)
                    .field("type", "text")
                    .endObject()

                    .startObject(PostFieldNames.DEVICE_TYPE)
                    .field("type", "text")
                    .endObject()

                    //FIXME is this field required?
                    /*.startObject(PostFieldNames.IMAGE_IDS)
                    .field("type","array")
                    .endObject()*/

                    /*.startObject(PostFieldNames.TAGS)
                    .field("type","array")
                    .endObject()*/

                    .startObject(PostFieldNames.USER_ID)
                    .field("type", "long")
                    .endObject()

                    .startObject(PostFieldNames.UPS)
                    .field("type", "long")
                    .endObject()

                    .startObject(PostFieldNames.DOWNS)
                    .field("type", "long")
                    .endObject()

                    .startObject(PostFieldNames.RATING)
                    .field("type", "long")
                    .endObject()

                    .startObject(PostFieldNames.VIEWS)
                    .field("type", "long")
                    .endObject()

                    .startObject(PostFieldNames.VISIBLE)
                    .field("type", "boolean")
                    .endObject()

                    .startObject(PostFieldNames.CREATION_DATE)
                    .field("type", "date")
                    .field("format", PostSearchEntity.CREATION_DATE_PATTERN)
                    .field("doc_values", true)
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();

            indices.preparePutMapping(searchPostIndexName)
                    .setType(searchPostTypeName)
                    .setSource(mapping)
                    .execute().actionGet();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createUserMapping(IndicesAdminClient indices) {
        try {
            XContentBuilder mapping = jsonBuilder()
                    .startObject()
                        .startObject(searchUserTypeName)
                            .startObject("properties")
                                .startObject(UserFieldNames.ID)
                                    .field("type", "long")
                                .endObject()
                                .startObject(UserFieldNames.NAME)
                                    .field("type", "text")
                                .endObject()
                                .startObject(UserFieldNames.EMAIL)
                                    .field("type", "text")
                                .endObject()
                                .startObject(UserFieldNames.RATING)
                                    .field("type", "long")
                                .endObject()
                                .startObject(UserFieldNames.CREATION_DATE)
                                    .field("type", "date")
                                    .field("format", ImageSearchEntity.CREATION_DATE_PATTERN)
                                    .field("doc_values", true)
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();

            indices.preparePutMapping(searchUserIndexName)
                    .setType(searchUserTypeName)
                    .setSource(mapping)
                    .execute().actionGet();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createImageMapping(IndicesAdminClient indices) {
        try {
            XContentBuilder mapping = jsonBuilder()
                    .startObject()
                        .startObject(searchImageTypeName)
                            .startObject("properties")
                                .startObject(ImageFieldNames.ID)
                                    .field("type", "long")
                                .endObject()
                                .startObject(ImageFieldNames.NAME)
                                    .field("type","text")
                                .endObject()
                                .startObject(ImageFieldNames.DESCRIPTION)
                                    .field("type","text")
                                .endObject()
                                .startObject(ImageFieldNames.USER_ID)
                                    .field("type","long")
                                .endObject()
                                .startObject(ImageFieldNames.CREATION_DATE)
                                    .field("type","date")
                                    .field("format", ImageSearchEntity.CREATION_DATE_PATTERN)
                                    .field("doc_values", true)
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();

            indices.preparePutMapping(searchImageIndexName)
                    .setType(searchImageTypeName)
                    .setSource(mapping)
                    .execute().actionGet();

        } catch (Exception e) {
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
}
