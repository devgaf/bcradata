package devgaf.bcradata.services.thirdparty;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;

import devgaf.bcradata.dtos.Icl;
import devgaf.bcradata.exceptions.BcraServiceException;
import devgaf.bcradata.exceptions.NoContentException;
import devgaf.bcradata.exceptions.SSLConfigurationException;
import devgaf.bcradata.repositories.IclRepository;
import devgaf.bcradata.models.IclEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Servicio para consultar el BCRA.
 */
@Slf4j
@Service
public class BcraService {

	@Value("${urlBcraPrincipalesVariables}")
	private String urlBcraPrincipalesVariables;

	@Value("${urlBcraBase}")
	private String urlBcraBase;

	@Value("${bcraVersion}")
	private String bcraVersion;

	@Value("${bcraPathBase}")
	private String bcraPathBase;

	@Value("${bcraIclPath}")
	private String bcraIclPath;

	@Value("${bcraLimitZero}")
	private String bcraLimitZero;

	private String urlBcraIclFullRecords = urlBcraPrincipalesVariables + bcraIclPath + bcraLimitZero;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final IclRepository iclRepository;

	/**
	 * Constructor de la clase BcraService.
	 * 
	 * @param restTemplate  RestTemplate para realizar las solicitudes HTTP.
	 * @param iclRepository Repositorio de la entidad IclEntity.
	 */
	public BcraService(RestTemplate restTemplate, IclRepository iclRepository) {
		this.restTemplate = restTemplate;
		this.iclRepository = iclRepository;
	}

	/**
	 * Lanza una excepcion dependiendo del tipo de excepcion pasada como parametro.
	 * 
	 * @param mensaje el mensaje de error a mostrar
	 * @param ex      la excepcion a lanzar
	 * 
	 * @throws IOException               si <code>ex</code> es una instancia de
	 *                                   {@link IOException}
	 * @throws SSLConfigurationException si <code>ex</code> es una instancia de
	 *                                   {@link SSLConfigurationException}
	 * @throws BcraServiceException      en caso contrario
	 */
	private void throwException(String mensaje, Exception ex) throws IOException {
		log.error(mensaje, ex);
		switch (ex) {
			case SSLConfigurationException sslconfigurationexception -> throw sslconfigurationexception;
			case IOException ioException -> throw ioException;
			case NoContentException noContentException -> throw noContentException;
			default -> throw new BcraServiceException(ex.toString());
		}
	}

	/**
	 * Guarda un nuevo IclEntity en la base de datos si no existe, o actualiza su
	 * valor si ya existe.
	 * 
	 * @param iclEntity el IclEntity a guardar
	 */
	public void saveIcl(IclEntity iclEntity) {
		IclEntity existingIcl = iclRepository.findByDate(iclEntity.getDate());
		if (existingIcl != null && Double.compare(existingIcl.getMeasurement(), iclEntity.getMeasurement()) != 0) {
			existingIcl.setMeasurement(iclEntity.getMeasurement());
			iclRepository.save(existingIcl);
		} else if (existingIcl == null) {
			iclRepository.save(iclEntity);
		}
	}

	/**
	 * Valida que un nodo JSON no sea nulo y contenga los campos requeridos 'fecha'
	 * y 'valor'.
	 * 
	 * @param node el nodo JSON a validar
	 * @throws IOException si el nodo es nulo o falta alguno de los campos
	 *                     requeridos
	 */

	private void validateJsonNode(JsonNode node) throws IOException {
		if (node == null) {
			log.warn("Null JSON node encountered");
			throw new IOException("Null JSON node encountered");
		}
		if (!node.has("fecha") || !node.has("valor")) {
			log.warn("Missing 'fecha' or 'valor' in JSON node: {}", node.toString());
			throw new IOException("Missing 'fecha' or 'valor' in JSON node: {" + node.toString() + "}");
		}
	}

