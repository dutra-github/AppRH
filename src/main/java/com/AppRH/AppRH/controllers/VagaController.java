package com.AppRH.AppRH.controllers;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.AppRH.AppRH.models.Candidato;
import com.AppRH.AppRH.models.Vaga;
import com.AppRH.AppRH.repository.CandidatoRepository;
import com.AppRH.AppRH.repository.VagaRepository;
@Controller
public class VagaController {
    @Autowired
    private VagaRepository vr;
    @Autowired
    private CandidatoRepository cr;

    // GET que chama o FORM que CADASTRA VAGA
    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
    public String form() {
        return "Vaga/form-vaga";
    }

    // POST que cadastra a vaga
    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
    public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/cadastrarVaga";
        }

        vr.save(vaga);
        attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
        return "redirect:/cadastrarVaga";
    }

    // GET que lista as vagas
    @RequestMapping("/Vagas")
    public ModelAndView listaVagas() {
        ModelAndView mv = new ModelAndView("Vaga/lista-vaga");
        Iterable<Vaga> vagas = vr.findAll();
        mv.addObject("vagas", vagas);
        return mv;
    }

    // GET que mostra os detalhes da vaga e os candidatos
    @RequestMapping("Vaga/{codigo}")
    public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("Vaga/detalhes-vaga");
        mv.addObject("vaga", vaga);

        Iterable<Candidato> candidatos = cr.findByVaga(vaga);
        mv.addObject("candidatos", candidatos);

        return mv;

    }

    // GET que DELETA VAGAS
    @RequestMapping("/deletarVaga")
    public String deletarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        vr.delete(vaga);
        return "redirect:/Vagas";
    }

    // POST que adiciona candidato a vaga
    @RequestMapping(value = "/Vaga/{codigo}", method = RequestMethod.POST)
    public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
                                   BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/{codigo}";
        }

        // rg duplicado
        if (cr.findByRg(candidato.getRg()) != null) {
            attributes.addFlashAttribute("mensagem_erro", "RG duplicado");
            return "redirect:/{codigo}";
        }

        Vaga vaga = vr.findByCodigo(codigo);
        candidato.setVaga(vaga);
        cr.save(candidato);
        attributes.addFlashAttribute("mensagem", "Candidato adionado com sucesso!");
        return "redirect:/{codigo}";
    }

    // GET que DELETA CANDIDATO pelo RG
    @RequestMapping("/deletarCandidato")
    public String deletarCandidato(String rg) {
        Candidato candidato = cr.findByRg(rg);
        Vaga vaga = candidato.getVaga();
        String codigo = "" + vaga.getCodigo();

        cr.delete(candidato);

        return "redirect:/" + codigo;

    }

    // Métodos que atualizam vaga
    // GET que chama o formulário de edição da vaga
    @RequestMapping(value = "/editar-vaga", method = RequestMethod.GET)
    public ModelAndView editarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("Vaga/update-vaga");
        mv.addObject("vaga", vaga);
        return mv;
    }

    // POST do FORM que atualiza a vaga
    @RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
    public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
        vr.save(vaga);
        attributes.addFlashAttribute("success", "Vaga alterada com sucesso!");

        long codigoLong = vaga.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/" + codigo;
    }

}