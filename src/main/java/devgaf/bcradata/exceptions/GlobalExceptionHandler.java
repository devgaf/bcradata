package devgaf.bcradata.exceptions;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// Manejo global de la excepción personalizada
	@ExceptionHandler(NoContentException.class)
	public ResponseEntity<String> handleNotFoundException(NoContentException ex) {
		log.error("Error de base de datos: {}", ex.toString());
		return new ResponseEntity<>(ex.toString(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<String> handleSQLException(SQLException ex) {
		log.error("Error en la base de datos: {}", ex.toString());
		return new ResponseEntity<>("Data base exception: " + ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SSLConfigurationException.class)
	public ResponseEntity<String> handleSSLConfigurationException(SSLConfigurationException ex) {
		log.error("SSL Configuration Error: {}", ex.toString());
		return new ResponseEntity<>("SSL Configuration Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Manejador de excepciones genéricas
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		log.error("Error inesperado: {}", ex.toString());
		return new ResponseEntity<>("Internal server error: " + ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
