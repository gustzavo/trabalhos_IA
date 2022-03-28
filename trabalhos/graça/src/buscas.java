import java.util.ArrayList;
import java.lang.Math;

public class buscas {
    private puzzle puz;
    private String[][] puzzle_game;

    public buscas(){
        this.puz = new puzzle();
        puzzle_game = puz.criar_puzzle();
    }

    public buscas(String[][] lista) throws puzzleincorreto{
        this.puz = new puzzle();
        puzzle_game = puz.criar_puzzle(lista);
        puz.show(puzzle_game);
    }

    public boolean greedy_search(int max_passos){  //comentario: horrivel, não resolve facilmente... ficando sempre num melhor local
        while( (max_passos > 0) && (!ganhei(puzzle_game))){ // enquanto ainda tenho passos e ainda não ganhei...
            String[] passos = puz.aonde_mover(puzzle_game);
            int menor = -1;
            String caminho = "";
            for(int i = 0; i < passos.length; i++){

                String[][] copia = new String[3][3];
                for(int a = 0 ; a < 3 ; a++){
                    for(int b = 0 ; b < 3; b++ ){   //fazendo uma copia
                        copia[a][b] = puzzle_game[a][b];
                    }
                }

                puz.movimenta(passos[i], copia,true);
                if(menor == -1) {
                    menor = puz.avalia(copia);             //garante o primeiro movimento
                    System.out.println("avalia:" + menor + " "+ passos[i]);
                    caminho = passos[i];
                }else{
                    int avalia = puz.avalia(copia);
                    System.out.println("avalia:" + avalia + " "+ passos[i]);
                    if(avalia<=menor){
                        menor = avalia;            //garante o melhor movimento(após primeiro)
                        caminho = passos[i];
                    }
                }
            }
            //-------------------------------------ja sei o que movimento farei, mas vou repetir? 1->2->1->2 ???
            if(puz.movimentos.size()>2 && puz.movimentos.get(puz.movimentos.size()-3) == puz.movimentos.get(puz.movimentos.size()-1)){
                puz.show(puzzle_game);
                return false; //encerra pois não vai achar o resultado...
            }
            // caso contrario, continuamos aqui...
            max_passos--;
            puz.movimenta(caminho,puzzle_game,true);
            puz.show(puzzle_game);
        }
        puz.show(puzzle_game);
        return true;
    }
    
    public void blind_search_depth(){ // metodo perigoso, pois como não tem limite, vai sempre no primeiro nó, e isso vai dar loop infinito.
        boolean[] achei = {false};
        ArrayList historico = new ArrayList<>();
        blind_search_depth_second(puzzle_game,"",achei,historico);
        if(achei[0]==true){
            System.out.println("Achei resultado!.");
            for(int i = 0; i < historico.size();i++)System.out.println(historico.get(i));
        }else{
            System.out.println("não encontrei.");
        }
        
    }

    private boolean blind_search_depth_second(String[][] game,String movimento,boolean[] achei,ArrayList hitorico){ 
        //esse metodo é para realizar busca em profundidade, o de cima é só visual.
        if(!movimento.equals("")){   
            puz.movimenta(movimento, game,false);
            hitorico.add(movimento);
        }
        if(!ganhei(game)){
            String[] possibilidades = puz.aonde_mover(game);
            for(String a: possibilidades){
                blind_search_depth_second(game,a,achei,hitorico);
            }
            return false;
        }
        achei[0] = true;
        return true;
    }

    public ArrayList blind_search_depth(int max_depth,boolean show_results){ 
        boolean[] achei = {false};
        ArrayList historico = new ArrayList<>();
        ArrayList caminho = new ArrayList<>();
        blind_search_depth_second(puzzle_game,"",achei,historico,max_depth,caminho);
        if(achei[0]==true){
            if(show_results){
                System.out.println("Achei resultado!.");
                for(int i = 0; i < caminho.size();i++)System.out.println(caminho.get(i));
            }
            return caminho;
        }else{
            if(show_results)
            System.out.println("não encontrei.");
            return null;
        }
        
    }

