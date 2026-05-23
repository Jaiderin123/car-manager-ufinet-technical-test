package com.ufinet.carmanager.infrastructure.config.security;

public record AuthPrincipal(Long userId, Long workspaceId, String role) {
}
