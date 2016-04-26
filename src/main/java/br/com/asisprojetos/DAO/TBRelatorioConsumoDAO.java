/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.DAO;

import br.com.asisprojetos.mapper.TBRelatorioConsumoMapper;
import br.com.asisprojetos.model.TBRelatorioConsumo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author acer k
 */
public class TBRelatorioConsumoDAO extends JDBCAbstract {
    
    
    static Logger logger = LoggerFactory.getLogger(TBRelatorioConsumoDAO.class);
    
    
    public List<TBRelatorioConsumo> getReportForAllProducts(int codContrato ){
        
        String SQL =    " SELECT " + 
                        " p.NOM_PRODUTO produto , " + 
                        " cp.QTD_PROCESS_MES as qtdMes, " + 
                        " cp.QTD_PROCESS_ATU as qtdAtual, " + 
                        " IFNULL(((cp.QTD_PROCESS_ATU * 100) / cp.QTD_PROCESS_MES),0) as porcentagem " + 
                        " FROM SPF_TBCONTRATO c, " + 
                        " SPF_TBCONTRATO_PRODUTO cp, "+ 
                        " SPF_TBPRODUTO p " +  
                        " WHERE c.COD_CONTRATO = cp.COD_CONTRATO " +  
                        " AND p.COD_PRODUTO = cp.COD_PRODUTO " +  
                        " AND cp.COD_CONTRATO = :codContrato " +
                        " AND p.NOM_PRODUTO != 'CRUZAMENTOS' " +
                        " AND p.NOM_PRODUTO != 'EDIT' " +
                        " AND p.NOM_PRODUTO != 'ECF' " +
                        " AND p.NOM_PRODUTO != 'SPED FISCAL x CTe' " +
                        " ORDER BY qtdAtual,produto ASC  ";  
                          

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        
        namedParameters.addValue( "codContrato" , codContrato);
        
        List <TBRelatorioConsumo> tbRelatorioConsumo=null;
        
        try {
            tbRelatorioConsumo = getNamedParameterJdbcTemplate().query(SQL, namedParameters, new TBRelatorioConsumoMapper());
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        return tbRelatorioConsumo;                
        
    }
    
    
    /*Funcao criada para buscar quantos arquivos foram enviados "Relatório de Consumo de Arquivos" . 
      Busca sera feita na tabela Controle De Processo SPF_TBCTRL_PROCESSO.
    */
    public List<TBRelatorioConsumo> getReportForAllProductsByControleDeProcesso(int codContrato, List<String> listCodCliente ){
        
                String SQL  = ""
                    + "SELECT "
                    + "   NOM_PRODUTO AS  produto , "
                    + "   contratoProduto.QTD_PROCESS_MES AS qtdMes , "
                    + "   count(*) AS qtdAtual , "
                    + "   IFNULL((( count(*) * 100) / contratoProduto.QTD_PROCESS_MES ), 0) AS porcentagem  "
                    + "FROM "
                    + "              SPF_TBCTRL_PROCESSO ctrlProcesso "
                    + "   INNER JOIN SPF_TBPRODUTO tbProduto "
                    + "   on         ctrlProcesso.COD_PRODUTO = tbProduto.COD_PRODUTO "
                    + "   INNER JOIN SPF_TBCONTRATO_PRODUTO  contratoProduto "
                    + "   on contratoProduto.COD_CONTRATO = :codContrato "
                    + "WHERE ctrlProcesso.COD_CLIENTE in( "
                    //+ "       SELECT COD_CLIENTE FROM SPF_TBCONTRATO WHERE COD_CONTRATO = :codContrato "
                    + "  :clientesId "    
                    + "       ) "
                    + "AND ctrlProcesso.SITUACAO=4 "
                    + "AND ctrlProcesso.DATA_INIC_PROC>='2015-11-01 00:00:00' "
                    + "AND ctrlProcesso.DATA_FIM_PROC <= '2015-11-30 23:59:59' "
                    + "AND ctrlProcesso.COD_PRODUTO "
                    + "in ( "
                    + " "
                    + "		select COD_PRODUTO from SPF_TBPRODUTO where NOM_PRODUTO  in ( "
                    + "		'CONTROLE ESTOQUE', "
                    + "		'DIPJ', "
                    + "		'EFD CONTRIBUIÇÕES', "
                    + "		'EFD CONTRIBUIÇÕES x DACON', "
                    + "		'EFD CONTRIBUIÇÕES x NFE', "
                    + "		'EFD CONTRIBUIÇÕES x SPED FISCAL', "
                    + "		'Livro Eletrônico - DF', "
                    + "		'SEF', "
                    + "		'SEF II - 2012', "
                    + "		'SEF II - e-DOC', "
                    + "		'SINTEGRA', "
                    + "		'SINTEGRA ST', "
                    + "		'SPED CONTABIL', "
                    + "		'SPED CONTÁBIL x DIPJ', "
                    + "		'SPED FISCAL x DAPI', "
                    + "		'SPED FISCAL x GIARJ', "
                    + "		'SPED FISCAL x GIASP', "
                    + "		'SPED FISCAL x NFE', "
                    + "		'SPED FISCAL x SEF II e-Doc', "
                    + "		'ASIS Tax', "
                    + "		'NOTA FISCAL ELETRÔNICA', "
                    + "		'SPED FISCAL', "
                    + "		'ASIS Tax Linhas' "
                    + "		 ) "
                    + "	) "
                    + "     "
                    + "    AND contratoProduto.COD_PRODUTO = ctrlProcesso.COD_PRODUTO "
                    + "     "
                    + "group by ctrlProcesso.COD_PRODUTO "
                    + ";";        

                
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        
        namedParameters.addValue( "codContrato" , codContrato);
        namedParameters.addValue( "clientesId" , listCodCliente);
        
        List <TBRelatorioConsumo> tbRelatorioConsumo=null;
        
        try {
            tbRelatorioConsumo = getNamedParameterJdbcTemplate().query(SQL, namedParameters, new TBRelatorioConsumoMapper());
        }catch(DataAccessException ex){
            logger.error("Erro ao efetuar busca no banco de dados. Message {}", ex);
        }
        
        return tbRelatorioConsumo;                
        
    }
    
}
