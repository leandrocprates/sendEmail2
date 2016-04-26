/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.DAO;

import br.com.asisprojetos.mapper.TBContratoMapper;
import br.com.asisprojetos.model.TBContrato;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author acer k
 */
public class TBContratoDAO extends JDBCAbstract {
    
    static Logger logger = LoggerFactory.getLogger(TBContratoDAO.class);
    
    
    public List<TBContrato> getContratoById(int codContrato ){
        
        String SQL = "SELECT  c.COD_CONTRATO,"+
                            " c.COD_CLIENTE,"+
                            " c.COD_VENDEDOR,"+
                            " c.NUM_CONTRATO,"+
                            " c.DAT_VIGENCIA_INI,"+
                            " c.DAT_VIGENCIA_FIM,"+
                            " c.COMISSAO,"+
                            " c.QTD_PROCESS_MES,"+
                            " c.QTD_PROCESS_ATU,"+
                            " c.DAT_ULTIMA_ATU,"+
                            " c.DAT_CRIACAO,"+
                            " c.QTD_CNPJ,"+
                            " c.QTD_NF,"+
                            " c.QTD_PARCELAS,"+
                            " c.VALOR,"+
                            " c.IN_ATIVO,"+
                            " c.FERRAMENTA,"+
                            " c.PRIORIDADE"+
                        " FROM SPF_TBCONTRATO c WHERE c.COD_CONTRATO = :codContrato  " ; 

        
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        
        namedParameters.addValue( "codContrato" , codContrato);
        
        List <TBContrato> tbContrato=null;
        
        try {
            tbContrato = getNamedParameterJdbcTemplate().query(SQL, namedParameters, new TBContratoMapper());
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        return tbContrato;                
        
    }
    
    
    
}
