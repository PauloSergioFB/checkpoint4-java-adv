package br.com.fiap.checkpoint4.presentation.transferObjects;

public record Event(
        OrderDTO payload, Integer attempt, String error) {
}
