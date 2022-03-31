import java.util.ArrayList;
import java.util.Collections;

public class puzzle{
    private String[][] objetivo;
    private String[][] actual_state;
    private ArrayList<String> movimentos;
    public int cumulative_score = 0;           //ajuda no stAr

    public puzzle(){
        movimentos = new ArrayList<>();
    }

    public void criar_puzzle(int n,int m) throws puzzleincorreto{
        if(n>0 && m>0){
            this.actual_state = new String[n][m];
            ArrayList<String> lista = criar_lista(n,m);
            //-------------------Vamos embaralhar ela agora
            Collections.shuffle(lista);
            //-------------------e passar para nxm
            int aj=0;
            for(int i = 0; i<n ; i++){
                for(int a = 0; a<m; a++){
                    this.actual_state[i][a] = lista.get(aj);
                    aj++;
                }
            }
            criar_objetivo(n,m);
        }else{
            throw new puzzleincorreto("Quantidade de espaços invalidos.");
        }
    }

    public void criar_puzzle(String[][] lista) throws puzzleincorreto{
        //-----------------------------------------Verificação de validade no momento
        if(lista.length>0 && lista[0].length > 0){
            ArrayList<String> lists = criar_lista(lista.length, lista[0].length);
            try{
                for(int i = 0 ; i < lista.length; i++){
                    for(int a = 0 ; a < lista[0].length; a++){
                        if(lists.contains(lista[i][a])) lists.remove(lista[i][a]);
                    }
                }
                if(lists.size()!=0){
                    throw new puzzleincorreto("dados invalidos em lista");
                }
            }catch(ArrayIndexOutOfBoundsException e){
                throw new puzzleincorreto("quantidade de n incompativel com m");
            }
            //-------------------------------------Validado!
            this.actual_state = lista;
            criar_objetivo(lista.length, lista[0].length);
        }else{throw new puzzleincorreto("quantidade de dados invalidos");}

    }

    private ArrayList<String> criar_lista(int n, int m){
        //-------------------criar os numeros + X
        ArrayList<String> lista = new ArrayList<>();
        for(int i = 1 ; i < n*m ; i++){
            lista.add(String.valueOf(i));
        }
        lista.add("X");
        return lista;
    }

    private void criar_objetivo(int n, int m){
        ArrayList<String> obj = criar_lista(actual_state.length, actual_state[0].length);
        objetivo = new String[n][m];
        int index = 0;
        for(int i = 0 ; i < n; i++){
            for(int a = 0 ; a < m; a++){
                objetivo[i][a] = obj.get(index);
                index++;
            }
        }
    }

    public void show(){
        for(int i = 0; i<actual_state.length ; i++){
            for(int a = 0; a<actual_state[i].length; a++) System.out.print(actual_state[i][a] + "|" );
            System.out.println();
        }
    }
   
    public int avalia(int n) throws puzzleincorreto{
        switch(n){
            case(1): return avalia1();
        }
        throw new puzzleincorreto("avalia("+n+") -> não exite ou não foi implementado ainda");
    }

    private int[] find(String element,String[][] lista){ // deixei lista pois posso fazer tanto de objetivo quanto de lista
        for(int i = 0 ; i < lista.length ;i++){
            for(int j =0;j < lista[i].length; j++){
                if(lista[i][j].equals(element)){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1,-1};
    }

    private int avalia1(){ // avaliar peça(objetivo)
        int peso = 0;
        for(int i = 0 ; i < actual_state.length ; i++){
            for(int j = 0 ; j < actual_state[i].length; j++){
                String element = actual_state[i][j];
                int[] x = find(element, objetivo);
                int x1,y1=0;

                x1 = (x[0]>i) ? x[0]-i:i-x[0];
                y1 = (x[1]>j) ? x[1]-j:j-x[1];

                peso += x1+y1;
            }
        }
        return peso;
        
    }

    public String[] aonde_mover(){
        int[] aqui = find("X",actual_state);
        ArrayList<String> movimento = new ArrayList<>();
        if(aqui[1]<actual_state[0].length-1){
            movimento.add("direita");
        }
        if(aqui[1]>0){
            movimento.add("esquerda");
        }
        if(aqui[0]<actual_state.length-1){
            movimento.add("baixo");
        }
        if(aqui[0]>0){
            movimento.add("cima");
        }
        Collections.shuffle(movimento);
        String[] movimenta = new String[movimento.size()];
        for(int i = 0; i < movimento.size();i++){
            movimenta[i]=movimento.get(i);         //passando para vetor pois nao sei trabalhar com ArrayList
        }
        return movimenta;
    }

    public void movimenta(String mover,boolean salvar) throws puzzleincorreto{
        int[] aqui = find("X",this.actual_state);
        try{
            if(mover.equals("direita")){
                this.actual_state[aqui[0]][aqui[1]] = this.actual_state[aqui[0]][aqui[1]+1];
                this.actual_state[aqui[0]][aqui[1]+1] = "X";
                if(salvar == true)movimentos.add("direita");
            }else if(mover.equals("esquerda")){
                this.actual_state[aqui[0]][aqui[1]] = this.actual_state[aqui[0]][aqui[1]-1];
                this.actual_state[aqui[0]][aqui[1]-1] = "X";
                if(salvar == true)movimentos.add("esquerda");
            }else if(mover.equals("baixo")){
                this.actual_state[aqui[0]][aqui[1]] = this.actual_state[aqui[0]+1][aqui[1]];
                this.actual_state[aqui[0]+1][aqui[1]] = "X";
                if(salvar == true)movimentos.add("baixo");
            }else if(mover.equals("cima")){
                this.actual_state[aqui[0]][aqui[1]] = this.actual_state[aqui[0]-1][aqui[1]];
                this.actual_state[aqui[0]-1][aqui[1]] = "X";
                if(salvar == true)movimentos.add("cima");
                
            }
        }catch(IndexOutOfBoundsException e){
            throw new puzzleincorreto("index além do permitido mover, 171");
        }
    }

    public String[][] get_actual_state(){
        String[][] state = new String[actual_state.length][actual_state[0].length];
        for(int i = 0 ; i < actual_state.length; i++)
            for(int a = 0 ; a < actual_state[i].length; a++) 
                state[i][a] = actual_state[i][a];
        return state;
    }

    public void set_actual_state(String[][] state){
        this.actual_state =state;
        criar_objetivo(state.length, state[0].length);
    }

    public ArrayList<String> getmovimentos(){
        return (ArrayList<String>) movimentos.clone();
    }

    public void setmovimentos(ArrayList<String> movimentos){
        this.movimentos = movimentos;
    }

    public void desfazer() throws puzzleincorreto{
        movimenta(inverso(movimentos.get(movimentos.size()-1)), false);
        movimentos.remove(movimentos.size()-1);

    }

    private String inverso(String a){
        switch(a){
            case("direita") : return "esquerda";
            case("esquerda"): return "direita";
            case("baixo")   : return "cima";
            case("cima")    : return "baixo";
        }
        return null;
    }
}
