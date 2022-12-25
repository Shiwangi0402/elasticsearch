package com.example.elasticsearch.configuration;

import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.HttpHeaders;

import com.example.elasticsearch.connection.ESConnection;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.elasticsearch.repository")
@ComponentScan(basePackages = {"com.example.elasticsearch"})
public class Config extends AbstractElasticsearchConfiguration{

	@Value("${elasticsearch.url}")
	public String elasticsearchUrl;
	
	@Autowired
	private final ESConnection esConnectionDetails;
	
	public Config(ESConnection esConnectionDetails) {
		this.esConnectionDetails = esConnectionDetails;
	}
	@Bean
	@Override
	public RestHighLevelClient elasticsearchClient() {
		
		HttpHeaders compatibilityHeaders = new HttpHeaders();
        compatibilityHeaders.add("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
        compatibilityHeaders.add("Content-Type", "application/vnd.elasticsearch+json;"
                + "compatible-with=7");
        HttpClientConfigCallback httpClientConfigurer = new HttpClientConfigCallbackImpl();
		final ClientConfiguration config = ClientConfiguration.builder()
				.connectedTo(elasticsearchUrl)
				.usingSsl().withHttpClientConfigurer(httpClientConfigurer)
                .withBasicAuth(esConnectionDetails.getUsername(), esConnectionDetails.getPassword())
                .withDefaultHeaders(compatibilityHeaders)
                .build();
		return RestClients.create(config).rest();
	}

}

class HttpClientConfigCallbackImpl implements HttpClientConfigCallback {

	@Override
	public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
		String truststoreLocation = "C:\\Users\\shiva\\Desktop\\Shiwangi\\Elastic\\elasticsearch-8.5.3\\config\\certs\\truststore.p12";
		File trustStoreLocationFile = new File(truststoreLocation);
		try {
			SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(trustStoreLocationFile, "password".toCharArray());
			SSLContext sslContext = sslContextBuilder.build();
			httpClientBuilder.setSSLContext(sslContext);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return httpClientBuilder;
	}
	
}