import java.util.ArrayList;

public class mymain {
    public static void main(String[] args) throws Exception {
        /*buscas busca = new buscas(new String[][]{
            {"7","1","6"},
            {"X","2","4"},
            {"5","3","8"}});*/
        buscas busca = new buscas();
        //busca.greedy_search();
        //busca.blind_search_depth(19,true);//n<20 se não demora muito....resulta em: não resolver todos os problemas, mas a maioria!.
        //busca.stAr();
        busca.aprofundamento_iterativo(20);

     
     
        
    }
}