	/**
	 * Mapea una respuesta JSON del BCRA a una lista de Icl.
	 * 
	 * @param jsonResponse la respuesta JSON del BCRA
	 * @return una lista de Icl con los datos de la respuesta JSON
	 * @throws IOException si hay un error parseando la respuesta JSON
	 */
	private List<Icl> mapToIclList(String jsonResponse) throws IOException {
		JsonNode rootNode = objectMapper.readTree(jsonResponse);
		JsonNode resultsNode = rootNode.get("results");
		List<Icl> iclList = new ArrayList<>();
		if (resultsNode != null && resultsNode.isArray()) {
			for (JsonNode node : resultsNode) {
				validateJsonNode(node);
				JsonNode dateNode = node.get("fecha");
				JsonNode valueNode = node.get("valor");
				Icl icl = new Icl();
				icl.setDate(LocalDate.parse(dateNode.asText()));
				icl.setMeasurement(valueNode.asDouble());
				iclList.add(icl);
			}
		} else {
			log.warn("Missing 'results' array in JSON response");
			throw new IOException("Missing 'results' array in JSON response");
		}
		return iclList;
	}

	/**
	 * Consulta el BCRA y devuelve una lista del ICL historico
	 * 
	 * @return lista de Icl
	 * @throws SSLConfigurationException si hay un error en la configuracion SSL
	 * @throws IOException               si hay un error parseando la respuesta JSON
	 */
	public List<Icl> getResponseBcraIcl() throws SSLConfigurationException, IOException {
		String url = UriComponentsBuilder.fromUriString(urlBcraIclFullRecords)
					.queryParam("limit", "0")
					.toUriString();
		try {
			String response = restTemplate.getForObject(url, String.class);
			if (response != null) {
				List<Icl> iclList = mapToIclList(response);
				if (iclList.isEmpty()) {
					throw new NoContentException("No se han encontrado datos");
				} else {
					iclRepository.saveAll(iclList.stream().map(this::toEntity).toList());
				}
				return iclList;
			} else {
				throw new NoContentException("Response body is null");
			}
		} catch (SSLConfigurationException sslEx) {
			throwException("SSL Configuration Error", sslEx);
		} catch (IOException ioEx) {
			throwException("IOException Error", ioEx);
		} catch (NoContentException ncex) {
			throwException("NoContentException Error", ncex);
		} catch (Exception ex) {
			throwException("Error fetching data from BCRA", ex);
		}
		return new ArrayList<>();
	}

	/**
	 * Consulta el BCRA y devuelve una lista del ICL desde una fecha dateIni hasta
	 * una fecha dateEnd
	 * 
	 * @param dateIni fecha de inicio en formato "dd/MM/yyyy"
	 * @param dateEnd fecha de fin en formato "dd/MM/yyyy"
	 * @return lista de Icl desde dateIni hasta dateEnd
	 * @throws SSLConfigurationException si hay un error en la configuracion SSL
	 * @throws IOException               si hay un error parseando la respuesta JSON
	 */
	public List<Icl> getResponseBcraIclFromDate(String dateIni, String dateEnd)
			throws SSLConfigurationException, IOException {
		String url = UriComponentsBuilder.fromUriString(urlBcraIclFullRecords)
				.queryParam("limit", "0")
				.queryParam("desde", dateIni)
				.queryParam("hasta", dateEnd)
				.toUriString();
		try {
			String response = restTemplate.getForObject(url, String.class);
			if (response != null) {
				List<Icl> iclList = mapToIclList(response);
				if (iclList.isEmpty()) {
					throw new NoContentException("No se han encontrado datos entere las fechas " + dateIni + " y " + dateEnd);
				} else {
					iclRepository.saveAll(iclList.stream().map(this::toEntity).toList());
				}
				return iclList;
			} else {
				throw new NoContentException("Response body is null");
			}
		} catch (SSLConfigurationException sslEx) {
			throwException("SSL Configuration Error", sslEx);
		} catch (IOException ioEx) {
			throwException("IOException Error", ioEx);
		} catch (NoContentException ncex) {
			throwException("NoContentException Error", ncex);
		} catch (Exception ex) {
			throwException("Error fetching data from BCRA", ex);
		}
		return new ArrayList<>();
	}

	/**
	 * Convierte un objeto Icl a una entidad IclEntity.
	 * 
	 * @param icl el objeto Icl a convertir
	 * @return una entidad IclEntity con los datos del objeto Icl
	 */
	private IclEntity toEntity(Icl icl) {
		IclEntity entity = new IclEntity();
		entity.setDate(icl.getDate());
		entity.setMeasurement(icl.getMeasurement());
		return entity;
	}
}
