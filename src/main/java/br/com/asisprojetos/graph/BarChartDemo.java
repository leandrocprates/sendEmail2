

package br.com.asisprojetos.graph;


import br.com.asisprojetos.DAO.TBContratoDAO;
import br.com.asisprojetos.DAO.TBRelatorioConsumoDAO;
import br.com.asisprojetos.DAO.TBRelatorioDiagnosticoDAO;
import br.com.asisprojetos.mailreport.Config;
import br.com.asisprojetos.model.TBContrato;
import br.com.asisprojetos.model.TBRelatorioConsumo;
import br.com.asisprojetos.request.GoogleChartRequest;
import gui.ava.html.image.generator.HtmlImageGenerator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Documentacao de referencia : https://developers.google.com/chart/image/docs/gallery/pie_charts
 **/

public class BarChartDemo {

    static Logger logger = LoggerFactory.getLogger(BarChartDemo.class);
    
    private TBRelatorioConsumoDAO tbRelatorioConsumoDAO;
    private TBContratoDAO tbContratoDAO;
    private TBRelatorioDiagnosticoDAO tbRelatorioDiagnosticoDAO;
    private Config config;
    private GoogleChartRequest googleChartRequest;
    
    
    private final String URL_GOOGLE_CHART="http://chart.apis.google.com/chart?";
    
    public BarChartDemo( Config config, TBRelatorioConsumoDAO tbRelatorioConsumoDAO, 
                         TBContratoDAO tbContratoDAO, TBRelatorioDiagnosticoDAO tbRelatorioDiagnosticoDAO,
                         GoogleChartRequest googleChartRequest ) {
        
        this.config=config;
        this.tbRelatorioConsumoDAO=tbRelatorioConsumoDAO;
        this.tbContratoDAO=tbContratoDAO;
        this.tbRelatorioDiagnosticoDAO=tbRelatorioDiagnosticoDAO;
        this.googleChartRequest=googleChartRequest;
    }

