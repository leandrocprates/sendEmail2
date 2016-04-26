/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.mailreport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.asisprojetos.DAO.TBRelatorioEmailDAO;
import br.com.asisprojetos.email.SendEmail;
import br.com.asisprojetos.enums.Produto;
import br.com.asisprojetos.graph.BarChartDemo;
import br.com.asisprojetos.model.TBRelatorioEmail;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 *
 * @author acer k
 * 
 * Start inicial da aplicação : 
 * 
 * Passar parametros de data de Inicio de Processamento e 
 *                      data de Fim de Processamento 
 * 
 * Formato :  java -jar Aplicacao.jar 2015-01-01 2015-02-01
 * 
 * String dataIniProc="2015-01-01 00:00:00";
 * String dataFimProc="2015-02-01 23:59:59";  
 * 
 */
public class Main {
    
    static Logger logger = LoggerFactory.getLogger(Main.class);
    
    
    public static void main(String args[]){
        
        logger.debug("Testando debug");
        
        if ( args.length < 2 ){
            logger.error("Erro : Numero de parametros errados.");
            System.exit(1);
        }
        
        String dataIniProc=String.format("%s 00:00:00", args[0] );
        String dataFimProc=String.format("%s 23:59:59", args[1] );
        
        
        logger.debug("Data Inicial: {} , Data Final: {}", dataIniProc, dataFimProc );
        
        
        String mes = String.format("%s/%s", args[0].substring(5,7), args[0].substring(0,4) );
        
        
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Datasource.xml");

        
        TBRelatorioEmailDAO tbRelatorioEmailDAO = (TBRelatorioEmailDAO) context.getBean("TBRelatorioEmailDAO");        
        
        List<TBRelatorioEmail> listaRelatorioEmail =  tbRelatorioEmailDAO.getAll();
        
        for ( TBRelatorioEmail tbRelatorioEmail : listaRelatorioEmail ){
            
            logger.debug(" CodContrato: {}, CodProduto: {}" ,tbRelatorioEmail.getCodContrato(),
                                                             tbRelatorioEmail.getCodProduto() ); 
            
            
            List<String> listaEmails = tbRelatorioEmailDAO.getListaEmails( tbRelatorioEmail.getCodContrato() );
            
            
            if ( !listaEmails.isEmpty() ){
                
                
                logger.debug("Lista de Emails obtida : [{}] ", listaEmails );

                //List<String> listaCodProduto = Arrays.asList( StringUtils.split(tbRelatorioEmail.getCodProduto(), ';') ) ;
                List<String> listaCodProduto = new ArrayList<String>() ;
                listaCodProduto.add("1");//Sped Fiscal


                logger.debug("Gerando Relatorio Geral de Consumo por Produto...");


                List<String> fileNames=new ArrayList<String>();
                String fileName;

                BarChartDemo barChartDemo = (BarChartDemo) context.getBean("BarChartDemo");
                //fileName = barChartDemo.generateBarChartGraph(tbRelatorioEmail.getCodContrato());
                //fileNames.add(fileName);
                fileNames = barChartDemo.generateBarChartGraph2(tbRelatorioEmail.getCodContrato());



                String templateFile=null;

                for ( String codProduto : listaCodProduto ){


                    if ( codProduto.equals( Produto.SPED_FISCAL.getCodProduto() ) ){

                        logger.debug("Produto Codigo : {} " , codProduto );
                        templateFile="index6.html";

                        //grafico de diagnostico
                        fileName = barChartDemo.generateDiagnosticGraph(tbRelatorioEmail.getCodContrato(),dataIniProc,dataFimProc);
                        fileNames.add(fileName);

                        //grafico de auditoria recorrente
                        fileName = barChartDemo.generateRecurrentGraph(tbRelatorioEmail.getCodContrato(),dataIniProc,dataFimProc);
                        fileNames.add(fileName);


                    } else {
                        logger.debug("Produto Codigo : {} não aceito para gerar grafico " , codProduto );
                    }


                    logger.debug("Enviando Email.............Produto Codigo : {}", codProduto );

                    SendEmail sendEmail = (SendEmail) context.getBean("SendEmail"); 
                    sendEmail.enviar(listaEmails.toArray(new String[listaEmails.size()]), 
                            "Relatório Mensal de Riscos Fiscais", templateFile, fileNames, mes, tbRelatorioEmail.getCodContrato() );
                    
                    //sendEmail.enviar( new String[]{"leandro.prates@asisprojetos.com.br","leandro.prates@gmail.com"}  , 
                    //        "Relatório Mensal de Riscos Fiscais", templateFile, fileNames, mes, tbRelatorioEmail.getCodContrato() );

                }

                //Remover todos os arquivos png gerado para o cliente

                Config config = (Config) context.getBean("Config");        

                for(String f: fileNames){

                    try{
                        File file = new File( String.format("%s/%s", config.getOutDirectory(), f ) );
                        if ( file.delete() ) {
                            logger.debug("Arquivo: [{}/{}] deletado com sucesso.",config.getOutDirectory(),f );
                        }else{
                            logger.error("Erro ao deletar o arquivo: [{}/{}]",config.getOutDirectory(),f );
                        }

                    }catch (Exception ex){
                        logger.error("Erro ao deletar o arquivo: [{}/{}] . Message {}",config.getOutDirectory(),f, ex );
                    }

                }
            
            }

            
        }
        
    }
    
}
