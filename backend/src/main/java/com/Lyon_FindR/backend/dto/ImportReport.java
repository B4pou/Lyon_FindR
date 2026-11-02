package com.Lyon_FindR.backend.dto;

import java.util.List;

public record ImportReport(List<CategoryResult> results) {
    public record CategoryResult(String category, int created, int updated, int skipped, String error) {}
}
