/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.DAO;


import lombok.Data;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author acer k
 */

@Data
public class JDBCAbstract {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

}
