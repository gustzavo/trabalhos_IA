public class mymain {
    public static void main(String[] args) throws Exception {
        buscas busca = new buscas(new String[][]{
            {"3","2","1"},
            {"4","8","X"},
            {"6","5","7"}});
        //busca.greedy_search(10);
        /*buscas busca = new buscas(new String[][]{
            {"1","2","3"},
            {"X","5","6"},
            {"4","7","8"}});*/
        //buscas busca = new buscas(3,3);
        //busca.puz.show();
        //busca.greedy_search();
        //busca.blind_search_depth(19);//n<20 se não demora muito....resulta em: não resolver todos os problemas, mas a maioria!.
        //busca.stAr();
        //busca.aprofundamento_iterativo(20);
        busca.breadth_search();
        //busca.IDAstar(20);
    }


}
