/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.mapper;

import br.com.asisprojetos.model.TBRelatorioConsumo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author acer k
 */
public class TBRelatorioConsumoMapper implements RowMapper<TBRelatorioConsumo>{
    
    @Override
    public TBRelatorioConsumo mapRow(ResultSet rs, int rowNum) throws SQLException {

       TBRelatorioConsumo tbRelatorioConsumo = new TBRelatorioConsumo();
       tbRelatorioConsumo.setNomeProduto(rs.getString("produto"));
       tbRelatorioConsumo.setQtdMes(rs.getInt("qtdMes"));
       tbRelatorioConsumo.setQtdAtual(rs.getInt("qtdAtual"));
       tbRelatorioConsumo.setPorcentagem(rs.getDouble("porcentagem"));
       
       return tbRelatorioConsumo;
    }
    
    
}
