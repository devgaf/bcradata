package devgaf.bcradata.exceptions;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase que maneja las excepciones globales de la aplicación
 * logueo de errores con SLF4J y manejo de excepciones
 * 
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	
	/**
	 * Manejo global de la excepción NoContentException
	 * 
	 * @param ex la excepción lanzada
	 * @return una respuesta con un mensaje de error y un estado HTTP 204
	 */
	@ExceptionHandler(NoContentException.class)
	public ResponseEntity<String> handleNotFoundException(NoContentException ex) {
		log.error("Error de base de datos: {}", ex.toString());
		return new ResponseEntity<>(ex.toString(), HttpStatus.NO_CONTENT);
	}

	/**
	 * Manejo global de la excepción SQLException
	 * 
	 * @param ex la excepción lanzada
	 * @return una respuesta con un mensaje de error y un estado HTTP 500
	 */
	@ExceptionHandler(SQLException.class)
	public ResponseEntity<String> handleSQLException(SQLException ex) {
		log.error("Error en la base de datos: {}", ex.toString());
		return new ResponseEntity<>("Data base exception: " + ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Manejo global de la excepción SSLConfigurationException
	 * 
	 * @param ex la excepción lanzada
	 * @return una respuesta con un mensaje de error y un estado HTTP 500
	 */
	@ExceptionHandler(SSLConfigurationException.class)
	public ResponseEntity<String> handleSSLConfigurationException(SSLConfigurationException ex) {
		log.error("SSL Configuration Error: {}", ex.toString());
		return new ResponseEntity<>("SSL Configuration Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Manejo global de la excepción NoResourceFoundException
	 * 
	 * @param ex la excepción lanzada
	 * @return una respuesta con un mensaje de error y un estado HTTP 404
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException ex) {
		log.error("Resource not found: {}", ex.toString());
		return new ResponseEntity<>("Resource not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Manejo global de la excepciónes
	 * 
	 * @param ex la excepción lanzada
	 * @return una respuesta con un mensaje de error y un estado HTTP 500
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		log.error("Error inesperado: {}", ex.toString());
		return new ResponseEntity<>("Internal server error: " + ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
