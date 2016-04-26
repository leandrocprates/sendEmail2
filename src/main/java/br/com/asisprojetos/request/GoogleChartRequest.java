/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.request;

import br.com.asisprojetos.mailreport.Config;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.http.HttpEntity;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author acer k
 */
public class GoogleChartRequest {
    
    
    static Logger logger = LoggerFactory.getLogger(GoogleChartRequest.class);
    private Config config;
    
    public GoogleChartRequest(Config config){
        this.config=config;
    }

    
    public void makeRequest(String url, String fileName){
        
        try{
            
            FileOutputStream fos = null;
            
            
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            logger.debug("Executando Request para Gerar Grafico no Servidor do Google Chart. URL : [{}]", url);
            
            
            request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);

            
            logger.debug("Codigo de Resposta: {}" , response.getStatusLine().getStatusCode() );
            
            
            fos = new FileOutputStream(new File( String.format("%s/%s", config.getOutDirectory(), fileName ) ));
            
            HttpEntity httpEntity = response.getEntity();
            
            httpEntity.writeTo(fos);
            
            fos.close();
            
            
            logger.debug("Arquivo gerado no diretorio: {}/{} ", config.getOutDirectory(), fileName );
            

        } catch (Exception ex){
            logger.error("Erro ao fazer Request para Servidor do Google Chart: {}", ex);
        }
        
    }
    
}