    public String generateBarChartGraph(int codContrato){
        

        logger.debug("Buscando Dados para Montar Grafico de Consumo......");
        String fileName="barChartFile.png";
        StringBuilder barProgressHtml = new StringBuilder();
        
        
        try {

            List<TBRelatorioConsumo> listRelatorioConsumo = tbRelatorioConsumoDAO.getReportForAllProducts(codContrato);        

            
            for ( TBRelatorioConsumo tbRelatorioConsumo: listRelatorioConsumo ){
                
            
                String modelHtml=
                
                "<table style=\"width: 300px\">"+
                "<tr>"+
                "<td>$TITULO$</td><td align=\"right\" >$TEXTO$</td>"+
                "</tr>"+
                "<tr>"+
                "<td colspan=\"2\">"+
                        "<div    style=\"width: 300px; "+
                                "height: 15px;"+
                                "border: 1px solid #888; "+
                                "background: rgb(181,181,181);"+
                                "position: relative;\">"+

                                "<div  style=\" width:$SIZE$%;height: 14px; "+
                                "background: rgb(101,153,153); "+
                                "border-top: 1px solid #fceabb;\">"+
                                        "<p style=\"position: absolute; text-align: center; width: 100%; margin: 0; line-height: 9px;\"></p>"+
                                "</div>"+
                        "</div>"+
                "</td>"+
                "</tr>"+
                "</table>"+
                "<br/>";
                
                
                modelHtml = StringUtils.replace(modelHtml,"$TITULO$", tbRelatorioConsumo.getNomeProduto() );
                modelHtml = StringUtils.replace(modelHtml,"$SIZE$", Double.toString(tbRelatorioConsumo.getPorcentagem()) );
                modelHtml = StringUtils.replace(modelHtml,"$TEXTO$", String.format("%d de %d",tbRelatorioConsumo.getQtdAtual(),tbRelatorioConsumo.getQtdMes() ) );
                
                
                barProgressHtml.append(modelHtml);
                
            }
            
            
            HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
            imageGenerator.loadHtml(barProgressHtml.toString());
            imageGenerator.saveAsImage(String.format("%s/%s", config.getOutDirectory(), fileName ));
            
            
            
        } catch (Exception ex){
            logger.error("Erro ao gerar Grafico . Message {}", ex);
        }
        
        return fileName;
        
    }
    
    
    public List<String> generateBarChartGraph2(int codContrato){
        

        logger.debug("Buscando Dados para Montar Grafico de Consumo......");
        String fileName=null;
        List<String> listFileNames=new ArrayList<String>();
        
        
        try {

            List<TBRelatorioConsumo> listRelatorioConsumo = tbRelatorioConsumoDAO.getReportForAllProducts(codContrato);  
            
            /* Usar este trecho somente se nao tiver mais os dados da tabela SPF_TBCONTRATO_PRODUTO 
             disponivel para aquele mes ja que a tabela é zerada no final de cada mes
            
            List<TBContrato> listContrato = tbContratoDAO.getContratoById(codContrato);
            String clientes;
            if ( codContrato!=124 ){
                clientes = tbRelatorioDiagnosticoDAO.getHierarchyClients(Integer.toString( listContrato.get(0).getCodCliente() ) );
            } else {
                //asis tratamento diferente 
                clientes ="254";
            }
            List<TBRelatorioConsumo> listRelatorioConsumo = tbRelatorioConsumoDAO.getReportForAllProductsByControleDeProcesso(codContrato, Arrays.asList(StringUtils.split(clientes, ',')) );        
            */
            
            DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
            unusualSymbols.setDecimalSeparator('.');       
            
            DecimalFormat df = new DecimalFormat("##.##", unusualSymbols);
            

            for ( TBRelatorioConsumo tbRelatorioConsumo: listRelatorioConsumo ){

                
                fileName = String.format("%s_%d.png",tbRelatorioConsumo.getNomeProduto(), codContrato );
                
                StringBuilder url = new StringBuilder();
                url.append(URL_GOOGLE_CHART);
                url.append("cht=p3&"); //tipo de grafico
                url.append("chco=669999,b5b5b5&"); //cor
                url.append("chs=450x200&"); //visualizacao
                url.append("chts=000000,16&"); //cor e fonte
                url.append( String.format("chtt=%s&", tbRelatorioConsumo.getNomeProduto())); //titulo 

                url.append(String.format("chd=t:%s,%s&", df.format( tbRelatorioConsumo.getPorcentagem() ),
                                                            "100" ) ) ; //valores

                url.append(String.format("chl=%.2f|%.2f&",(float) tbRelatorioConsumo.getPorcentagem(),
                                                          (float) 100-tbRelatorioConsumo.getPorcentagem() )) ; //label


                url.append( String.format("chdl=Usado(%d)|Disponivel(%d)", tbRelatorioConsumo.getQtdAtual(),
                                                                           tbRelatorioConsumo.getQtdMes() ) ); //legenda


                googleChartRequest.makeRequest( StringUtils.replaceEach(url.toString(), new String[]{"|", "%", " "}, new String[]{"%7C", "%25", "%20"}) ,
                                                fileName );

                
                listFileNames.add(fileName);
                
            }
            
            
        } catch (Exception ex){
            logger.error("Erro ao gerar Grafico . Message {}", ex);
        }
        
        return listFileNames;
        
    }
    
