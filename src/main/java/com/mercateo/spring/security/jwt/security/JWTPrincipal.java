/**
 * Copyright © 2017 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mercateo.spring.security.jwt.security;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercateo.spring.security.jwt.token.claim.JWTClaim;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public class JWTPrincipal implements UserDetails {

    private final Long id;

    private final String username;

    private final String token;

    private final List<? extends GrantedAuthority> authorities;

    private final Map<String, JWTClaim> claims;

    public JWTPrincipal(long id, String username, String token, List<? extends GrantedAuthority> authorities,
            Map<String, JWTClaim> claims) {
        this.id = Long.valueOf(id);
        this.username = username;
        this.token = token;
        this.authorities = authorities;
        this.claims = claims;
    }

    public static JWTPrincipal fromContext() {
        return (JWTPrincipal) requireNonNull(requireNonNull(SecurityContextHolder.getContext(),
                "no security context available").getAuthentication(), "no authentication available").getPrincipal();
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.toJavaList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Option<JWTClaim> getClaim(String key) {
        return claims.get(key);
    }
}
