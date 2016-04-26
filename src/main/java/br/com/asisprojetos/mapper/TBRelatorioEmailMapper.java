/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.mapper;

import br.com.asisprojetos.enums.TipoEnvio;
import br.com.asisprojetos.model.TBRelatorioEmail;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author acer k
 */
public class TBRelatorioEmailMapper implements RowMapper<TBRelatorioEmail>{
    
    @Override
    public TBRelatorioEmail mapRow(ResultSet rs, int rowNum) throws SQLException {

       TBRelatorioEmail tbRelatorioEmail = new TBRelatorioEmail();
       tbRelatorioEmail.setCodContrato(rs.getInt("COD_CONTRATO"));
       tbRelatorioEmail.setCodProduto(rs.getString("COD_PRODUTO"));
       //tbRelatorioEmail.setTipoEnvio(TipoEnvio.valueOf(rs.getString("TIPO_ENVIO")));
       //tbRelatorioEmail.setDataEnvio(rs.getString("DATA_ENVIO"));
       //tbRelatorioEmail.setStatus(rs.getBoolean("STATUS"));
       
       return tbRelatorioEmail;
    }
    
    
}
