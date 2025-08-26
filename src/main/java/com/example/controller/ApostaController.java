package com.example.controller;

import com.example.model.Aposta;
import com.example.model.ApostaFactory;
import com.example.model.Resultado;
import com.example.model.Sorteio;
import com.example.observer.SorteioObservable;
import com.example.repository.ApostaRepository;
//Conecta a interface (JogoDoBichoGUI) com o modelo (ApostaFactory, Sorteio) e o observable (SorteioObservable).
public class ApostaController {
    private SorteioObservable sorteioObservable;
    
    private ApostaRepository apostaRepository;
    
    public ApostaController(SorteioObservable sorteioObservable, ApostaRepository apostaRepository) {
        this.sorteioObservable = sorteioObservable;
        this.apostaRepository = apostaRepository;
    }
    //Interface vai chamar esse criarAposta() do ApostaController → ApostaFactory → retorna Aposta.
    public Aposta criarERegistrarAposta(String tipo, String numero, double valor, String cpf) {
        Aposta aposta = ApostaFactory.criarAposta(tipo, numero, valor, cpf);
        apostaRepository.salvar(aposta);       // Salva no repository
        sorteioObservable.addAposta(aposta);   // Notifica observadores
        return aposta;
    }
    
    public SorteioObservable getSorteioObservable() {
    return sorteioObservable;
}

    //Interface vai chamar esse realizarSorteio() → ApostaController → Sorteio.realizarSorteio() → Resultado.
    
public void realizarSorteio() {
        
        Resultado resultado = Sorteio.realizarSorteio(apostaRepository);
        
        // Notifica o observer ou view com as apostas ganhadoras
        System.out.println("Número sorteado: " + resultado.getNumeroSorteado());
        System.out.println("Animal: " + resultado.getAnimal());
        System.out.println("Prêmio: " + resultado.getPremio());
        System.out.println("Apostas vencedoras:");
        resultado.getGanhadoras().forEach(System.out::println);
        
    sorteioObservable.setResultado(resultado);
    }

    //Resultado é passado para SorteioObservable.setResultado(), que notifica a Interface.


}
