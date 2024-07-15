package com.aluracursos.forohub.infra.security;

import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //OBTENER EL TOKEN DEL HEADER
        var authHeader = request.getHeader("Authorization");
        if(authHeader != null ){
            var token = authHeader.replace("Bearer ","");
            var emailUsuario = tokenService.getSubject(token);//EXTRAE EMAIL USUARIO
            if (emailUsuario != null){//SI TOKEN VALIDO
                var usuario = usuarioRepository.findByEmail(emailUsuario);
                var authentication = new UsernamePasswordAuthenticationToken(usuario,
                        null,
                        usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }
}


//EXPLICACION METODO

//@Component: Esta anotación indica que esta clase es un componente de Spring
// y será gestionada por el contenedor de Spring.

//public class SecurityFilter extends OncePerRequestFilter: Esta clase extiende
// de OncePerRequestFilter, que es una clase abstracta de Spring Security que
// garantiza que el filtro se ejecute una sola vez por solicitud.

//@Autowired private TokenService tokenService;: Esta línea inyecta una instancia
// del servicio TokenService, que se encarga de la gestión de los tokens de
// autenticación.

//@Autowired private UsuarioRepository usuarioRepository;: Esta línea inyecta una
// instancia del repositorio UsuarioRepository, que se utiliza para buscar usuarios
// en la base de datos.

//@Override protected void doFilterInternal(...): Este método se ejecuta para cada
// solicitud HTTP y es donde se implementa la lógica de filtrado.

//var authHeader = request.getHeader("Authorization");: Obtiene el valor del encabezado
// "Authorization" de la solicitud HTTP.

//if(authHeader != null): Verifica si el encabezado "Authorization" está presente.

//authHeader = authHeader.replace("Bearer ","");: Elimina el prefijo "Bearer " del
// valor del encabezado "Authorization".

//var subject = tokenService.getSubject(authHeader);: Utiliza el servicio TokenService
// para extraer el "subject" (que en este caso es el email del usuario) del token
// de autenticación.

//if (subject != null): Verifica si se pudo extraer el "subject" del token.

//var usuario = usuarioRepository.findByEmail(subject);: Busca el usuario en la base
// de datos utilizando el email extraído del token.

//var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
// usuario.getAuthorities());: Crea un objeto UsernamePasswordAuthenticationToken con
// el usuario encontrado, sus autoridades y un valor nulo para las credenciales.

//SecurityContextHolder.getContext().setAuthentication(authentication);: Establece la
// autenticación en el contexto de seguridad de Spring, lo que significa que el usuario
// está ahora autenticado.

//filterChain.doFilter(request, response);: Pasa la solicitud y la respuesta al
// siguiente filtro en la cadena de filtros.
