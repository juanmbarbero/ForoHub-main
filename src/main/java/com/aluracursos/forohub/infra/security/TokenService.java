package com.aluracursos.forohub.infra.security;

import com.aluracursos.forohub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${forohub.security.secret}")
    private String forohubSecret;

    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(forohubSecret);//Ver nota
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarTiempoDeExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException();
        }
    }
    public String getSubject(String token){
        if (token == null){
            throw new RuntimeException("El token no puede ser nulo.");
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(forohubSecret);//Validando firma
            verifier = JWT.require(algorithm)
                    .withIssuer("forohub")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception){
            System.out.println(exception.toString());
        }
        if(verifier.getSubject() == null){
            throw new RuntimeException("Verifier es inválido.");
        }
        return verifier.getSubject();
    }

    private Instant generarTiempoDeExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

//La variable forohubSecret no es la clave personal del usuario, sino una clave secreta utilizada por la aplicación para firmar y verificar los tokens JWT (JSON Web Tokens) generados.
//
//En el contexto de la autenticación y autorización basada en tokens JWT, el forohubSecret cumple las siguientes funciones:
//
//    Firma del token: Cuando se genera un token JWT para un usuario autenticado, este token debe ser firmado digitalmente para garantizar su integridad y autenticidad. La firma se realiza utilizando un algoritmo criptográfico (en este caso, HMAC256) y la clave secreta forohubSecret.
//
//    Verificación del token: Cuando un cliente envía un token JWT en las solicitudes, el servidor debe verificar la firma del token utilizando la misma clave secreta forohubSecret. Esto permite al servidor confiar en la validez del token y autorizar el acceso a los recursos protegidos.
//
//La clave secreta forohubSecret debe ser conocida únicamente por el servidor y no debe ser compartida con los clientes. Esta clave actúa como un "secreto compartido" entre el servidor y el cliente, lo que permite al servidor confiar en la autenticidad de los tokens generados.
//
//En resumen, el forohubSecret no es la clave personal del usuario, sino una clave secreta utilizada por la aplicación para asegurar la autenticidad de los tokens JWT generados durante el proceso de autenticación. Esta clave debe ser mantenida de forma segura en el servidor y no debe ser expuesta a los clientes.