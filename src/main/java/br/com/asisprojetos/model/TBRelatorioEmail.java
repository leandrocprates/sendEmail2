/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.model;

import br.com.asisprojetos.enums.TipoEnvio;
import lombok.Data;

/**
 *
 * @author acer k
 */
@Data 
public class TBRelatorioEmail {

    private int codContrato;
    private String codProduto;
    private TipoEnvio tipoEnvio;
    private String dataEnvio;
    private boolean status;

}
