package com.AppRH.AppRH.repository;

import java.util.List;

import com.AppRH.AppRH.models.Candidato;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.AppRH.AppRH.models.Vaga;
public interface CandidatoRepository extends CrudRepository<Candidato, String> {
    Iterable<Candidato>findByVaga(Vaga vaga);
    Candidato findByRg(String rg);
    Candidato findById(long id);
    List<Candidato>findByNomeCandidato(String nome);

}
