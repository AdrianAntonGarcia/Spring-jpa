package com.bolsaideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

import javax.validation.Valid;

import com.bolsaideas.springboot.app.models.dao.IClienteDao;
import com.bolsaideas.springboot.app.models.entity.Cliente;

@Controller
public class ClienteController {

	@Autowired
	@Qualifier("clienteDaoJPA")
	private IClienteDao clienteDao;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteDao.findaAll());
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}

	/**
	 * El @BindingResult siempre tiene que ir junto al objeto que está validado
	 * con @Valid
	 * 
	 * El @ModelAttribute solo es necesario si el nombre con el que lo pasamos a la
	 * vista en el método anterior es distinto al que le
	 * estamos dando (Nombre de la clase) en el método receptor del form
	 * 
	 * @param cliente
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute(value = "cliente") Cliente clienteN, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		clienteDao.save(clienteN);
		return "redirect:listar";
	}
}
