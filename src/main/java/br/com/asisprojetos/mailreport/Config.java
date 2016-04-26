/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.mailreport;


import java.io.InputStream;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author acer k
 */
public class Config {
    
    static Logger logger = LoggerFactory.getLogger(Config.class);        
        
    @Getter
    @Setter
    private String outDirectory=null;
    
    @Getter
    @Setter
    private String htmlDirectory=null;
    
    @Getter
    @Setter
    private String hostname=null;

    @Getter
    @Setter
    private int port;

    @Getter
    @Setter
    private String from=null;
    
    @Getter
    @Setter
    private String fromName=null;

    @Getter
    @Setter
    private String authenticatorUser=null;

    @Getter
    @Setter
    private String authenticatorPassword=null;
    
    public Config(){
        
        try{
            Properties prop = new Properties();
            InputStream input = null;        

            input = getClass().getClassLoader().getResourceAsStream("config.properties");
            
            prop.load(input);
            setOutDirectory(prop.getProperty("outDirectory"));
            setHtmlDirectory(prop.getProperty("htmlDirectory"));
            setHostname(prop.getProperty("hostname"));
            setPort( Integer.parseInt(prop.getProperty("port")) );
            setFrom(prop.getProperty("from"));
            setFromName(prop.getProperty("fromName"));
            setAuthenticatorUser(prop.getProperty("authenticatorUser"));
            setAuthenticatorPassword(prop.getProperty("authenticatorPassword"));
            
        } catch (Exception ex){
            logger.error("Erro ao efetuar leitura de configurações. Message: {}", ex.getMessage());
        }
        
    }
    
}
