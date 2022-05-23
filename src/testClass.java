public class testClass {

    public static void main(String[] args) {

//        Champion champion = new Champion(5, 5, 5);
//
//        System.out.println(champion);

//        BTree bT = new BTree(3);
//
//        bT.insert(9,9);
//        bT.insert(15, 15);
//        bT.insert(17, 17);
//        bT.insert(23, 23);
//        bT.insert(67, 67);
//        bT.insert(89, 89);
//        bT.insert(34, 34);
//        bT.insert(57, 57);
//        bT.insert(199, 199);
//        bT.insert(3, 3);
//        bT.insert(4, 4);

        BTree bT = new BTree(4);

        bT.insert(34, "A");
        bT.insert(3, "A");
        bT.insert(46, "A");
        bT.insert(74, "A");
        bT.insert(56, "A");
        bT.insert(8, "A");
        bT.insert(97, "A");
        bT.insert(64, "A");
        bT.insert(14, "A");
        bT.insert(25, "A");
        bT.insert(100, "A");
        bT.insert(120, "A");
        bT.insert(15, "A");
        bT.insert(20, "A");
        bT.insert(1, "A");
        bT.insert(35, "A");
        bT.insert(34, "B");
        bT.insert(34, "C");
        bT.insert(8, "B");
        bT.insert(8, "C");
        bT.insert(8, "D");
//
//        bT.recursivePrintTree(bT.root, 0);
//
        bT.rangeSearch(23, 78);

    }

}