    private boolean blind_search_depth_second(String[][] game,String movimento,boolean[] achei,ArrayList hitorico,int max_depth,ArrayList caminho){ 
        //esse metodo é para realizar busca em profundidade, o de cima é só visual.
        if(caminho.size()==0){
            if(!movimento.equals("")){   
                puz.movimenta(movimento, game,false);
                max_depth--;
                hitorico.add(movimento);
            }
            if(!ganhei(game) && max_depth>0){
                String[] possibilidades = puz.aonde_mover(game);
                for(String a: possibilidades){
                    if(achei[0] == false){
                        String[][] copia_jogo = copia(game);
                        blind_search_depth_second(copia_jogo,a,achei,hitorico,max_depth,caminho);
                        hitorico.remove(hitorico.size()-1);
                    }
                }
                return false;
            }
            if(ganhei(game)){
                achei[0] = true;
                if(caminho.size()==0)
                for(int i = 0 ; i < hitorico.size(); i++){
                    caminho.add(hitorico.get(i));
                }
            }
            return true;
        }
        return true;
    }

    public String[][] copia(String[][] lista){
        String[][] copia = new String[3][3];
        for(int a = 0 ; a < 3 ; a++){
            for(int b = 0 ; b < 3; b++ ){   //fazendo uma copia
                copia[a][b] = lista[a][b];
            }
        }
        return copia;
    }


    public boolean ganhei(String[][] game){
        if(puz.avalia(game)== 0){
            return true;
        }
        return false;
    }

    public void stAr(){
        boolean[] achei = {false};
        ArrayList<String>[] resultado = new ArrayList[1];
        ArrayList<puzzle> movimentos= new ArrayList<>();
        stAr_rec(achei, puz,movimentos,resultado);
        if(achei[0] == true)
        for(int i = 0; i < resultado[0].size();i++){
            System.out.println(resultado[0].get(i));
        }
        else{
            System.out.println("erro");
        }
    }   

    private boolean stAr_rec(boolean[] achei,puzzle game,ArrayList<puzzle> movimentos,ArrayList<String>[] resultado) {
        achei[0] = ganhei(game.get_actual_state());
        if(achei[0]==false){
            String[][] matriz = game.get_actual_state();
            //-----------------------------------------caso eu ainda não tenha achado. entao vou listar os possiveis movimentos
            String[] movimentos_possiveis = puz.aonde_mover(matriz);
            //----------------------------------------- pegar meu score atual
            int actual_score = puz.avalia(matriz); 
            //----------------------------------------- analisar cada moviemento possivel e seu caso de peso e acresentar a lista de possiveis movimentos.
            puzzle[] next_move = new puzzle[1];
            for(int i = 0 ; i < movimentos_possiveis.length ; i++){
                next_move[0] = new puzzle();
                next_move[0].set_actual_state(game.get_actual_state());
                next_move[0].movimentos = (ArrayList<String>) game.movimentos.clone();
                next_move[0].movimenta(movimentos_possiveis[i], true);
                int ajuda = next_move[0].avalia(next_move[0].get_actual_state());
                next_move[0].cumulative_score+=actual_score + (ajuda - actual_score ); // total + (diferença[x-y])
                movimentos.add(next_move[0]); 
            }
            //----------------------------------------- OK, tenho todos os movimentos, agora... pegar o melhor!(min( (actual_score - next_move_score)))
            int indice_next = 0;
            int peso_minimo = -1;
            for(int i = 0 ; i < movimentos.size() ; i++){
                puzzle analisa = movimentos.get(i);
                if(peso_minimo == -1){
                    indice_next = i;
                    peso_minimo = analisa.cumulative_score;
                }else {
                    if(analisa.cumulative_score < peso_minimo){
                        indice_next = i;
                        peso_minimo = analisa.cumulative_score;
                    }
                }
            }
            //----------------------------------------- ok, tenho o meu proximo passo de menor custo..., agora ir incrementar o caminho cumulative score e ir para ele recursivamente
            puzzle v = movimentos.get(indice_next);
            movimentos.remove(indice_next);
            stAr_rec(achei,v,movimentos,resultado);
            



            //-----------------------------------------
        }else{
            resultado[0] = (ArrayList<String>) game.movimentos.clone();
            return true;
        }
        return false;
    }

    public boolean aprofundamento_iterativo(int max){  // só por controle mesmo
        for(int i = 1 ; i < max+1; i ++){
            ArrayList resultado = blind_search_depth(i,false);
            if(resultado != null){
                System.out.println("Encontrei resultado!!.");
                for(int a = 0; a < resultado.size(); a++) System.out.println(resultado.get(a));
                return true;
            }
        }
        System.out.println("não Encontrei resultado.");
        return false;

    }
    
}
