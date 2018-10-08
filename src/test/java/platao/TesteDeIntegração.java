
package platao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import platao.relatorios.RelatórioController;
import platao.relatorios.RelatórioDiário;

@ComponentScan
@SpringJUnitConfig
@WebMvcTest(RelatórioController.class)
class TesteDeIntegração
{
	private static final double ENERGIA_GERADA_TOTAL_DO_ARQUIVO_CSV = 4916.75;
	private static final String URL_DE_GERAR_RELATÓRIOS = "/relatorios/gerar/";
	
	private static final String ARQUIVO_CSV_KEY = "arquivo";
	private static final String ARQUIVO_CSV = "src/test/resources/Teste.csv";

	private static final String TARIFA_KEY = "tarifa";
	private static final String TARIFA = "0.8";
	
	@Autowired private MockMvc mockMVC;
	@Autowired private ObjectMapper mapper;

	@Test
	void testeDeIntegração() throws Exception
	{
		MockMultipartFile file = new MockMultipartFile(ARQUIVO_CSV_KEY, FileUtils.openInputStream(new File(ARQUIVO_CSV)));

		String resposta = mockMVC	.perform(multipart(URL_DE_GERAR_RELATÓRIOS).file(file)
																			   .param(TARIFA_KEY, TARIFA))
									.andExpect(status().isOk())
									.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
									.andReturn()
									.getResponse()
									.getContentAsString();

		RelatórioDiário[] relatórios = mapper.readValue(resposta, RelatórioDiário[].class);

		double valorEconomizadoTotal = Arrays.stream(relatórios)
											 .mapToDouble(relatório -> relatório.getValorEconomizado())
											 .sum();

		double energiaGeradaTotal = Arrays.stream(relatórios)
										  .mapToDouble(relatório -> relatório.getEnergiaGerada())
										  .sum();

		assertEquals(ENERGIA_GERADA_TOTAL_DO_ARQUIVO_CSV, energiaGeradaTotal);
		assertEquals(ENERGIA_GERADA_TOTAL_DO_ARQUIVO_CSV * Double.parseDouble(TARIFA), valorEconomizadoTotal);
	}
}
