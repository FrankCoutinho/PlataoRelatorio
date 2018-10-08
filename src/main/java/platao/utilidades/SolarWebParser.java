
package platao.utilidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.multipart.MultipartFile;

import platao.relatorios.RelatórioDiário;

public interface SolarWebParser
{
	public static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static List<RelatórioDiário> obterRelatóriosDiários(MultipartFile arquivo, double tarifa)
	{
		List<List<String>> tabela = obterTabela(arquivo);
		List<LocalDate> listaDeDatas = obterListaDeDatas(tabela);
		List<Double> listaDeGeraçãoDiária = obterListaDeGeraçãoDiária(tabela);

		return IntStream.range(0, listaDeDatas.size())
						.mapToObj(linha -> new RelatórioDiário(listaDeDatas.get(linha), listaDeGeraçãoDiária.get(linha), tarifa))
						.collect(Collectors.toList());
	}

	public static List<List<String>> obterTabela(MultipartFile arquivo)
	{
		try
		{
			return new BufferedReader(new InputStreamReader(arquivo.getInputStream()))	.lines()
																						.skip(2)
																						.map(line -> Arrays.asList(line.split(",")))
																						.collect(Collectors.toList());
		} catch (IOException exception)
		{
			throw new UncheckedIOException(exception);
		}
	}

	public static List<LocalDate> obterListaDeDatas(List<List<String>> tabela)
	{
		return tabela.stream()
					 .map(linha -> LocalDate.parse(linha.get(0), INPUT_DATE_FORMATTER))
					 .collect(Collectors.toList());
	}

	public static List<Double> obterListaDeGeraçãoDiária(List<List<String>> table)
	{
		return table.stream()
					.map(line -> Double.parseDouble(line.get(line.size() - 1)))
					.collect(Collectors.toList());
	}
}
