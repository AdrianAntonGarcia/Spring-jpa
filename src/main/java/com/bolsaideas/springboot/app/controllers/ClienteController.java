package com.bolsaideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import com.bolsaideas.springboot.app.models.entity.Cliente;
import com.bolsaideas.springboot.app.models.service.IClienteService;
import com.bolsaideas.springboot.app.models.service.IUploadFileService;
import com.bolsaideas.springboot.app.util.paginator.PageRender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Guardamos el objeto cliente en sesión para tener el campo id que no vendría
 * en el formulario
 */
@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	@Qualifier("clienteServiceImpl")
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService fileService;

	/**
	 * En vez de con el mvcConfig, lo hacemos a través de la respuesta http
	 * 
	 * @param id
	 * @param model
	 * @param flash
	 * @return
	 */
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = fileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteService.findOneWithFacturas(id);
		if (cliente == null) {
			flash.addAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		return "ver";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findaAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("page", pageRender);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
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
			Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		if (!foto.isEmpty()) {

			if (clienteN.getId() != null && clienteN.getId() > 0 && clienteN.getFoto() != null
					&& clienteN.getFoto().length() > 0) {
				fileService.delete(clienteN.getFoto());
			}
			String uniqueFilename;
			try {
				uniqueFilename = fileService.copy(foto);
				flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
				clienteN.setFoto(uniqueFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String mensajeFlash = (clienteN.getId() != null) ? "Cliente editado con éxito!"
				: "Cliente creado con éxito!";
		clienteService.save(clienteN);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "Cliente no encontrado");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente no encontrado");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Modificar el cliente: ".concat(id.toString()));
		return "form";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente clienteEliminar = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
			if (fileService.delete(clienteEliminar.getFoto())) {
				flash.addFlashAttribute("info", "Foto eliminada");
			}
		}
		return "redirect:/listar";
	}
}