    public String generateDiagnosticGraph(int codContrato, String dataIniProc, String dataFimProc){
        
        logger.debug("Buscando Dados para Montar Grafico de Diagnostico......");
        String fileName=String.format( "diagnostico_%d.png", codContrato);
        
        int r = 0;
        int g = 0;
        int y = 0;

        try {

            List<TBContrato> listContrato = tbContratoDAO.getContratoById(codContrato);

            String clientes = tbRelatorioDiagnosticoDAO.getHierarchyClients(Integer.toString( listContrato.get(0).getCodCliente() ) );

            List<String> listProtocols = tbRelatorioDiagnosticoDAO.getProtocols(Arrays.asList(StringUtils.split(clientes, ',')) , dataIniProc,dataFimProc);


            List<Map<String, Object>> listResults = tbRelatorioDiagnosticoDAO.getConsolidatedResults( listProtocols /*, Arrays.asList(StringUtils.split(clientes, ','))*/ );


            for( Map<String,Object> map : listResults ){

                String cor =(String) map.get("COR");
                if ( cor.equals("G")){
                    g++;
                }else if (cor.equals("Y")){
                    y++;
                }else if(cor.equals("R")){
                    r++;
                }

            }
            
            //montar grafico usando google 
            
            
            DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
            unusualSymbols.setDecimalSeparator('.');       
            
            DecimalFormat df = new DecimalFormat("##.##", unusualSymbols);
            
            StringBuilder url = new StringBuilder();
            url.append(URL_GOOGLE_CHART);
            url.append("cht=p3&"); //tipo de grafico
            url.append("chco=04B404,FFFF00,FF0000&"); //cor
            url.append("chs=800x300&"); //visualizacao
            url.append("chts=000000,16&"); //cor e fonte
            //url.append("chtt=Diagnostico&"); //titulo 
            
            
            url.append(String.format("chd=t:%s,%s,%s&", df.format( ( (double)g/(r+g+y) ) *100 ),
                                                        df.format( ( (double)y/(r+g+y) ) *100 ),
                                                        df.format( ( (double)r/(r+g+y) ) *100 ) ) ) ; //valores
            
            
            
            url.append(String.format("chl=%.2f|%.2f|%.2f&",   ((float)g/(r+g+y))*100,
                                                              ((float)y/(r+g+y))*100,
                                                              ((float)r/(r+g+y))*100 )) ; //label
            
            
            url.append( String.format("chdl=Beneficio(%d)|Advertencia(%d)|Erro(%d)", 
                         g , y , r ) ); //legenda
            
            
            googleChartRequest.makeRequest( StringUtils.replaceEach(url.toString(), new String[]{"|", "%"}, new String[]{"%7C", "%25"}) ,
                                            fileName );
            
            
        }catch(Exception ex){
            logger.error("Erro ao gerar Grafico . Message {}", ex);
        }
        
        return fileName;
        
    }
    
    
    public String generateRecurrentGraph(int codContrato, String dataIniProc, String dataFimProc){
        
        logger.debug("Buscando Dados para Montar Grafico de Recorrencia......");
        String fileName=String.format( "recorrencia_%d.png", codContrato);
        StringBuilder valor = new StringBuilder();
        StringBuilder label = new StringBuilder() ;
        StringBuilder legenda = new StringBuilder();
        long totalRecorrencia=0;
        
        try {
            
            List<TBContrato> listContrato = tbContratoDAO.getContratoById(codContrato);

            String clientes = tbRelatorioDiagnosticoDAO.getHierarchyClients(Integer.toString( listContrato.get(0).getCodCliente() ) );

            List<String> listProtocols = tbRelatorioDiagnosticoDAO.getProtocols(Arrays.asList(StringUtils.split(clientes, ',')) , dataIniProc,dataFimProc);

            List<Map<String, Object>> listResults = tbRelatorioDiagnosticoDAO.getConsolidatedRecurrentResults(listProtocols/*, Arrays.asList(StringUtils.split(clientes, ',')) */);
            
            
            
            //montar grafico usando google
            
            DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
            unusualSymbols.setDecimalSeparator('.');       
            
            DecimalFormat df = new DecimalFormat("##.##", unusualSymbols);
            
            
            StringBuilder url = new StringBuilder();
            url.append(URL_GOOGLE_CHART);
            url.append("cht=p3&"); //tipo de grafico
            url.append("chco=B38FB1,7DA1BF,D2B29A,8a8587,C78D6B&"); //cor
            url.append("chs=1000x300&"); //visualizacao
            url.append("chts=000000,16&"); //cor e fonte
            //url.append("chtt=Recorrencia&"); //titulo 
            
            
            for( Map<String,Object> map : listResults ){
                totalRecorrencia = totalRecorrencia + (Long) map.get("RECORRENCIA")  ;
            }
            
            for( Map<String,Object> map : listResults ){

                long valorRecorrencia =  (Long) map.get("RECORRENCIA") ;
                valor.append( String.format("%s," , df.format( ( (double)  valorRecorrencia/ (totalRecorrencia) ) *100 )  ) );  //valores
                
                label.append( String.format("%.2f|" , ((float)valorRecorrencia/(totalRecorrencia))*100 ) ) ; //label
                
                legenda.append( String.format("%s(%d)|", (String)map.get("NOM_FORMULA"), (Long)map.get("RECORRENCIA") ) ) ; 
                
            }
            
            valor.replace(valor.length()-1, valor.length(), "");
           
            label.replace(label.length()-1, label.length(), "");
           
            legenda.replace(legenda.length()-1, legenda.length(), "");

           
            url.append(String.format("chd=t:%s&", valor.toString() ) ); 
           
            url.append(String.format("chl=%s&", label.toString() ) ); 
           
            url.append(String.format("chdl=%s", legenda.toString() ) ); 

            
            
            googleChartRequest.makeRequest( StringUtils.replaceEach(url.toString(), new String[]{"|", "%", " "}, new String[]{"%7C", "%25", "%20"}) ,
                                            fileName );
            
            
        }catch(Exception ex){
            logger.error("Erro ao gerar Grafico . Message {}", ex);
        }
        
        return fileName;
        
    }


}

