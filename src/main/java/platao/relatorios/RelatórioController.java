package platao.relatorios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/relatorios")
public class RelatórioController
{
	@Autowired
	private RelatórioService service;
	
	@PostMapping("/gerar")
	public List<RelatórioDiário> gerarRelatóriosDiários(@RequestPart MultipartFile arquivo, @RequestParam double tarifa) 
	{
		return service.obterRelatóriosDiários(arquivo, tarifa);
	}
}
