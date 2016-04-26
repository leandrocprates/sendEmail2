/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.mapper;

import br.com.asisprojetos.model.TBContrato;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author acer k
 */
public class TBContratoMapper implements RowMapper<TBContrato>{
    
    
    @Override
    public TBContrato mapRow(ResultSet rs, int rowNum) throws SQLException {

       TBContrato tbContrato = new TBContrato();
       tbContrato.setCodContrato(rs.getInt("COD_CONTRATO"));
       tbContrato.setCodCliente(rs.getInt("COD_CLIENTE"));
       tbContrato.setCodVendedor(rs.getInt("COD_VENDEDOR"));
       tbContrato.setNumContrato(rs.getString("NUM_CONTRATO"));
       tbContrato.setDataVigenciaIni(rs.getDate("DAT_VIGENCIA_INI"));
       tbContrato.setDataVigenciaFim(rs.getDate("DAT_VIGENCIA_FIM"));
       tbContrato.setComissao(rs.getFloat("COMISSAO"));
       tbContrato.setQtdProcessMes(rs.getInt("QTD_PROCESS_MES"));
       tbContrato.setQtdProcessAtu(rs.getInt("QTD_PROCESS_ATU"));
       tbContrato.setDataUltimaAtu(rs.getDate("DAT_ULTIMA_ATU"));
       tbContrato.setDataCriacao(rs.getDate("DAT_CRIACAO"));
       tbContrato.setQtdCnpj(rs.getInt("QTD_CNPJ"));
       tbContrato.setQtdNF(rs.getInt("QTD_NF"));
       tbContrato.setQtdParcelas(rs.getInt("QTD_PARCELAS"));
       tbContrato.setValor(rs.getFloat("VALOR"));
       tbContrato.setInAtivo(StringUtils.equals( rs.getString("IN_ATIVO"), "A" ));
       tbContrato.setFerramenta(rs.getInt("FERRAMENTA"));
       tbContrato.setPrioridade(rs.getInt("PRIORIDADE"));
       
       return tbContrato;
    }
    
    
}
