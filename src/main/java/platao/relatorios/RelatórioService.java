package platao.relatorios;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import platao.utilidades.SolarWebParser;

@Service
public class RelatórioService
{
	public List<RelatórioDiário> obterRelatóriosDiários(MultipartFile arquivo, double tarifa)
	{
		List<List<String>> tabela = SolarWebParser.obterTabela(arquivo);
		List<LocalDate> listaDeDatas = SolarWebParser.obterListaDeDatas(tabela);
		List<Double> listaDeGeraçãoDiária = SolarWebParser.obterListaDeGeraçãoDiária(tabela);

		return IntStream.range(0, listaDeDatas.size())
						.mapToObj(linha -> new RelatórioDiário(listaDeDatas.get(linha), listaDeGeraçãoDiária.get(linha), tarifa))
						.collect(Collectors.toList());
	}
}
