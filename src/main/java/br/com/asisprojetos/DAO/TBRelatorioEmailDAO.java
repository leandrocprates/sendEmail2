/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.DAO;

import br.com.asisprojetos.mapper.TBRelatorioEmailMapper;
import br.com.asisprojetos.model.TBRelatorioEmail;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author acer k
 */
public class TBRelatorioEmailDAO extends JDBCAbstract {
    
    static Logger logger = LoggerFactory.getLogger(TBRelatorioEmailDAO.class);
    
    public List<TBRelatorioEmail> getAll(){
        
        //String SQL = "select *from TB_RELATORIO_EMAIL order by COD_CONTRATO asc ";
        String SQL = " SELECT tbc.COD_CONTRATO, tbcp.COD_PRODUTO FROM SPF_TBCONTRATO tbc " + 
                " INNER JOIN SPF_TBCONTRATO_PRODUTO tbcp ON tbc.COD_CONTRATO = tbcp.COD_CONTRATO " +
                " WHERE tbcp.COD_PRODUTO = 1 AND tbc.IN_ATIVO = 'A' AND tbcp.DAT_VIGENCIA_FIM >= DATE_FORMAT(NOW(),'%Y-%m-%d') " ;
        
        
        List <TBRelatorioEmail> tbRelatorioEmail=null;
        
        try {
            tbRelatorioEmail = getNamedParameterJdbcTemplate().query(SQL, new TBRelatorioEmailMapper());
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        return tbRelatorioEmail;      
        
    }
    
    
    public List<String> getListaEmails(int codContrato){
        
        //CodPerfil deve ser 4 Cliente Master - trocar para producao
        //CodPerfil 1 - Master ASIS
        
        String SQL = " SELECT l.E_MAIL FROM SPF_TBCONTRATO c inner join  SPF_TBLOGIN  l "+
                    " on c.COD_CLIENTE = l.COD_CLIENTE where c.COD_CONTRATO = :codContrato AND "+ 
                    " l.COD_PERFIL in (1,4)  AND l.E_MAIL is not null  AND l.E_MAIL != '' AND " + 
                    " l.E_MAIL not in ( SELECT E_MAIL FROM TB_RELATORIO_BLACKLIST b where b.COD_CONTRATO = :codContrato and b.TIPO='EMAIL' )"; 
        
        SqlParameterSource namedParameters = new MapSqlParameterSource("codContrato", codContrato );
        
        
        List<String> listEmails=null;
        
        try {
            listEmails = getNamedParameterJdbcTemplate().queryForList(SQL, namedParameters, String.class);
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }

        return listEmails;
        
    }
    
    
}
