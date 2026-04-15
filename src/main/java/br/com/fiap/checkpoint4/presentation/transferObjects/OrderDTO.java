package br.com.fiap.checkpoint4.presentation.transferObjects;

import java.util.List;

import lombok.Builder;

@Builder
public record OrderDTO(
        Long orderId,
        List<String> products) {

}