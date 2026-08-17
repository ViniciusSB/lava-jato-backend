package com.lavajato.dto.ordemServico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteOrdemServico {
    private Long id;
    private String celular;
    private Integer fidelidade;
}
