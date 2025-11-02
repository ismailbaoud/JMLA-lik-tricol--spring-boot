package org.ismail.gestiondescommmendsfournisseurspringboot.service;

import org.ismail.gestiondescommmendsfournisseurspringboot.model.Commende;
import org.ismail.gestiondescommmendsfournisseurspringboot.repository.CommendeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommendeService {

    @Autowired
    public CommendeRepository commendeRepository;

    public Commende creerCommende(Commende commende) {
        return commendeRepository.save(commende);
    }

}
