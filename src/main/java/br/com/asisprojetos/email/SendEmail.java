/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.email;

import br.com.asisprojetos.mailreport.Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author acer k
 */
public class SendEmail {
    
    static Logger logger = LoggerFactory.getLogger(SendEmail.class);
    private Config config=null;
    
    
    public SendEmail(Config config){
        this.config=config;
    }
    
    
    public void enviar(String toEmail[], String subject, String templateFile, List<String> fileNames, String mes, int codContrato){
        
        try{
            
            String htmlFileTemplate = loadHtmlFile(templateFile);

            for (String to : toEmail) {
            
                String htmlFile=htmlFileTemplate;
                
                
                // Create the email message
                HtmlEmail email = new HtmlEmail();
                email.setHostName(config.getHostname());
                email.setSmtpPort(config.getPort());
                email.setFrom( config.getFrom(), config.getFromName() ); // remetente
                email.setAuthenticator( new DefaultAuthenticator(config.getAuthenticatorUser(), config.getAuthenticatorPassword()) );
                email.setSSL(true);
                email.setSubject(subject); //Assunto

                email.addTo(to);//para


                logger.debug("Enviando Email para : [{}] ", to );

                int i=1;

                for (String fileName : fileNames ) {

                    String cid;

                    if (fileName.startsWith("diagnostico") ){
                        try{
                            cid = email.embed( new File( String.format("%s/%s", config.getOutDirectory(), fileName ) ) );
                            htmlFile = StringUtils.replace(htmlFile, "$codGraph24$", "<img src=\"cid:"+cid+"\">");
                        }catch(EmailException ex){
                            logger.error("Arquivo de diagnostico nao encontrado.");
                        }
                    }else if (fileName.startsWith("recorrencia")) {
                        try{
                            cid = email.embed( new File( String.format("%s/%s", config.getOutDirectory(), fileName ) ) );
                            htmlFile = StringUtils.replace(htmlFile, "$codGraph25$", "<img src=\"cid:"+cid+"\">");
                        }catch(EmailException ex){
                            logger.error("Arquivo de recorrencia nao encontrado.");
                        }
                    }else{
                        cid = email.embed( new File( String.format("%s/%s", config.getOutDirectory(), fileName ) ) );
                        htmlFile = StringUtils.replace(htmlFile, "$codGraph"+i+"$", "<img src=\"cid:"+cid+"\">");
                        i++;
                    }

                }

                //apaga $codGraph$ não usado do template
                for(int t=i; t<=25 ; t++){
                    htmlFile = StringUtils.replace(htmlFile, "$codGraph"+t+"$", " ");
                }

                htmlFile = StringUtils.replace(htmlFile, "$MES$", mes);
                htmlFile = StringUtils.replace(htmlFile, "$MAIL$", to);
                htmlFile = StringUtils.replace(htmlFile, "$CONTRATO$", Integer.toString(codContrato));
                

                email.setHtmlMsg(htmlFile);

                // set the alternative message
                email.setTextMsg("Your email client does not support HTML messages");

                // send the email
                email.send();          

            }
            
            logger.debug("Email enviado com sucesso......");
            
        }catch(FileNotFoundException ex){
            logger.error("Arquivo [{}/{}] nao encontrado para leitura.", config.getHtmlDirectory(), templateFile );
        }catch (Exception ex){
            logger.error("Erro ao Enviar email : {}", ex);
        }
        
    }
    
    
    public String loadHtmlFile(String templateFile) throws FileNotFoundException, IOException {
        
        FileInputStream fis = null;
        BufferedReader reader = null;
        StringBuilder htmlFile = new StringBuilder();
        
        logger.debug("Iniciando leitura do template [{}/{}] ", config.getHtmlDirectory(), templateFile );
        
        try {
            
            fis = new FileInputStream( String.format("%s/%s", config.getHtmlDirectory(), templateFile ) );
            reader = new BufferedReader(new InputStreamReader(fis));
          
            String line = null;
            while( (line = reader.readLine()) != null){
                htmlFile.append(line);
                htmlFile.append("\n");
            }
            

        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
                logger.error("Erro ao efetuar fechamento  do arquivo. Message [{}]", ex);
            }
        }
        
        logger.debug("Leitura do template [{}/{}] efetuada com sucesso.", config.getHtmlDirectory(), templateFile );
        
        return htmlFile.toString();
        
    }
    
    
}
