/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.enums;

/**
 *
 * @author acer k
 */
public enum Produto {
    
    SPED_FISCAL("1"),
    SINTEGRA("2"),
    SPED_CONTABIL("3"),
    EFD_CONTRIBUIÇÕES("4"),
    NOTA_FISCAL_ELETRONICA("5");
    
    private String codProduto;
    
    private Produto(String codProduto) {
            this.codProduto = codProduto;
    }    

    public String getCodProduto() {
            return codProduto;
    }
    
}
