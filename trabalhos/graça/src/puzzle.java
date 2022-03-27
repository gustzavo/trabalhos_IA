import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class puzzle{
    public String[][] objetivo = new String[][]{{"1","2","3"},{"4","5","6"},{"7","8","X"}};
    private String[][] actual_state;
    public ArrayList<String> movimentos;
    public int cumulative_score = 0;           //ajuda no stAr

    public puzzle(){
        movimentos = new ArrayList<>();
    }

    public String[][] criar_puzzle(){
        String[][] puzzle_game = new String[3][3];
        //-------------------embaralhar
        List<String> lista = Arrays.asList("1","2","3","4","5","6","7","8","X");
        Collections.shuffle(lista);
        //-------------------passar para 3x3
        int aj=0;
        for(int i = 0; i<3 ; i++){
            for(int a = 0; a<3; a++){
                puzzle_game[i][a] = lista.get(aj);
                aj++;
            }
        }
        System.out.println("estado inicial:");
        show(puzzle_game);
        this.actual_state = puzzle_game;
        return puzzle_game;
    }

    public void show(String[][] lista){
        for(int i = 0; i<3 ; i++){
            for(int a = 0; a<3; a++) System.out.print(lista[i][a] + "|" );
            System.out.println();
        }
    }
   
    public String[][] criar_puzzle(String[][] lista) throws puzzleincorreto{
        for(int i = 0; i < objetivo.length;i++){
            for(int j = 0; j < objetivo.length;j++){
                int[] resp = find(objetivo[i][j],lista);
                if(resp[0]==-1) throw new puzzleincorreto();
            }
        } 
        this.actual_state = lista;
        return lista;
    }

    public int avalia(String[][] lista){ // avaliar peça(objetivo)
        int peso = 0;
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 3; j++){
                String element = lista[i][j];
                int[] x = find(element, objetivo);
                int x1,y1=0;
                if(x[0]>i) x1 = x[0]-i;
                else x1 = i-x[0];

                if(x[1]>j)y1 = x[1]-j;
                else y1 = j-x[1];
                
                peso += x1+y1;
                //System.out.println(element + " = " + (x1+y1));
            }
        }
        //System.out.println(peso);
        return peso;
        
    }

    public int[] find(String element,String[][] lista){
        for(int i = 0 ; i < 3 ;i++){
            for(int j =0;j < 3; j++){
                if(lista[i][j].equals(element)){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1,-1};
    }

    public String[] aonde_mover(String[][] lista){
        int[] aqui = find("X",lista);
        ArrayList<String> movimento = new ArrayList<>();
        if(aqui[1]<2){
            movimento.add("direita");
        }
        if(aqui[1]>0){
            movimento.add("esquerda");
        }
        if(aqui[0]<2){
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

    public void movimenta(String mover,String[][] lista,boolean salvar){
        int[] aqui = find("X",lista);
        if(mover.equals("direita")){
            lista[aqui[0]][aqui[1]] = lista[aqui[0]][aqui[1]+1];
            lista[aqui[0]][aqui[1]+1] = "X";
            if(salvar == true)movimentos.add("direita");
        }else if(mover.equals("esquerda")){
            lista[aqui[0]][aqui[1]] = lista[aqui[0]][aqui[1]-1];
            lista[aqui[0]][aqui[1]-1] = "X";
            if(salvar == true)movimentos.add("esquerda");
        }else if(mover.equals("baixo")){
            lista[aqui[0]][aqui[1]] = lista[aqui[0]+1][aqui[1]];
            lista[aqui[0]+1][aqui[1]] = "X";
            if(salvar == true)movimentos.add("baixo");
        }else if(mover.equals("cima")){
            lista[aqui[0]][aqui[1]] = lista[aqui[0]-1][aqui[1]];
            lista[aqui[0]-1][aqui[1]] = "X";
            if(salvar == true)movimentos.add("cima");
            
        }
    }

    public void movimenta(String mover,boolean salvar){
        int[] aqui = find("X",this.actual_state);
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
    }

    public String[][] get_actual_state(){
        String[][] state = new String[actual_state.length][actual_state[0].length];
        for(int i = 0 ; i < actual_state.length; i++)for(int a = 0 ; a < actual_state.length; a++) state[i][a] = actual_state[i][a];
        return state;
    }

    public void set_actual_state(String[][] state){
        this.actual_state =state;
    }

}
