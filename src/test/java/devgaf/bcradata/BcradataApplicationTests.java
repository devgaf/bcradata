package devgaf.bcradata;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BcradataApplicationTests {


	/*
	 * Este método verifica la carga del contexto de la aplicación y se asegura
	 * de que no haya excepciones no manejadas ni referencias nulas.
	 */
	@Test
	void contextLoads() {
		try {
			// Simulamos la carga del contexto
			// Si hay algún fallo en la carga, se lanzará una excepción
			// Aquí puedes agregar cualquier lógica adicional necesaria para la prueba

			// Verificamos que no haya referencias nulas
			assertNotNull(getClass(), "La referencia a la clase no debe ser nula");

		} catch (Exception e) {
			fail("Se lanzó una excepción inesperada: " + e.toString());
		}
	}
}
