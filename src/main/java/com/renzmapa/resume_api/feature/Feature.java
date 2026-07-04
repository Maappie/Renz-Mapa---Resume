package com.renzmapa.resume_api.feature;

import java.util.List;

/**
 * Immutable data model for the Feature resource.
 *
 * <p>Using a Java {@code record} enforces immutability by design — no setters,
 * no accidental mutation. All fields are final and constructor-assigned.
 *
 * <p>Naming convention: match the JSON contract exactly (camelCase).
 * The record field names become the serialized JSON keys via Jackson.
 *
 * @param id          Unique identifier for the resource.
 * @param name        Display name.
 * @param description A short human-readable description.
 * @param tags        Optional list of classification tags.
 */
public record Feature(
    Long id,
    String name,
    String description,
    List<String> tags
) {}
