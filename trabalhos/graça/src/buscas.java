import java.util.ArrayList;

public class buscas {
    public puzzle puz = new puzzle();

    public buscas(int n,int m) throws puzzleincorreto{ // linha e coluna
        puz.criar_puzzle(n , m);
    }

    public buscas(String[][] lista) throws puzzleincorreto{
        puz.criar_puzzle(lista);
    }

    public boolean ganhei() throws puzzleincorreto{
        int a = puz.avalia(1);
        if(a== 0){
            return true;
        }
        return false;
    }

    private puzzle copiar(puzzle game){
        puzzle novo = new puzzle();
        novo.set_actual_state(game.get_actual_state());
        novo.cumulative_score = game.cumulative_score;
        novo.setmovimentos(game.getmovimentos());
        return novo;
    }

    public boolean greedy_search(int max_passos) throws puzzleincorreto{  //comentario: horrivel, não resolve facilmente... ficando sempre num melhor local
        while( (max_passos > 0) && (!ganhei())){ // enquanto ainda tenho passos e ainda não ganhei...
            String[] passos = puz.aonde_mover();
            int menor = -1;
            String caminho = "";
            for(int i = 0; i < passos.length; i++){
                
                puzzle copia = copiar(puz);

                copia.movimenta(passos[i],true);
                if(menor == -1) {
                    menor = copia.avalia(1);             //garante o primeiro movimento
                    //System.out.println("avalia:" + menor + " "+ passos[i]);
                    caminho = passos[i];
                }else{
                    int avalia = copia.avalia(1);
                    //System.out.println("avalia:" + avalia + " "+ passos[i]);
                    if(avalia<=menor){
                        menor = avalia;            //garante o melhor movimento(após primeiro)
                        caminho = passos[i];
                    }
                }
            }
            //-------------------------------------ja sei o que movimento farei, mas vou repetir? 1->2->1->2 ???
            if(puz.getmovimentos().size()>2 && puz.getmovimentos().get(puz.getmovimentos().size()-3) == puz.getmovimentos().get(puz.getmovimentos().size()-1)){
                System.out.println("impaquei...");
                return false; //encerra pois não vai achar o resultado...
            }
            // caso contrario, continuamos aqui...
            max_passos--;
            puz.movimenta(caminho,true);
            for(int i = 0 ; i < puz.get_actual_state().length*2;i++)System.out.print("*");
            System.out.println(caminho);
            puz.show();
        }
        System.out.println("não fui capaz de achar solução com " + max_passos+ " passos");
        return true;
    }
    
    public void blind_search_depth() throws puzzleincorreto{ // metodo perigoso, pois como não tem limite, vai sempre no primeiro nó, e isso vai dar loop infinito.
        boolean[] achei = {false};
        blind_search_depth_second(achei);
        if(achei[0]==true){
            System.out.println("Achei resultado!.");
            ArrayList<String> historico = puz.getmovimentos();
            for(int i = 0; i < historico.size();i++)System.out.println(historico.get(i));
        }else{
            System.out.println("não encontrei.");
        }
        
    }

    private boolean blind_search_depth_second(boolean[] achei) throws puzzleincorreto{ 
        //esse metodo é para realizar busca em profundidade, o de cima é só visual.
        if(!ganhei()){
            String[] possibilidades = puz.aonde_mover();
            for(String a: possibilidades){
                puz.movimenta(a, true);
                if(achei[0] == false){
                    puz.desfazer();
                }
            }
            return false;
        }
        achei[0] = true;
        return true;
    }

    public ArrayList<String> blind_search_depth(int max_depth) throws puzzleincorreto{ 
        boolean[] achei = {false};
        blind_search_depth_second(achei,max_depth);
        if(achei[0]==true){
            System.out.println("Achei resultado!.");
            for(String a: puz.getmovimentos())System.out.println(a);
            return puz.getmovimentos();
        }else{
            System.out.println("não encontrei.");
        }
        return null;
        
    }

    private boolean blind_search_depth_second(boolean[] achei,int max_depth) throws puzzleincorreto{ 
        //esse metodo é para realizar busca em profundidade, o de cima é só visual.
        if(ganhei()){
            achei[0] = true;
            return true;
        }
        else if(max_depth>0 && achei[0] == false ){
            for(String mover: puz.aonde_mover()){
                puz.movimenta(mover, true);
                blind_search_depth_second(achei,max_depth-1);
                if(achei[0] == true) return true;
                puz.desfazer();
            }
        }
        return false;
        
        
        
    }

    public void stAr() throws puzzleincorreto{
        boolean[] achei = {false};
        ArrayList<puzzle>[] movimentos= new ArrayList[1];
        movimentos[0]= new ArrayList<>();
        stAr_rec(achei, puz,movimentos);
        if(achei[0] == true){
            for(String a: puz.getmovimentos()){
                System.out.println(a);
                
            }
            puz.show();
        }
        else{
            System.out.println("erro");
        }
    }   

