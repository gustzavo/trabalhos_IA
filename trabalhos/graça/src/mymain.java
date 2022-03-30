import java.util.ArrayList;

public class mymain {
    public static void main(String[] args) throws Exception {
        /*buscas busca = new buscas(new String[][]{
            {"4","2","3"},
            {"7","1","6"},
            {"8","5","X"}});*/
        buscas busca = new buscas();
        //busca.greedy_search();
<<<<<<< HEAD
        //busca.blind_search_depth(19,true);//n<20 se não demora muito....resulta em: não resolver todos os problemas, mas a maioria!.
        //busca.stAr();
        //busca.aprofundamento_iterativo(20);
        busca.breadth_search();
=======
        //busca.blind_search_depth(20);//n<20 se não demora muito....resulta em: não resolver todos os problemas, mas a maioria!.
        //busca.stAr();
        busca.IDAstar(20);
>>>>>>> d0e057b (alterado para testar o novo método IDA*)
    }


}
