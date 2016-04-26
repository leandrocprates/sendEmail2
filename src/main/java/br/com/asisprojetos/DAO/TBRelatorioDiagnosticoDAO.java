/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.DAO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author acer k
 */
public class TBRelatorioDiagnosticoDAO extends JDBCAbstract {
    
    static Logger logger = LoggerFactory.getLogger(TBRelatorioDiagnosticoDAO.class);
    
    /**
     * Busca cliente e suas filiais
     * @param codCliente
     * @return 
     **/
    public String getHierarchyClients(String codCliente ){
        
        List<String> listCodCliente = Arrays.asList(StringUtils.split(codCliente, ','));
        
        String SQL =    "SELECT" +
                            " GROUP_CONCAT(COD_CLIENTE) " +  
                        " FROM " +
                            " SPF_TBCLIENTE " + 
                        " WHERE " + 
                            " COD_MATRIZ IN ( :codCliente ) " + 
                            " AND COD_MATRIZ <> COD_CLIENTE " ; 
        
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        
        namedParameters.addValue( "codCliente" , listCodCliente );
        
        String groupCliente=null;
        
        try {
            groupCliente = getNamedParameterJdbcTemplate().queryForObject(SQL, namedParameters, String.class );
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        if ( groupCliente == null ){
            return codCliente;
        }else{
            return codCliente+","+getHierarchyClients(groupCliente);
        }
        
    }
    
    
    /**
     * Lista os protocolos para Cod Produto =1 Sped Fiscal , das Matriz e filiais 
     * @param listCodCliente
     * @param dataIniProc
     * @param dataFimProc
     * @return 
     **/
    public List<String> getProtocols(List<String> listCodCliente, String dataIniProc, String dataFimProc ){
        
        //String dataIniProc="2015-01-01 00:00:00";
        //String dataFimProc="2015-02-01 23:59:59";  
        //String cnpj="12359822000142";
        List<String> listProtocols=null;
        
        
        String SQL =    "SELECT "+
                            "tp.NUM_PROT  "+
                        "FROM " +
                            " SPF_TBITEM_RESULT tr " +    
                        " INNER JOIN " +
                            " SPF_TBCTRL_PROCESSO  tp " +    
                                " ON tr.NUM_PROT=tp.NUM_PROT " +
                        " WHERE " +
                            " tp.COD_PRODUTO= 1 " +  
                            " and tp.IS_TRIAL=0 " +  
                            " and tp.SITUACAO=4 " + 
                            " and tp.IN_ATIVO='A' " + 
                            " and ( " + 
                                " tp.COD_CLIENTE in ( " + 
                                    " :codCliente " + 
                                " ) " + 
                            " ) " +  
                            //" and tp.CNPJ=:cnpj " +  
                            " and tp.DATA_INIC_PROC>=:dataIniProc " +  
                            " and tp.DATA_FIM_PROC<=:dataFimProc " + 
                        " GROUP BY " + 
                            " tp.NUM_PROT " +  
                        " ORDER BY " + 
                            " tp.NUM_PROT desc  ";


        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        
        namedParameters.addValue( "codCliente" , listCodCliente );
        namedParameters.addValue( "dataIniProc" , dataIniProc );
        namedParameters.addValue( "dataFimProc" , dataFimProc );
        //namedParameters.addValue( "cnpj" , cnpj );
        
        try {
              listProtocols = getNamedParameterJdbcTemplate().queryForList(SQL, namedParameters, String.class );
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        return listProtocols;
        
    }
    
    
    public List<Map<String, Object>> getConsolidatedResults( List<String> listProtocols/*, List<String> listCodCliente*/ ){
        
        
        List<Map<String, Object>> listResults=null;
        
        String SQL =   " SELECT " +
                           " tf.cor as COR, " + 
                           " tf.COD_FORMULA as COD_FORMULA  " + 
                       " FROM " + 
                           " SPF_TBITEM_RESULT tr   " + 
                       " INNER JOIN " + 
                           " SPF_TBFORMULA tf  " + 
                               " on tr.COD_FORMULA=tf.COD_FORMULA  " + 
                       " INNER JOIN " + 
                           " SPF_TBCTRL_PROCESSO  tcp   " + 
                              "  on tr.NUM_PROT=tcp.NUM_PROT cross  " + 
                       " JOIN " + 
                            " SPF_TBFORMULA tf2   " + 
                       "  WHERE " + 
                           " tr.COD_FORMULA = tf2.COD_FORMULA  " + 
                           " and ( tcp.NUM_PROT in ( :numProtocol ) )  " + 
                           " and ( tf2.NIVEL_FORMULA not in  (  '4' , '5' )  ) " +  
                           //" and ( tcp.COD_CLIENTE in ( :codCliente  ) ) " +
                       " GROUP BY  tf.COD_FORMULA  ORDER BY tf.cor DESC " ;


        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        
        namedParameters.addValue( "numProtocol" , listProtocols );
        //namedParameters.addValue( "codCliente" , listCodCliente );
        
        
        try {
              listResults = getNamedParameterJdbcTemplate().queryForList(SQL, namedParameters );
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        return listResults;
        
        
    }
    
    
    public List<Map<String, Object>> getConsolidatedRecurrentResults( List<String> listProtocols/*, List<String> listCodCliente*/ ){
        
        
        List<Map<String, Object>> listResults=null;
        
        String SQL =    " SELECT T.COD_FORMULA, " +
                               " T.NOM_FORMULA, " + 
                               " COUNT(T.COD_FORMULA) AS RECORRENCIA " + 
                        " FROM " + 
                          " (SELECT P.NUM_PROT, " + 
                                  " F.NOM_FORMULA, " + 
                                  " F.COD_FORMULA " + 
                           " FROM SPF_TBCTRL_PROCESSO P " + 
                           " INNER JOIN SPF_TBITEM_RESULT IR ON P.NUM_PROT = IR.NUM_PROT " + 
                           " INNER JOIN SPF_TBFORMULA F ON IR.COD_FORMULA = F.COD_FORMULA " + 
                           " WHERE P.NUM_PROT IN ( :numProtocol ) " + 
                           "   AND F.NIVEL_FORMULA NOT IN ('4', '5') " + 
                           //"   AND P.COD_CLIENTE IN ( :codCliente ) " + 
                           " GROUP BY P.NUM_PROT, F.COD_FORMULA) AS T " + 
                           " GROUP BY T.COD_FORMULA " + 
                           " ORDER BY COUNT(T.COD_FORMULA) DESC LIMIT 5  " ; 

        
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        
        namedParameters.addValue( "numProtocol" , listProtocols );
        //namedParameters.addValue( "codCliente" , listCodCliente );
        
        
        try {
              listResults = getNamedParameterJdbcTemplate().queryForList(SQL, namedParameters );
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        return listResults;
        
    }
    
    
}
