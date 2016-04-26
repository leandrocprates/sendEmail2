/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asisprojetos.model;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author acer k
 */
@Data
public class TBContrato {

    private int codContrato;
    private int codCliente;
    private int codVendedor;
    private String numContrato;
    private Date dataVigenciaIni;
    private Date dataVigenciaFim;
    private float comissao;
    private int qtdProcessMes;
    private int qtdProcessAtu;
    private Date dataUltimaAtu;
    private Date dataCriacao;
    private int qtdCnpj;
    private int qtdNF;
    private int qtdParcelas;
    private float valor;
    private boolean inAtivo;
    private int ferramenta;
    private int prioridade;
    
    
}
