package platao.relatorios;

import java.time.LocalDate;

import lombok.Value;
import lombok.NonNull;

@Value
public class RelatórioDiário
{
	private LocalDate data;
	private Double energiaGerada;
	private Double valorEconomizado;
	
	public RelatórioDiário()
	{
		energiaGerada = 0.0;
		valorEconomizado = 0.0;
		data = LocalDate.now();
	}

	public RelatórioDiário(@NonNull LocalDate data, @NonNull Double energiaGerada, @NonNull Double tarifa)
	{
		super();
		this.data = data;
		this.energiaGerada = energiaGerada;
		this.valorEconomizado = energiaGerada * tarifa * 0.86;
	}
}
