package com.paypal.api_gateWay.filters;

import com.paypal.api_gateWay.config.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthFilter implements GlobalFilter, Ordered {



    public static final List<String> PUBLIC_PATH= List.of("/auth/signup","/auth/login");
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path= exchange.getRequest().getPath().value();
        String normalizaPath=path.replaceAll("/+$","");
        if (PUBLIC_PATH.contains(normalizaPath)){
            return chain.filter(exchange).
                    doOnSubscribe(s->System.out.print("Processing without cehcking"))
                    .doOnSuccess(v->System.out.println("successfully processing without cehcking"))
                    .doOnError(e-> System.err.println("error processing without cehcking"));
        }
        String authHeader= exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if( authHeader==null ||!authHeader.startsWith("Bearer")){
             exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
             return exchange.getResponse().setComplete();
        }
        try {
          String token= authHeader.substring(7);
            Claims claims= JwtUtil.validateToken(token);
            exchange.getRequest().mutate()
                    .header("X-User-Email",claims.getSubject())
                    .header("X-ser-Id",claims.get("userId",String.class))
                    .header("X-User-Role",claims.get("role",String.class)).build();
            return chain.filter(exchange).doOnSubscribe(s->System.out.print("Processing without cehcking")).
                    doOnSuccess(v->System.out.println("successfully processing without cehcking"))
                    .doOnError(e->System.err.println("error processing without cehcking"));
        } catch (Exception e) {
         exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
         return  exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