    private boolean stAr_rec(boolean[] achei,puzzle game,ArrayList<puzzle>[] movimentos) throws puzzleincorreto {
        achei[0] = game.avalia(1) == 0;
        if(achei[0]==false){ 
            //----------------------------------------- pegar meu score atual
            int actual_score = game.avalia(1); 
            //----------------------------------------- analisar cada moviemento possivel e seu caso de peso e acresentar a lista de possiveis movimentos.
            puzzle[] next_move = new puzzle[1]; 
            for(String movimentos_possiveis : game.aonde_mover()){
                next_move[0] = copiar(game);
                next_move[0].movimenta(movimentos_possiveis, true);
                int ajuda = next_move[0].avalia(1);
                next_move[0].cumulative_score+=actual_score + (ajuda - actual_score ); // total + (diferença[x-y])
                movimentos[0].add(next_move[0]); 
            }
            //----------------------------------------- OK, tenho todos os movimentos, agora... pegar o melhor!(min( (actual_score - next_move_score)))
            int indice_next = 0;
            int peso_minimo = -1;
            for(int i = 0 ; i < movimentos[0].size() ; i++){
                puzzle analisa = movimentos[0].get(i);
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
            puzzle v = movimentos[0].get(indice_next);
            movimentos[0].remove(indice_next);
            stAr_rec(achei,v,movimentos);
            //-----------------------------------------
        }else{
            puz = game;
            return true;
        }
        return false;
    }

    public boolean aprofundamento_iterativo(int max) throws puzzleincorreto{  // só por controle mesmo
        for(int i = 1 ; i < max+1; i ++){
            boolean[] achei = {false};
            blind_search_depth_second(achei,i);
            ArrayList resultado = puz.getmovimentos();
            if(achei[0]==true){
                System.out.println("Encontrei resultado!!.");
                for(int a = 0; a < resultado.size(); a++) System.out.println(resultado.get(a));
                return true;
            }
        }
        System.out.println("não Encontrei resultado.");
        return false;
    }

    public boolean breadth_search() throws puzzleincorreto{
        if(ganhei()) {
            //ja veio resolvido
            System.out.println("ja esta resolvido");
            return true;
        }
        boolean continua = true;
        ArrayList<puzzle> fila = new ArrayList<>();
        fila.add(puz);
        while(continua){
            puz= fila.get(0);
            for(String a : puz.aonde_mover()){
                puzzle novo = copiar(puz);
                novo.movimenta(a, true);
                if(ganhei()){
                    continua = false;
                }else fila.add(novo);
            }
            fila.remove(0);
        }
        for(String a : puz.getmovimentos()) System.out.println(a);
        return true;
    }

    /*public String[][] copia(String[][] lista){
        String[][] copia = new String[3][3];
        for(int a = 0 ; a < 3 ; a++){
            for(int b = 0 ; b < 3; b++ ){   //fazendo uma copia
                copia[a][b] = lista[a][b];
            }
        }
        return copia;
    }


    

            DEIXEI AQUI COMENTADO POIS A FUNÇÃO A CIMA EU NÃO IREI USAR, E ABAIXO POIS ESTÁ INCOMPLETO

    

    

    public void IDAstar(int max_depth){
        String[] movimentos_possiveis = this.puz.aonde_mover(this.puz.get_actual_state());
        puzzle next_move;
        int pesoMinimo;
        String movimentoRealizado;
        int contadorMovimentos = 0;
        while ((! ganhei(this.puz.get_actual_state())) && (max_depth > contadorMovimentos)){
            movimentoRealizado = "";
            pesoMinimo=-1;
            for (String movimento : movimentos_possiveis){
                next_move = new puzzle();
                next_move.set_actual_state(this.puz.get_actual_state());
                next_move.movimentos = (ArrayList<String>) this.puz.movimentos.clone();
                next_move.movimenta(movimento,true);
                int ajuda = next_move.avalia(next_move.get_actual_state());
                next_move.cumulative_score+=puz.avalia(this.puz.get_actual_state())+(ajuda-puz.avalia(this.puz.get_actual_state()));
                if (-1 == pesoMinimo) {
                    pesoMinimo = next_move.cumulative_score;
                    movimentoRealizado = movimento;
                }else if(pesoMinimo > next_move.cumulative_score){
                    pesoMinimo = next_move.cumulative_score;
                    movimentoRealizado = movimento;
                }
            }
            this.puz.movimenta(movimentoRealizado,true);
            contadorMovimentos++;
        }
        if ((contadorMovimentos > max_depth) && (! ganhei(this.puz.get_actual_state()))){
            System.out.println("Erro, não foi possível encontrar uma solução para o puzzle com menos de " + max_depth + " movimentos.");
            return;
        }
        System.out.println("Encontrada solução em " + contadorMovimentos + " movimentos.");
        System.out.println("Movimentos a serem realizados:");
        for (String movimento : this.puz.movimentos){
            System.out.println(movimento);
        }
    }*/
    
}
